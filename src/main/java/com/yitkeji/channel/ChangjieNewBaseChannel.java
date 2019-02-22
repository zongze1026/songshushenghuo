package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.yitkeji.changjienew.ChangjieNew;
import com.yitkeji.changjienew.model.*;
import com.yitkeji.channel.inter.CashChannel;
import com.yitkeji.channel.inter.RepaymentChannel;
import com.yitkeji.channel.model.ChangjieRes;
import com.yitkeji.channel.model.ChannelParams;
import com.yitkeji.channel.util.SnowflakeIdWorker;
import com.yitkeji.channel.util.StringUtil;
import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.service.ChangjieNewBindService;
import com.yitkeji.songshushenghuo.service.ChangjieNewRegistedService;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Bank;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Banks;
import com.yitkeji.songshushenghuo.vo.cfg.channel.ChangjieNewCfg;
import com.yitkeji.songshushenghuo.vo.cfg.vip.RateElement;
import com.yitkeji.songshushenghuo.vo.enums.*;
import com.yitkeji.songshushenghuo.vo.model.*;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ChangjieNewBaseChannel extends BaseChannel<ChangjieNewCfg> implements CashChannel<ChangjieNewCfg>, RepaymentChannel<ChangjieNewCfg> {

    private ChangjieNew changjieNew;
    private ChangjieNewRegistedService changjieNewRegistedService;
    private ChangjieNewBindService changjieNewBindService;
    private String notifyUrl;

    protected ChangjieNewBaseChannel(Channel channel, ChangjieNewCfg changjieNewCfg){
        super(channel);
        changjieNewRegistedService = SpringUtil.getBean(ChangjieNewRegistedService.class);
        changjieNewBindService = SpringUtil.getBean(ChangjieNewBindService.class);
        this.initCfg(changjieNewCfg);
    }

    protected void initCfg(ChangjieNewCfg changjieNewCfg){
        if(needReloadCfg(changjieNewCfg, cfg)){
            cfg = changjieNewCfg;
            changjieNew = new ChangjieNew(changjieNewCfg.getBaseurl(), changjieNewCfg.getMerchantNo(), changjieNewCfg.getKey());
            changjieNew.setLogger(channelLogger);
            notifyUrl = "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + changjieNewCfg.getNotifyUrl();
        }
    }

    private ChangjieRes parseRes(String result){
        ChangjieRes changjieRes = new ChangjieRes();
        try {
            changjieRes = JSON.parseObject(result, ChangjieRes.class);
        }catch (Exception e){
            e.printStackTrace();
            changjieRes.setCode("");
            changjieRes.setMessage("接口响应异常");
        }
        return changjieRes;
    }

    /**
     * 进件
     * @param debitCard 结算卡
     * @return 成功时返回ChangjieNewRegisted对象，失败时返回String对象（失败原因的描述）
     */
    private Object registMerchant(Card debitCard,  RateElement rate){

        Bank bank = debitCard.matchBank();

        RegistMerchant registMerchant = new RegistMerchant();
        registMerchant.setReqFlowNo(SnowflakeIdWorker.getId(0, 0));
        registMerchant.setMerName(debitCard.getName() + StringUtil.randomNum(12 - debitCard.getName().length()));
        registMerchant.setMerAbbr(debitCard.getName());
        registMerchant.setIdCardNo(debitCard.getIdcard());
        registMerchant.setBankAccountNo(debitCard.getCardNo());
        registMerchant.setMobile(debitCard.getPhone());
        registMerchant.setBankAccountName(debitCard.getName());
        registMerchant.setBankName(bank.getBankName());
        registMerchant.setBankSubName(bank.getBankName());
        registMerchant.setBankCode(bank.getId());
        registMerchant.setBankAbbr(bank.getCode());
        registMerchant.setBankChannelNo(bank.getUnion());
        registMerchant.setBankProvince("浙江省");
        registMerchant.setBankCity("海宁市");

        registMerchant.setDebitRate(rate.getRate() + "");
        registMerchant.setDebitCapAmount("99999999");
        registMerchant.setCreditRate(rate.getRate() + "");
        registMerchant.setCreditCapAmount("99999999");
        registMerchant.setWithdrawDepositRate("0");
        registMerchant.setWithdrawDepositSingleFee(Double.valueOf(rate.getFixedAmount()).intValue() + "");

        ChangjieRes changjieRes = parseRes(changjieNew.regist(registMerchant));
        if(!changjieRes.isSuccess()){
            return changjieRes.getErrorMsg();
        }
        ChangjieNewRegisted changjieNewRegisted = new ChangjieNewRegisted();
        changjieNewRegisted.setChannel(getChannel().name());
        changjieNewRegisted.setUserId(debitCard.getUserId());
        changjieNewRegisted.setName(debitCard.getName());
        changjieNewRegisted.setIdcard(debitCard.getIdcard());
        changjieNewRegisted.setDebitCardId(debitCard.getCardId());

        changjieNewRegisted.setMerchantNo(changjieRes.getData().getMerchantCode());
        changjieNewRegisted.setDebitRate(rate.getRate());
        changjieNewRegisted.setCreditRate(rate.getRate());
        changjieNewRegisted.setWithdrawDepositRate(0);
        changjieNewRegisted.setWithdrawDepositSingleFee(rate.getFixedAmount());
        changjieNewRegistedService.add(changjieNewRegisted);
        return changjieNewRegisted;
    }


    private Object registProduct(ChangjieNewRegisted changjieNewRegisted, double debitRate, double creditRate){
        RegisterProduct registerProduct = new RegisterProduct();
        registerProduct.setMerchantNo(changjieNewRegisted.getMerchantNo());
        registerProduct.setChannelCode(cfg.getChannelCode());
        registerProduct.setDebitRate(debitRate + "");
        registerProduct.setDebitCapAmount("99999999");
        registerProduct.setCreditRate(creditRate + "");
        registerProduct.setCreditCapAmount("99999999");
        ChangjieRes changjieRes = parseRes(changjieNew.registProduct(registerProduct));
        if(!changjieRes.isSuccess()){
            return changjieRes.getErrorMsg();
        }
        return changjieNewRegisted;
    }

    private Object editRate(ChangjieNewRegisted changjieNewRegisted, Double debitRate, Double creditRate){
        EditRate editRate = new EditRate();
        editRate.setMerchantNo(changjieNewRegisted.getMerchantNo());
        editRate.setDebitCapAmount("99999999");
        editRate.setDebitRate(debitRate + "");
        editRate.setCreditRate(creditRate + "");
        editRate.setCreditCapAmount("99999999");
        ChangjieRes changjieRes = parseRes(changjieNew.editRate(editRate));
        if(!changjieRes.isSuccess()){
            return changjieRes.getErrorMsg();
        }
        changjieNewRegisted.setDebitRate(debitRate);
        changjieNewRegisted.setCreditRate(creditRate);
        changjieNewRegistedService.updateByPrimaryKey(changjieNewRegisted, "debitRate", "creditRate");
        return changjieNewRegisted;
    }

    private Object editWithdrawRate(ChangjieNewRegisted changjieNewRegisted, Long fixedAmount){
        EditWithdrawRate editWithdrawRate = new EditWithdrawRate();
        editWithdrawRate.setMerchantNo(changjieNewRegisted.getMerchantNo());
        editWithdrawRate.setWithdrawDepositRate("0");
        editWithdrawRate.setWithdrawDepositSingleFee(fixedAmount.intValue() + "");
        ChangjieRes changjieRes = parseRes(changjieNew.editWithdrawRate(editWithdrawRate));
        if(!changjieRes.isSuccess()){
            return changjieRes.getErrorMsg();
        }
        changjieNewRegisted.setWithdrawDepositRate(0);
        changjieNewRegisted.setWithdrawDepositSingleFee(fixedAmount);
        changjieNewRegistedService.updateByPrimaryKey(changjieNewRegisted, "withdrawDepositRate", "withdrawDepositSingleFee");
        return changjieNewRegisted;
    }

    /**
     * 绑卡
     * @param changjieNewRegisted
     * @param card
     * @return
     */
    private Object bindCard(ChangjieNewRegisted changjieNewRegisted, Card card){
        BindCard bindCard = new BindCard();
        bindCard.setOrderNo(SnowflakeIdWorker.getId(0, 0));
        bindCard.setMerchantNo(changjieNewRegisted.getMerchantNo());
        bindCard.setBankAccountName(card.getName());
        bindCard.setBankAccountNo(card.getCardNo());
        bindCard.setIdCardNo(card.getIdcard());
        bindCard.setMobile(card.getPhone());
        ChangjieRes changjieRes = parseRes(changjieNew.bindCard(bindCard));
        if(!changjieRes.isActionSuccess()){
            return changjieRes.getErrorMsg();
        }
        ChangjieNewBind changjieNewBind = new ChangjieNewBind();
        changjieNewBind.setCardId(card.getCardId());
        changjieNewBind.setMerchantNo(changjieNewRegisted.getMerchantNo());
        changjieNewBind.setRequestNo(bindCard.getOrderNo());
        changjieNewBind.setChannel(getChannel().name());
        RedisUtil.set(CacheKey.CHANGJIE.getKey(changjieNewBind.getRequestNo()), JSON.toJSONString(changjieNewBind), 1, TimeUnit.HOURS);
        return changjieNewBind;
    }


    private Object confirmBindCard(ChangjieNewRegisted changjieNewRegisted, Card card, String orderNo, String code){

        ConfirmBindCard confirmBindCard = new ConfirmBindCard();
        confirmBindCard.setOrderNo(orderNo);
        confirmBindCard.setMerchantNo(changjieNewRegisted.getMerchantNo());
        confirmBindCard.setBankAccountName(card.getName());
        confirmBindCard.setBankAccountNo(card.getCardNo());
        confirmBindCard.setIdCardNo(card.getIdcard());
        confirmBindCard.setMobile(card.getPhone());
        confirmBindCard.setCvn2(card.getCvv());
        confirmBindCard.setExpired(card.getExpiryDate().replaceAll("(\\d{2})(\\d{2})", "$2$1"));
        confirmBindCard.setSmsCode(code);
        ChangjieRes changjieRes = parseRes(changjieNew.confirmBindCard(confirmBindCard));
        if(!changjieRes.isActionSuccess()){
            return changjieRes.getErrorMsg();
        }
        Object cacheBindInfo = RedisUtil.get(CacheKey.CHANGJIE.getKey(orderNo));
        if(cacheBindInfo == null){
            return "验证码已失效，请重新获取";
        }
        ChangjieNewBind changjieNewBind = JSON.parseObject((String)cacheBindInfo, ChangjieNewBind.class);
        Object result = queryBindCard(changjieNewRegisted, changjieNewBind.getRequestNo(), 1);
        if(result instanceof String){
            return result;
        }
        changjieNewBindService.add(changjieNewBind);
        return changjieNewBind;
    }

    /**
     * 查询绑卡状态
     * @param changjieNewRegisted
     * @param orderNo
     * @param tried
     * @return
     */
    private Object queryBindCard(ChangjieNewRegisted changjieNewRegisted, String orderNo, Integer tried){

        QueryBindCard queryBindCard = new QueryBindCard();
        queryBindCard.setOrderNo(orderNo);
        queryBindCard.setMerchantNo(changjieNewRegisted.getMerchantNo());

        ChangjieRes changjieRes = parseRes(changjieNew.queryBindCard(queryBindCard));
        if(!changjieRes.isActionSuccess()){
            return changjieRes.getErrorMsg();
        }
        switch (changjieRes.getData().getSignStatus()){
            case 2: return changjieRes;
            case 3: return changjieRes.getData().getRespMsg();
            case 4: return changjieRes.getData().getRespMsg();
            default: try {
                if(tried > 10){
                    return "查询绑卡状态超时";
                }
                Thread.sleep(5000 * tried);
                return queryBindCard(changjieNewRegisted, orderNo, tried + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "查询绑卡状态异常";
            }
        }
    }

    /**
     * 支付
     * @param changjieNewRegisted
     * @param params
     * @param order
     * @return
     */
    private Object pay(ChangjieNewRegisted changjieNewRegisted, ChannelParams params, Order order){
        Card card = params.getCard();

        Pay pay = new Pay();
        pay.setOrderNo(order.getTrackingNo());
        pay.setMerchantNo(changjieNewRegisted.getMerchantNo());
        pay.setOrderAmount(order.getMoney() + "");
        pay.setBankAccountName(card.getName());
        pay.setBankAccountNo(card.getCardNo());
        pay.setIdCardNo(card.getIdcard());
        pay.setMobile(card.getPhone());
        pay.setCvn2(card.getCvv());
        pay.setExpired(card.getExpiryDate().replaceAll("(\\d{2})(\\d{2})", "$2$1"));
        pay.setProductName(order.getDesc());
        pay.setProductDesc(order.getDesc());
        pay.setTrxCtNm(params.getCity());
        pay.setTrxSourceIp(params.getIp());
        pay.setServerCallbackUrl(notifyUrl);
        ChangjieRes changjieRes = parseRes(changjieNew.pay(pay));
        if(!changjieRes.isActionSuccess()){
            // 通道不支持城市时强制切换到杭州市
            if(!params.isDefault() && "-400003".equals(changjieRes.getCode())){
                params.resetPosition();
                order.setTrackingNo(SnowflakeIdWorker.getId(Double.valueOf(Math.random() * 10).intValue(), Double.valueOf(Math.random() * 10).intValue()));
                return pay(changjieNewRegisted, params, order);
            }
            return changjieRes.getErrorMsg();
        }
        return changjieRes;
    }

    /**
     * 查询订单
     * @param changjieNewRegisted
     * @param order
     * @param tried
     * @return
     */
    private Object queryPay(ChangjieNewRegisted changjieNewRegisted, Order order, Integer tried){

        QueryPay queryPay = new QueryPay();
        queryPay.setMerchantNo(changjieNewRegisted.getMerchantNo());
        queryPay.setOrderNo(order.getTrackingNo());

        ChangjieRes changjieRes = parseRes(changjieNew.queryPay(queryPay));
        if(!changjieRes.isActionSuccess()){
            return changjieRes.getErrorMsg();
        }
        switch (changjieRes.getData().getOrderStatus()){
            case 2: return changjieRes;
            case 1: try {
                        if(tried > MAX_RETRIE_TIME){
                            return "查询订单超时";
                        }
                        Thread.sleep(Double.valueOf(Math.pow(RETRIE_POW, tried)).longValue() * 1000);
                        return queryPay(changjieNewRegisted, order, tried + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return "查询订单异常";
                    }
            default: return changjieRes.getErrorMsg();
        }
    }

    private Object queryMerchantWallet(ChangjieNewRegisted changjieNewRegisted, Order order, Integer tried){
        QueryWallet queryWallet = new QueryWallet();
        queryWallet.setMerchantNo(changjieNewRegisted.getMerchantNo());
        ChangjieRes changjieRes = parseRes(changjieNew.queryMerchantWallet(queryWallet));
        if(!changjieRes.isSuccess()){
            return changjieRes.getErrorMsg();
        }
        int walletMoney = changjieRes.getData().getQuickpayD0Balance();
        int computMoney = order.getMoney() - order.getRateMoney();
        if(walletMoney < computMoney){
            if(tried > 10){
                return "查询钱包余额超时";
            }
            try {
                Thread.sleep(10000 * tried);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "查询钱包余额失败";
            }
            return queryMerchantWallet(changjieNewRegisted, order, tried + 1);
        }
        return walletMoney;
    }

    private Object withdrawDeposit(ChangjieNewRegisted changjieNewRegisted, Order order, Card card){
        Calendar orderTime = Calendar.getInstance();
        orderTime.setTime(order.getCreateTime());
        Calendar now = Calendar.getInstance();
        String walletType = "402";
        if(now.get(Calendar.YEAR) == orderTime.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) == orderTime.get(Calendar.DAY_OF_YEAR)){
            walletType = "400";
        }

        Bank bank = card.matchBank();
        WithdrawDeposit withdrawDeposit = new WithdrawDeposit();
        withdrawDeposit.setRequestNo(SnowflakeIdWorker.getId(0, 0));
        withdrawDeposit.setMerchantNo(changjieNewRegisted.getMerchantNo());
        withdrawDeposit.setAmount(order.getMoney() - order.getRateMoney() + "");
        withdrawDeposit.setBankAccountName(card.getName());
        withdrawDeposit.setBankAccountNo(card.getCardNo());
        withdrawDeposit.setWalletType(walletType);
        withdrawDeposit.setBankName(bank.getBankName());
        withdrawDeposit.setBankSubName(bank.getBankName());
        withdrawDeposit.setBankChannelNo(bank.getUnion());

        ChangjieRes changjieRes = parseRes(changjieNew.withdrawDeposit(withdrawDeposit));
        if(!changjieRes.isSuccess()){
            return changjieRes.getMessage();
        }
        order.setWithdrawTrackingNo(withdrawDeposit.getRequestNo());
        return changjieRes;
    }

    // 查询提现结果
    private Object queryWithdrawDeposit(ChangjieNewRegisted changjieNewRegisted, Order order, Integer tried){

        QueryWithdraw queryWithdraw = new QueryWithdraw();
        queryWithdraw.setRequestNo(order.getWithdrawTrackingNo());
        queryWithdraw.setMerchantNo(changjieNewRegisted.getMerchantNo());

        ChangjieRes changjieRes = parseRes(changjieNew.queryWithdraw(queryWithdraw));
        if(!changjieRes.isSuccess()){
            return changjieRes.getErrorMsg();
        }
        switch (changjieRes.getData().getRemitStatus()){
            case 1: return changjieRes;
            case 3: return changjieRes.getErrorMsg();
            default: try {
                if(tried > 10){
                    return "查询代付订单超时";
                }
                Thread.sleep(5000 * tried);
                return queryWithdrawDeposit(changjieNewRegisted, order, tried + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "查询代付订单异常";
            }
        }
    }

    /**
     * 还款，进件绑卡
     * @param params
     * @return
     */
    @Override
    public Object repaymentFirst(ChannelParams params){
        Vip vip = params.getUser().matchVip();
        RateElement rate = vip.getDefaultRate().getRepayment();
        if(getChannel().isSmall()){
            rate = vip.getYunshanfuRate().getRepayment();
        }
        Object result;
        // 进件
        ChangjieNewRegisted registed = changjieNewRegistedService.findByIdcard(params.getCard().getIdcard(), getChannel().name());
        if(null == registed){
            ChangjieNewRegisted changjieNewRegisted = changjieNewRegistedService.findByIdcard(params.getCard().getIdcard(), getGroupChannel(getChannel()));
            if(changjieNewRegisted != null){
                registed = new ChangjieNewRegisted();
                BeanUtils.copyProperties(changjieNewRegisted, registed);
                registed.setChannel(getChannel().name());
                registed.setRegistId(null);
                registed.setCreditRate(rate.getRate());
                registed.setDebitRate(rate.getRate());
                result = registProduct(registed, rate.getRate(), rate.getRate());
                if(result instanceof String){
                    return result;
                }
                changjieNewRegistedService.add(registed);
            }else{
                result = this.registMerchant(params.getDebitCard(), rate);
                if(result instanceof String){
                    return result;
                }else if(result instanceof ChangjieNewRegisted){
                    registed = (ChangjieNewRegisted)result;
                }else{
                    return "进件失败";
                }
            }
        }

        // 绑卡
        ChangjieNewBind changjieNewBind = changjieNewBindService.findByCardId(params.getCard().getCardId(), getChannel().name());
        if(null == changjieNewBind){
            if(null != params.getRequestNo() && null != params.getCode()){
                result = this.confirmBindCard(registed, params.getCard(), params.getRequestNo(), params.getCode());
            }else{
                result = this.bindCard(registed, params.getCard());
            }
            if(result instanceof String){
                return result;
            }else if(result instanceof ChangjieNewBind){
                return result;
            }else{
                return "绑卡失败";
            }
        }
        return changjieNewBind;

    }

    /**
     * 还款，封装了代还流程
     * @param params
     * @return 返回订单
     */
    @Override
    public Order repayment(ChannelParams params) throws BaseException {
        return null;
    }


    @Override
    public Object cashFirst(ChannelParams params){
        return repaymentFirst(params);
    }

    @Override
    public Object cash(ChannelParams params) throws BaseException {
        Vip vip = params.getUser().matchVip();
        RateElement rate = vip.getDefaultRate().getCash();

        Order order = orderService.createCashOrder(this.getChannel(), params.getUser(), params.getCard(), params.getDebitCard(), params.getMoney());

        if(cfg.getDisabled()){
            return orderService.failOrder(order, CLOSEDMSG);
        }

        if(cfg.getBusinessType() != null && !BusinessType.CASH.equals(this.cfg.getBusinessType())){
            return orderService.failOrder(order, NOSUPPORTED_BUSINESS);
        }
        Object result;

        // 拉取进件信息
        ChangjieNewRegisted registed = changjieNewRegistedService.findByIdcard(params.getCard().getIdcard(), getChannel().name());
        if(null == registed){
            return orderService.failOrder(order, "尚未进件");
        }

        //修正交易费率
        if(registed.getCreditRate() != rate.getRate() || registed.getDebitRate() != rate.getRate()){
            result = this.editRate(registed, rate.getRate(), rate.getRate());
            if(result instanceof String){
                return orderService.failOrder(order, (String)result);
            }
            registed = (ChangjieNewRegisted)result;
        }

        // 修正提现费率
        if(registed.getWithdrawDepositSingleFee() != rate.getFixedAmount()){
            result = this.editWithdrawRate(registed,  rate.getFixedAmount());
            if(result instanceof String){
                return orderService.failOrder(order, (String)result);
            }
            registed = (ChangjieNewRegisted)result;
        }

        // 修正结算卡信息

        // 支付
        order.setTrackingNo(SnowflakeIdWorker.getId(Double.valueOf(Math.random() * 10).intValue(), Double.valueOf(Math.random() * 10).intValue()));
        Double rateMoney = Math.ceil(order.getMoney() * registed.getDebitRate());
        order.setRateMoney(order.getRateMoney() + rateMoney.intValue());
        orderService.updateByPrimaryKey(order, "trackingNo", "rateMoney");
        result = this.pay(registed, params, order);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }

        ChangjieNewRegisted finalRegisted = registed;
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
                result = queryMerchantWallet(finalRegisted, order, 1);
                if(result instanceof String){
                    orderService.failOrder(order, (String)result);
                    return;
                }

                // 提现
                result = withdrawDeposit(finalRegisted, order, params.getDebitCard());
                if(result instanceof String){
                    orderService.failOrder(order, (String)result);
                    return;
                }

                // TODO查询提现结果

                Double withdrawRateMoney = finalRegisted.getWithdrawDepositSingleFee();
                order.setRateMoney(order.getRateMoney() + withdrawRateMoney.intValue());
                order.setArriveMoney(order.getMoney() - order.getRateMoney());
                orderService.successOrder(order);
            }
        }, 0);

        return order;
    }

    /**
     * 查询钱包
     * @param changjieNewRegisted
     * @return
     */
    public String queryWallet(ChangjieNewRegisted changjieNewRegisted){
        QueryWallet queryMerchantWallet = new QueryWallet();
        queryMerchantWallet.setMerchantNo(changjieNewRegisted.getMerchantNo());
        String result = changjieNew.queryMerchantWallet(queryMerchantWallet);
        return result;
    }

    public Object queryOrder(ChangjieNewRegisted changjieNewRegisted, Order order, Integer tried){
        Object result = this.queryPay(changjieNewRegisted, order, 1);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }
        order.setStatus(OrderStatus.SUCCESS.getCode());
        orderService.updateByPrimaryKey(order,"status");
        return order;
    }

    /**
     * 发起结算
     * @param order
     * @return
     */
    public Order settlement(Order order){
        Card card = cardService.findByPrimaryKey(order.getTargetCardId());
        ChangjieNewRegisted changjieNewRegisted = changjieNewRegistedService.findByIdcard(card.getIdcard(), getChannel().name());

        Object result = this.queryPay(changjieNewRegisted, order, 1);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }

        result = this.withdrawDeposit(changjieNewRegisted, order, card);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }

        result = this.queryWithdrawDeposit(changjieNewRegisted, order, 1);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }
        if (order.getStatus() == OrderStatus.SUCCESS.getCode()){
            return order;
        }
        Double withdrawRateMoney = changjieNewRegisted.getWithdrawDepositSingleFee();
        order.setRateMoney(order.getRateMoney() + withdrawRateMoney.intValue());
        order.setArriveMoney(order.getMoney() - order.getRateMoney());
        return orderService.successOrder(order);
    }

    private Channel[] getGroupChannel(Channel channel){
        List<Channel[]> groups = new ArrayList<>();
        groups.add(new Channel[]{Channel.CHANGJIEFJSL, Channel.CHANGJIEFJLD, Channel.CHANGJIEFJLD2, Channel.CHANGJIEFJSM, Channel.CHANGJIEFJSBANK});
        groups.add(new Channel[]{Channel.CHANGJIEJSSL, Channel.CHANGJIEJSLD, Channel.CHANGJIEJSLD2, Channel.CHANGJIEJSXDHSM});
        groups.add(new Channel[]{Channel.CHANGJIESL, Channel.CHANGJIELD, Channel.CHANGJIELD2});
        for(Channel[] group: groups){
            for(int i=0; i<group.length; i++){
                Channel channel1 = group[i];
                if(channel1.equals(channel)){
                    return group;
                }
            }
        }
        return new Channel[]{};
    }

    public static void main(String[] args) {

    }

}
