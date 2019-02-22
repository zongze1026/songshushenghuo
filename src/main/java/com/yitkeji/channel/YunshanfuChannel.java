package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.yitkeji.channel.inter.RepaymentChannel;
import com.yitkeji.channel.model.ChannelParams;
import com.yitkeji.channel.model.YunshanfuRes;
import com.yitkeji.channel.util.SnowflakeIdWorker;
import com.yitkeji.channel.util.StringUtil;
import com.yitkeji.songshushenghuo.service.YunshanfuAreaService;
import com.yitkeji.songshushenghuo.service.YunshanfuBindService;
import com.yitkeji.songshushenghuo.service.YunshanfuRegistedService;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.YunshanfuCfg;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.*;
import com.yitkeji.yunshanfu.Yunshanfu;
import com.yitkeji.yunshanfu.model.*;

import java.util.concurrent.TimeUnit;

public class YunshanfuChannel extends BaseChannel<YunshanfuCfg> implements RepaymentChannel<YunshanfuCfg> {

    private Yunshanfu yunshanfu;
    private YunshanfuRegistedService yunshanfuRegistedService;
    private YunshanfuBindService yunshanfuBindService;
    private YunshanfuAreaService yunshanfuAreaService;
    private String notifyUrl;

    private YunshanfuChannel(){
        super(Channel.YUNSHANFU);
        yunshanfuRegistedService = SpringUtil.getBean(YunshanfuRegistedService.class);
        yunshanfuBindService = SpringUtil.getBean(YunshanfuBindService.class);
        yunshanfuAreaService = SpringUtil.getBean(YunshanfuAreaService.class);
        this.initCfg();
    }

    private static final class ChannelHolder{
        private static final YunshanfuChannel CHANNEL = new YunshanfuChannel();
    }

