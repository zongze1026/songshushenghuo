package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.yitkeji.channel.inter.CashChannel;
import com.yitkeji.channel.model.ChannelParams;
import com.yitkeji.channel.model.TonglianRes;
import com.yitkeji.channel.util.SnowflakeIdWorker;
import com.yitkeji.channel.util.StringUtil;
import com.yitkeji.songshushenghuo.service.TonglianBindService;
import com.yitkeji.songshushenghuo.service.TonglianRegistedService;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.TonglianCfg;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.*;
import com.yitkeji.tonglian.Tonglian;
import com.yitkeji.tonglian.model.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TonglianChannel extends BaseChannel<TonglianCfg> implements CashChannel<TonglianCfg> {

    private Tonglian tonglian;
    private TonglianRegistedService tonglianRegistedService;
    private TonglianBindService tonglianBindService;
    private String notifyUrl;

    private TonglianChannel(){
        super(Channel.TONGLIAN);
        tonglianRegistedService = SpringUtil.getBean(TonglianRegistedService.class);
        tonglianBindService = SpringUtil.getBean(TonglianBindService.class);
        this.initCfg();
    }

    private static final class ChannelHolder{
        private static final TonglianChannel CHANNEL = new TonglianChannel();
    }

    public static final TonglianChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg();
        return ChannelHolder.CHANNEL;
    }


    private void initCfg(){
        if(needReloadCfg(SystemCfg.getInstance().getChannel().getTonglian(), cfg)){
            cfg = SystemCfg.getInstance().getChannel().getTonglian();
            tonglian = new Tonglian(cfg.getBaseurl(), cfg.getMerchantNo(), cfg.getKey());
            tonglian.setLogger(channelLogger);
            notifyUrl = "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + cfg.getNotifyUrl();
        }
    }


    private TonglianRes parseRes(String result){
        TonglianRes tonglianRes = new TonglianRes();
        try {
            tonglianRes = JSON.parseObject(result, TonglianRes.class);
        }catch (Exception e){
            e.printStackTrace();
            tonglianRes.setRetmsg("接口响应异常");
        }
        return tonglianRes;
    }

    /**
     * 进件
     * @param debitCard 结算卡
     * @return 成功时返回TonglianRegisted对象，失败时返回String对象（失败原因的描述）
     */
    private Object registMerchant(Card debitCard, UserRate userRate){

        Register register = new Register();
        register.setOutcusid(SnowflakeIdWorker.getId(0,0));
        register.setCusname(debitCard.getName() + StringUtil.randomNum(12 - debitCard.getName().length()));
        register.setCusshortname(debitCard.getName());
        register.setMerprovice("330000");
        register.setAreacode("330100");
        register.setLegal(debitCard.getName());
        register.setIdno(debitCard.getIdcard());
        register.setPhone(debitCard.getPhone());
        register.setAddress("浙江杭州滨江");
        register.setAcctid(debitCard.getCardNo());
        register.setAcctname(debitCard.getName());
        register.setProdlist(userRate.getCashRate() * 100 + "");
        register.setSettfee(userRate.getCashFixedAmount() / 100 + "");

        TonglianRes tonglianRes = parseRes(tonglian.regist(register));
        if(!"SUCCESS".equals(tonglianRes.getRetcode())){
            return tonglianRes.getRetmsg();
        }
        TonglianRegisted tonglianRegisted = new TonglianRegisted();
        tonglianRegisted.setUserId(debitCard.getUserId());
        tonglianRegisted.setName(debitCard.getName());
        tonglianRegisted.setIdcard(debitCard.getIdcard());
        tonglianRegisted.setDebitCardId(debitCard.getCardId());
        tonglianRegisted.setCusid(tonglianRes.getCusid());
        tonglianRegisted.setOutcusid(tonglianRes.getOutcusid());
        tonglianRegisted.setDebitRate(userRate.getCashRate());
        tonglianRegisted.setCreditRate(userRate.getCashRate());
        tonglianRegisted.setWithdrawDepositRate(0.0);
        tonglianRegisted.setWithdrawDepositSingleFee(userRate.getCashFixedAmount());
        tonglianRegistedService.add(tonglianRegisted);
        return tonglianRegisted;
    }

    private Object modifyFeeInfo(TonglianRegisted tonglianRegisted, Card card, UserRate userRate){

        ModifyFeeInfo modifyFeeInfo = new ModifyFeeInfo();
        modifyFeeInfo.setCusid(tonglianRegisted.getCusid());
        modifyFeeInfo.setAcctid(card.getCardNo());
        modifyFeeInfo.setProdlist(userRate.getCashRate() * 100 + "");
        modifyFeeInfo.setSettfee(userRate.getCashFixedAmount() / 100 + "");

        TonglianRes tonglianRes = parseRes(tonglian.modifyFeeInfo(modifyFeeInfo));
        if(!"SUCCESS".equals(tonglianRes.getRetcode())){
            return tonglianRes.getRetmsg();
        }else{
            tonglianRegisted.setDebitCardId(card.getCardId());
            tonglianRegisted.setDebitRate(userRate.getCashRate());
            tonglianRegisted.setCreditRate(userRate.getCashRate());
            tonglianRegisted.setWithdrawDepositRate(0.00);
            tonglianRegisted.setWithdrawDepositSingleFee(userRate.getCashFixedAmount());
            tonglianRegistedService.updateByPrimaryKey(tonglianRegisted, "debitCardId", "debitRate", "creditRate", "withdrawDepositRate", "withdrawDepositSingleFee");
            return tonglianRegisted;
        }
    }

    /**
     * 绑卡
     * @param tonglianRegisted
     * @param card
     * @return
     */
    private Object bindCard(TonglianRegisted tonglianRegisted, Card card){

        BindCard bindCard = new BindCard();
        bindCard.setCusid(tonglianRegisted.getCusid());
        bindCard.setMeruserid(SnowflakeIdWorker.getId(2,2));
        bindCard.setCardno(card.getCardNo());
        bindCard.setAcctname(card.getName());
        bindCard.setValiddate(card.getExpiryDate());
        bindCard.setCvv2(card.getCvv());
        bindCard.setIdno(card.getIdcard());
        bindCard.setTel(card.getPhone());

        TonglianRes tonglianRes = parseRes(tonglian.bindCard(bindCard));
        if(!"SUCCESS".equals(tonglianRes.getRetcode()) || !"1999".equals(tonglianRes.getTrxstatus())){
            return tonglianRes.getRetmsg();
        }

        TonglianBind tonglianBind = new TonglianBind();
        tonglianBind.setCardId(card.getCardId());
        tonglianBind.setCusid(tonglianRegisted.getCusid());
        tonglianBind.setRequestNo(bindCard.getMeruserid());
        RedisUtil.set(CacheKey.TONGLIAN.getKey(tonglianBind.getRequestNo()), JSON.toJSONString(tonglianBind), 1, TimeUnit.HOURS);
        return tonglianBind;
    }


    private Object confirmBindCard(TonglianRegisted tonglianRegisted, Card card, String bindRequestNo, String code){

        ConfirmBindCard confirmBindCard = new ConfirmBindCard();
        confirmBindCard.setCusid(tonglianRegisted.getCusid());
        confirmBindCard.setMeruserid(SnowflakeIdWorker.getId(2,2));
        confirmBindCard.setCardno(card.getCardNo());
        confirmBindCard.setAcctname(card.getName());
        confirmBindCard.setValiddate(card.getExpiryDate());
        confirmBindCard.setCvv2(card.getCvv());
        confirmBindCard.setIdno(card.getIdcard());
        confirmBindCard.setTel(card.getPhone());
        confirmBindCard.setSmscode(code);

        TonglianRes tonglianRes = parseRes(tonglian.confirmBindCard(confirmBindCard));
        if(!"SUCCESS".equals(tonglianRes.getRetcode()) && !"0000".equals(tonglianRes.getTrxstatus())){
            return tonglianRes.getRetmsg();
        }
        Object object = RedisUtil.get(CacheKey.TONGLIAN.getKey(bindRequestNo));
        if(object == null){
            return "验证码失效";
        }
        TonglianBind tonglianBind = JSON.parseObject((String)object, TonglianBind.class);
        tonglianBind.setAgreeid(tonglianRes.getAgreeid());
        tonglianBindService.add(tonglianBind);
        return tonglianBind;
    }

    /**
     * 支付
     * @param tonglianRegisted
     * @param order
     * @return
     */
    private Object pay(TonglianRegisted tonglianRegisted, TonglianBind tonglianBind, Order order){

        Pay pay = new Pay();
        pay.setCusid(tonglianRegisted.getCusid());
        pay.setOrderid(order.getOrderNo());
        pay.setAgreeid(tonglianBind.getAgreeid());
        pay.setAmount(order.getMoney() + "");
        pay.setSubject(order.getDesc());
        pay.setCity("010");
        pay.setTrxreserve(order.getDesc());
        pay.setNotifyurl(notifyUrl);

        TonglianRes tonglianRes = parseRes(tonglian.pay(pay));
        if(!"SUCCESS".equals(tonglianRes.getRetcode()) || !"1999".equals(tonglianRes.getTrxstatus())){
            return tonglianRes.getRetmsg();
        }
        RedisUtil.set(CacheKey.TONGLIAN.getKey(order.getOrderId()), JSON.toJSONString(tonglianRes.getThpinfo()), 1, TimeUnit.HOURS);
        return tonglianRes;
    }


    private Object confirmPay(TonglianRegisted tonglianRegisted, TonglianBind tonglianBind, Order order, String code, int tried){
        ConfirmPay confirmPay = new ConfirmPay();
        confirmPay.setCusid(tonglianRegisted.getCusid());
        confirmPay.setTrxid(order.getTrackingNo());
        confirmPay.setAgreeid(tonglianBind.getAgreeid());
        confirmPay.setSmscode(code);

        Object object = RedisUtil.get(CacheKey.TONGLIAN.getKey(order.getOrderId()));
        if(object == null){
            return "交易失效";
        }
        confirmPay.setThpinfo((String)object);
        TonglianRes tonglianRes = parseRes(tonglian.confirmPay(confirmPay));
        if(!"SUCCESS".equals(tonglianRes.getRetcode())){
            return tonglianRes.getRetmsg();
        }
        if("1999".equals(tonglianRes.getTrxstatus())){
            if(tried < 5){
                return confirmPay(tonglianRegisted, tonglianBind, order, code, tried + 1);
            }else{
                return "重发交易超时";
            }
        }
        if(!"0000".equals(tonglianRes.getTrxstatus()) && !"2000".equals(tonglianRes.getTrxstatus())){
            return tonglianRes.getErrmsg();
        }
        return tonglianRes;
    }


    /**
     * 查询订单
     * @param tonglianRegisted
     * @param order
     * @param tried
     * @return
     */
    private Object queryPay(TonglianRegisted tonglianRegisted, Order order, Integer tried){

        QueryPay queryPay = new QueryPay();
        queryPay.setCusid(tonglianRegisted.getCusid());
        queryPay.setTrxid(order.getTrackingNo());

        TonglianRes tonglianRes = parseRes(tonglian.queryPay(queryPay));
        if(!"SUCCESS".equals(tonglianRes.getRetcode())){
            return tonglianRes.getRetmsg();
        }else{
            switch (tonglianRes.getTrxstatus()){
                case "0000": return tonglianRes;
                case "2000":
                    try {
                        if(tried > MAX_RETRIE_TIME){
                            return "查询订单超时";
                        }
                        Thread.sleep(Double.valueOf(Math.pow(RETRIE_POW, tried)).longValue() * 1000);
                        return queryPay(tonglianRegisted, order, tried + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return "查询订单异常";
                    }
                default: return tonglianRes.getRetmsg();
            }
        }
    }

    private Object queryMerchantWallet(TonglianRegisted tonglianRegisted, Integer tried){
        QueryMerchantWallet queryMerchantWallet = new QueryMerchantWallet();
        queryMerchantWallet.setCusid(tonglianRegisted.getCusid());
        TonglianRes tonglianRes = parseRes(tonglian.queryMerchantWallet(queryMerchantWallet));
        if(!"SUCCESS".equals(tonglianRes.getRetcode())){
            return tonglianRes.getRetmsg();
        }
        int walletMoney = Integer.parseInt(tonglianRes.getBalance());
        if(walletMoney < 1){
            if(tried > 10){
                return "查询钱包余额超时";
            }
            try {
                Thread.sleep(10000 * tried);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "查询钱包余额失败";
            }
            return queryMerchantWallet(tonglianRegisted, tried + 1);
        }
        return walletMoney;
    }

    private Object withdrawDeposit(TonglianRegisted tonglianRegisted, Order order){
        WithdrawDeposit withdrawDeposit = new WithdrawDeposit();
        withdrawDeposit.setCusid(tonglianRegisted.getCusid());
        withdrawDeposit.setOrderid(SnowflakeIdWorker.getId(5,1));
        withdrawDeposit.setTrxreserve(order.getDesc());
        withdrawDeposit.setNotifyurl(notifyUrl);
        TonglianRes tonglianRes = parseRes(tonglian.withdrawDeposit(withdrawDeposit));
        if(!"SUCCESS".equals(tonglianRes.getRetcode())){
            return tonglianRes.getRetmsg();
        }
        return tonglianRes;
    }

    private Object queryWithdraw(TonglianRegisted tonglianRegisted, String orderId){
        QueryWithdraw queryWithdraw = new QueryWithdraw();
        queryWithdraw.setCusid(tonglianRegisted.getCusid());
        queryWithdraw.setTrxid(orderId);
        TonglianRes tonglianRes = parseRes(tonglian.queryWithdraw(queryWithdraw));
        if(!"SUCCESS".equals(tonglianRes.getRetcode())){
            return tonglianRes.getRetmsg();
        }
        return tonglianRes;
    }

    /**
     * 查询钱包
     * @param tonglianRegisted
     * @return
     */
    public String queryWallet(TonglianRegisted tonglianRegisted){
        QueryMerchantWallet queryMerchantWallet = new QueryMerchantWallet();
        queryMerchantWallet.setCusid(tonglianRegisted.getCusid());
        String result = tonglian.queryMerchantWallet(queryMerchantWallet);
        return result;
    }

    @Override
    public Object cashFirst(ChannelParams params){
        Card card = params.getCard();
        UserRate userRate = userService.computeUserRate(params.getUser());

        Object result;
        // 进件
        TonglianRegisted registed = tonglianRegistedService.findByIdcard(card.getIdcard());
        if(null == registed){
            result = this.registMerchant(params.getDebitCard(), userRate);
            if(result instanceof String){
                return result;
            }else if(result instanceof TonglianRegisted){
                registed = (TonglianRegisted)result;
            }else{
                return "进件失败";
            }
        }

        // 绑卡
        TonglianBind tonglianBind = tonglianBindService.findByCardId(card.getCardId());
        if(null == tonglianBind){
            if(null != params.getRequestNo() && null != params.getCode()){
                result = this.confirmBindCard(registed, card, params.getRequestNo(), params.getCode());
            }else{
                result = this.bindCard(registed, card);
            }
            if(result instanceof String){
                return result;
            }else if(result instanceof TonglianBind){
                return result;
            }else{
                return "绑卡失败";
            }
        }
        return tonglianBind;
    }

    @Override
    public Object cash(ChannelParams params){
        Card card = params.getCard();
        UserRate userRate = userService.computeUserRate(params.getUser());
        Order order;
        if(params.getOrderId() == null){
            order = orderService.createCashOrder(this.getChannel(), params.getUser(), card, params.getDebitCard(), params.getMoney());
        }else{
            order = orderService.findByPrimaryKey(params.getOrderId());
        }

        if(cfg.getDisabled()){
            return orderService.failOrder(order, CLOSEDMSG);
        }

        if(cfg.getBusinessType() != null && !BusinessType.CASH.equals(this.cfg.getBusinessType())){
            return orderService.failOrder(order, NOSUPPORTED_BUSINESS);
        }
        Object result;

        // 拉取进件信息
        TonglianRegisted registed = tonglianRegistedService.findByIdcard(card.getIdcard());
        if(null == registed){
            return orderService.failOrder(order, "尚未进件");
        }

        TonglianBind tonglianBind = tonglianBindService.findByCardId(card.getCardId());

        //修正交易费率和提现卡
        if(registed.getCreditRate() != userRate.getCashRate()
                || registed.getDebitRate() != userRate.getCashRate()
                || !registed.getDebitCardId().equals(params.getUser().getDebitCardId())
                || registed.getWithdrawDepositSingleFee() != userRate.getCashFixedAmount()){
            result = this.modifyFeeInfo(registed, params.getDebitCard(), userRate);
            if(result instanceof String){
                return orderService.failOrder(order, (String)result);
            }
            registed = (TonglianRegisted)result;
        }


        // 支付
        Double rateMoney = Math.ceil(order.getMoney() * registed.getDebitRate());
        order.setRateMoney(rateMoney.intValue());
        if(params.getOrderId() == null){
            result = this.pay(registed, tonglianBind, order);
        }else{
            result = this.confirmPay(registed, tonglianBind, order, params.getCode(), 0);
        }

        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }else if (result instanceof TonglianRes){
            TonglianRes consumeResult = (TonglianRes)result;
            order.setTrackingNo(consumeResult.getTrxid());
            orderService.updateByPrimaryKey(order, "trackingNo", "rateMoney");
            if("1999".equals(consumeResult.getTrxstatus())){
                return order.getOrderId();
            }
        }else{
            return orderService.failOrder(order, "支付失败");
        }

        TonglianRegisted finalRegisted = registed;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // 查询
                Object result = queryPay(finalRegisted, order, 1);
                if(result instanceof String){
                    orderService.failOrder(order, (String)result);
                    return;
                }

                // 查询钱包
                result = queryMerchantWallet(finalRegisted, 1);
                if(result instanceof String){
                    orderService.failOrder(order, (String)result);
                    return;
                }

                // 提现
                result = withdrawDeposit(finalRegisted, order);
                if(result instanceof String){
                    orderService.failOrder(order, (String)result);
                    return;
                }
                Double withdrawRateMoney = finalRegisted.getWithdrawDepositSingleFee();
                order.setRateMoney(order.getRateMoney() + withdrawRateMoney.intValue());
                order.setArriveMoney(order.getMoney() - order.getRateMoney());
                orderService.successOrder(order);
            }
        }, 0);

        return order;
    }

    public static void main(String[] args) {

    }

}