    public static final YunshanfuChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg();
        return ChannelHolder.CHANNEL;
    }


    private void initCfg(){
        if(needReloadCfg(SystemCfg.getInstance().getChannel().getYunshanfu(), cfg)){
            cfg = SystemCfg.getInstance().getChannel().getYunshanfu();
            yunshanfu = new Yunshanfu(cfg.getBaseurl(), cfg.getMerchantNo(), cfg.getKey());
            yunshanfu.setLogger(channelLogger);
            notifyUrl = "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + cfg.getNotifyUrl();
        }
    }


    private YunshanfuRes parseRes(String result){
        YunshanfuRes yunshanfuRes = new YunshanfuRes();
        try {
            yunshanfuRes = JSON.parseObject(result, YunshanfuRes.class);
        }catch (Exception e){
            e.printStackTrace();
            yunshanfuRes.setRetmsg("接口响应异常");
        }finally {
            if(yunshanfuRes.getErrmsg() == null && yunshanfuRes.getRetmsg() != null){
                yunshanfuRes.setErrmsg(yunshanfuRes.getRetmsg());
            }
        }
        return yunshanfuRes;
    }

    /**
     * 进件
     * @param debitCard 结算卡
     * @return 成功时返回YunshanfuRegisted对象，失败时返回String对象（失败原因的描述）
     */
    private Object registMerchant(Card debitCard, double debitRate, double creditRate, double withdrawDepositRate, double withdrawDepositSingleFee, YunshanfuArea area){

        Register register = new Register();
        register.setOutcusid(SnowflakeIdWorker.getId(0,0));
        register.setCusname(debitCard.getName() + StringUtil.randomNum(12 - debitCard.getName().length()));
        register.setCusshortname(debitCard.getName());
        register.setMerprovice(area.getProvinceCode() + "");
        register.setAreacode(area.getAreaCode() + "");
        register.setLegal(debitCard.getName());
        register.setIdno(debitCard.getIdcard());
        register.setPhone(debitCard.getPhone());
        register.setAddress(area.getAreaName());
        register.setAcctid(debitCard.getCardNo());
        register.setAcctname(debitCard.getName());
        register.setSettfee(withdrawDepositSingleFee / 100 + "");
        register.setProdlist(debitRate * 100 + "");

        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.regist(register));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode())){
            return yunshanfuRes.getErrmsg();
        }
        Object result = queryRegist(yunshanfuRes.getOutcusid());
        if(result instanceof String || result == null){
            result = result == null ? "查询进件结果异常": "";
            return result;
        }
        YunshanfuRegisted yunshanfuRegisted = new YunshanfuRegisted();
        yunshanfuRegisted.setUserId(debitCard.getUserId());
        yunshanfuRegisted.setName(debitCard.getName());
        yunshanfuRegisted.setIdcard(debitCard.getIdcard());
        yunshanfuRegisted.setDebitCardId(debitCard.getCardId());
        yunshanfuRegisted.setCusid(yunshanfuRes.getCusid());
        yunshanfuRegisted.setOutcusid(yunshanfuRes.getOutcusid());
        yunshanfuRegisted.setDebitRate(debitRate);
        yunshanfuRegisted.setCreditRate(creditRate);
        yunshanfuRegisted.setWithdrawDepositRate(withdrawDepositRate);
        yunshanfuRegisted.setWithdrawDepositSingleFee(withdrawDepositSingleFee);
        yunshanfuRegistedService.add(yunshanfuRegisted);
        return yunshanfuRegisted;
    }


    private Object queryRegist(String outcusid){
        QueryRegister queryRegister = new QueryRegister();
        queryRegister.setOutcusid(outcusid);
        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.queryRegist(queryRegister));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode()) || !"1".equals(yunshanfuRes.getState())){
            return yunshanfuRes.getErrmsg();
        }

        // 轮询查询，并返回注册信息
        return yunshanfuRes;
    }

    private Object modifyFeeInfo(YunshanfuRegisted yunshanfuRegisted, Card card, double debitRate, double withdrawDepositSingleFee){

        EditFeeRate editFeeRate = new EditFeeRate();
        editFeeRate.setCusid(yunshanfuRegisted.getCusid());
        editFeeRate.setAcctid(card.getCardNo());
        editFeeRate.setProdlist(debitRate * 100 + "");
        editFeeRate.setSettfee(withdrawDepositSingleFee / 100 + "");

        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.editFeeRate(editFeeRate));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode())){
            return yunshanfuRes.getErrmsg();
        }else{
            yunshanfuRegisted.setDebitCardId(card.getCardId());
            yunshanfuRegisted.setDebitRate(debitRate);
            yunshanfuRegisted.setCreditRate(debitRate);
            yunshanfuRegisted.setWithdrawDepositRate(0.00);
            yunshanfuRegisted.setWithdrawDepositSingleFee(withdrawDepositSingleFee);
            yunshanfuRegistedService.updateByPrimaryKey(yunshanfuRegisted, "debitCardId", "debitRate", "creditRate", "withdrawDepositRate", "withdrawDepositSingleFee");
            return yunshanfuRegisted;
        }
    }

    /**
     * 绑卡
     * @param yunshanfuRegisted
     * @param card
     * @return
     */
    private Object bindCard(YunshanfuRegisted yunshanfuRegisted, Card card){

        BindCard bindCard = new BindCard();
        bindCard.setCusid(yunshanfuRegisted.getCusid());
        bindCard.setMeruserid(SnowflakeIdWorker.getId(2,2));
        bindCard.setCardno(card.getCardNo());
        bindCard.setAcctname(card.getName());
        bindCard.setValiddate(card.getExpiryDate());
        bindCard.setCvv2(card.getCvv());
        bindCard.setIdno(card.getIdcard());
        bindCard.setTel(card.getPhone());

        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.bindCard(bindCard));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode())){
            return yunshanfuRes.getErrmsg();
        }

        YunshanfuBind yunshanfuBind = new YunshanfuBind();
        yunshanfuBind.setCardId(card.getCardId());
        yunshanfuBind.setCusid(yunshanfuRegisted.getCusid());
        yunshanfuBind.setRequestNo(bindCard.getMeruserid());
        RedisUtil.set(CacheKey.YUNSHANFU.getKey(yunshanfuBind.getRequestNo()), JSON.toJSONString(yunshanfuBind), 1, TimeUnit.HOURS);
        return yunshanfuBind;
    }


    private Object confirmBindCard(YunshanfuRegisted yunshanfuRegisted, Card card, String bindRequestNo, String code){

        ConfirmBindCard confirmBindCard = new ConfirmBindCard();
        confirmBindCard.setCusid(yunshanfuRegisted.getCusid());
        confirmBindCard.setMeruserid(SnowflakeIdWorker.getId(2,2));
        confirmBindCard.setCardno(card.getCardNo());
        confirmBindCard.setAcctname(card.getName());
        confirmBindCard.setValiddate(card.getExpiryDate());
        confirmBindCard.setCvv2(card.getCvv());
        confirmBindCard.setIdno(card.getIdcard());
        confirmBindCard.setTel(card.getPhone());
        confirmBindCard.setSmscode(code);

        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.confirmBindCard(confirmBindCard));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode()) || !"0000".equals(yunshanfuRes.getTrxstatus())){
            return yunshanfuRes.getErrmsg();
        }
        Object object = RedisUtil.get(CacheKey.YUNSHANFU.getKey(bindRequestNo));
        if(object == null){
            return "验证码失效";
        }
        YunshanfuBind yunshanfuBind = JSON.parseObject((String)object, YunshanfuBind.class);
        yunshanfuBind.setAgreeid(yunshanfuRes.getAgreeid());
        yunshanfuBindService.add(yunshanfuBind);
        return yunshanfuBind;
    }

    /**
     * 支付
     * @param yunshanfuRegisted
     * @param order
     * @return
     */
    private Object smallPay(YunshanfuRegisted yunshanfuRegisted, YunshanfuBind yunshanfuBind, Order order, YunshanfuArea area){

        SmallPay smallPay = new SmallPay();
        smallPay.setCusid(yunshanfuRegisted.getCusid());
        smallPay.setOrderid(order.getOrderNo());
        smallPay.setAgreeid(yunshanfuBind.getAgreeid());
        smallPay.setAmount(order.getMoney() + "");
        smallPay.setCurrency("CNY");
        smallPay.setSubject(order.getDesc());
        smallPay.setValidtime("10");
        smallPay.setCity(area.getAreaCode() + "");
        smallPay.setTrxreserve(order.getDesc());
        smallPay.setNotifyurl(notifyUrl);

        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.smallPay(smallPay));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode())){
            return yunshanfuRes.getErrmsg();
        }
        return yunshanfuRes;
    }


    /**
     * 查询订单
     * @param yunshanfuRegisted
     * @param order
     * @param tried
     * @return
     */
    private Object queryPay(YunshanfuRegisted yunshanfuRegisted, Order order, Integer tried){

        QueryPay queryPay = new QueryPay();
        queryPay.setCusid(yunshanfuRegisted.getCusid());
        queryPay.setTrxid(order.getTrackingNo());

        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.queryPay(queryPay));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode())){
            return yunshanfuRes.getRetmsg();
        }else{
            if("0000".equals(yunshanfuRes.getTrxstatus())){
                return yunshanfuRes;
            }
            if("2000,0003,3054,".indexOf(yunshanfuRes.getTrxstatus() + ",") > -1){
                try {
                    if(tried > MAX_RETRIE_TIME){
                        return "查询订单超时";
                    }
                    Thread.sleep(Double.valueOf(Math.pow(RETRIE_POW, tried)).longValue() * 1000);
                    return queryPay(yunshanfuRegisted, order, tried + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "查询订单异常";
                }
            }
            return yunshanfuRes.getErrmsg();
        }
    }

    private Object queryWallet(YunshanfuRegisted yunshanfuRegisted, Integer tried){
        QueryWallet queryWallet = new QueryWallet();
        queryWallet.setCusid(yunshanfuRegisted.getCusid());
        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.queryWallet(queryWallet));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode())){
            return yunshanfuRes.getErrmsg();
        }
        int walletMoney = Integer.parseInt(yunshanfuRes.getBalance());
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
            return queryWallet(yunshanfuRegisted, tried + 1);
        }
        return walletMoney;
    }

    private Object withdrawDeposit(YunshanfuRegisted yunshanfuRegisted, YunshanfuBind yunshanfuBind, Order order){
        WithdrawPay withdrawPay = new WithdrawPay();
        withdrawPay.setCusid(yunshanfuRegisted.getCusid());
        withdrawPay.setIsall("2");
        withdrawPay.setAmount(order.getMoney() - order.getRateMoney() + "");
        withdrawPay.setOrderid(SnowflakeIdWorker.getId(5,1));
        withdrawPay.setAgreeid(yunshanfuBind.getAgreeid());
        withdrawPay.setTrxreserve(order.getDesc());
        withdrawPay.setNotifyurl(notifyUrl);
        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.withdrawPay(withdrawPay));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode()) || !"0000".equals(yunshanfuRes.getTrxstatus())){
            return yunshanfuRes.getErrmsg();
        }
        return yunshanfuRes;
    }

    private Object queryWithdraw(YunshanfuRegisted yunshanfuRegisted, String trackingNo){
        QueryWithdraw queryWithdraw = new QueryWithdraw();
        queryWithdraw.setCusid(yunshanfuRegisted.getCusid());
        queryWithdraw.setTrxid(trackingNo);
        YunshanfuRes yunshanfuRes = parseRes(yunshanfu.queryWithdraw(queryWithdraw));
        if(!"SUCCESS".equals(yunshanfuRes.getRetcode())){
            return yunshanfuRes.getErrmsg();
        }
        return yunshanfuRes;
    }

    /**
     * 还款，进件绑卡
     * @param params
     * @return
     */
    @Override
    public Object repaymentFirst(ChannelParams params){
        Card card = params.getCard();
        UserRate userRate = userService.computeUserRate(params.getUser());
        YunshanfuArea area = yunshanfuAreaService.findByAreaCode(params.getAreaCode());
        if(area == null){
            area = yunshanfuAreaService.findByUser(params.getUser());
        }
        if(area == null){
            area = yunshanfuAreaService.findByPrimaryKey(180L);
        }

        Object result;
        // 进件
        YunshanfuRegisted registed = yunshanfuRegistedService.findByIdcard(card.getIdcard());
        if(null == registed){
            result = this.registMerchant(params.getDebitCard(), userRate.getYunshanfuRate(), userRate.getYunshanfuRate(), 0, userRate.getYunshanfuFixedAmount(), area);
            if(result instanceof String){
                return result;
            }else if(result instanceof YunshanfuRegisted){
                registed = (YunshanfuRegisted)result;
            }else{
                return "进件失败";
            }
        }

        // 绑卡
        YunshanfuBind yunshanfuBind = yunshanfuBindService.findByCardId(card.getCardId());
        if(null == yunshanfuBind){
            if(null != params.getRequestNo() && null != params.getCode()){
                result = this.confirmBindCard(registed, card, params.getRequestNo(), params.getCode());
            }else{
                result = this.bindCard(registed, card);
            }
            if(result instanceof String){
                return result;
            }else if(result instanceof YunshanfuBind){
                return result;
            }else{
                return "绑卡失败";
            }
        }
        return yunshanfuBind;

    }
    /**
     * 还款，封装了代还流程
     * @param params
     * @return 返回订单
     */
    @Override
    public Order repayment(ChannelParams params){
        return null;
    }


    /**
     * 查询钱包
     * @param yunshanfuRegisted
     * @return
     */
    public String queryWallet(YunshanfuRegisted yunshanfuRegisted){
        QueryWallet queryWallet = new QueryWallet();
        queryWallet.setCusid(yunshanfuRegisted.getCusid());
        return yunshanfu.queryWallet(queryWallet);
    }


    /**
     * 发起结算
     * @param order
     * @return
     */
    public Order settlement(Order order){
        Card card = cardService.findByPrimaryKey(order.getTargetCardId());
        YunshanfuRegisted yunshanfuRegisted = yunshanfuRegistedService.findByIdcard(card.getIdcard());
        YunshanfuBind yunshanfuBind = yunshanfuBindService.findByCardId(card.getCardId());

        Object result = this.queryPay(yunshanfuRegisted, order, 1);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }

        result = this.withdrawDeposit(yunshanfuRegisted, yunshanfuBind, order);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }

        result = this.queryWithdraw(yunshanfuRegisted, order.getTrackingNo());
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }
        Double withdrawRateMoney = yunshanfuRegisted.getWithdrawDepositSingleFee();
        order.setRateMoney(order.getRateMoney() + withdrawRateMoney.intValue());
        order.setArriveMoney(order.getMoney() - order.getRateMoney());
        return orderService.successOrder(order);
    }

    public static void main(String[] args) {

    }

}
