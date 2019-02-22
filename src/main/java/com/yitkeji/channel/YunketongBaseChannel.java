package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.yitkeji.channel.inter.CashChannel;
import com.yitkeji.channel.model.ChannelParams;
import com.yitkeji.channel.model.YunketongRes;
import com.yitkeji.channel.model.YunketongResultMap;
import com.yitkeji.channel.util.SnowflakeIdWorker;
import com.yitkeji.channel.util.StringUtil;
import com.yitkeji.songshushenghuo.service.YunketongBindService;
import com.yitkeji.songshushenghuo.service.YunketongRegistedService;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Bank;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Banks;
import com.yitkeji.songshushenghuo.vo.cfg.channel.YunketongCfg;
import com.yitkeji.songshushenghuo.vo.cfg.vip.RateElement;
import com.yitkeji.songshushenghuo.vo.enums.*;
import com.yitkeji.songshushenghuo.vo.model.*;
import com.yitkeji.yunketong.Yunketong;
import com.yitkeji.yunketong.model.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class YunketongBaseChannel extends BaseChannel<YunketongCfg> implements CashChannel<YunketongCfg> {

    private Yunketong yunketong;
    private YunketongRegistedService yunketongRegistedService;
    private YunketongBindService yunketongBindService;
    private String notifyUrl;

    protected YunketongBaseChannel(Channel channel, YunketongCfg yunketongCfg){
        super(channel);
        yunketongRegistedService = SpringUtil.getBean(YunketongRegistedService.class);
        yunketongBindService = SpringUtil.getBean(YunketongBindService.class);
        this.initCfg(yunketongCfg);
    }


    protected void initCfg(YunketongCfg yunketongCfg){
        if(needReloadCfg(yunketongCfg, cfg)){
            cfg = yunketongCfg;
            yunketong = new Yunketong(yunketongCfg.getBaseurl(), yunketongCfg.getMerchantNo(), yunketongCfg.getKey());
            yunketong.setLogger(channelLogger);
            notifyUrl = "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + yunketongCfg.getNotifyUrl();
        }
    }


    private YunketongResultMap parseRes(String result){
        YunketongResultMap resultMap = new YunketongResultMap();
        try {
            YunketongRes changjieRes = JSON.parseObject(result, YunketongRes.class);
            if(!"SUCCESS".equals(changjieRes.getState())){
                resultMap.setBizMsg(changjieRes.getMessage());
                return resultMap;
            }
            resultMap = changjieRes.getResultMap();
        }catch (Exception e){
            e.printStackTrace();
            resultMap.setBizMsg("接口响应异常");
        }
        return resultMap;
    }

    /**
     * 进件
     * @param debitCard 结算卡
     * @return 成功时返回YunketongRegisted对象，失败时返回String对象（失败原因的描述）
     */
    private Object registMerchant(Card debitCard, double debitRate, double creditRate, double withdrawDepositRate, double withdrawDepositSingleFee){

        Bank bank = debitCard.matchBank();
        if(StringUtils.isBlank(bank.getCode())){
            return "不支持该银行";
        }


        Register register = new Register();
        register.setRequestNo(SnowflakeIdWorker.getId(0, 0));
        register.setMerchantName(debitCard.getName() + StringUtil.randomNum(12 - debitCard.getName().length()));
        register.setShortName(debitCard.getName());
        register.setBindMobile(debitCard.getPhone());
        register.setBindEmail(debitCard.getEmail() == null ? "mail@yitkeji.com": debitCard.getEmail());
        register.setAddress("杭州市");
        register.setIdCardNo(debitCard.getIdcard());

        register.setSettleBankAccountNo(debitCard.getCardNo());
        register.setSettleBankAccountName(debitCard.getName());
        register.setSettleBankAccountType("PRIVATE");
        register.setSettleBankName(bank.getBankName());
        register.setSettleBankSubName(bank.getBankName());
        register.setSettleBankAbbr(bank.getCode());
        register.setSettleBankChannelNo(bank.getUnion());
        register.setSettleBankCardProvince("浙江省");
        register.setSettleBankCardCity("杭州市");

        register.setDebitRate(debitRate + "");
        register.setDebitCapAmount("999999");
        register.setCreditRate(creditRate + "");
        register.setCreditCapAmount("999999");
        register.setWithdrawDepositRate(withdrawDepositRate + "");
        register.setWithdrawDepositSingleFee(withdrawDepositSingleFee / 100 + "");

        YunketongResultMap changjieResultMap = parseRes(yunketong.regist(register));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }
        YunketongRegisted yunketongRegisted = new YunketongRegisted();
        yunketongRegisted.setChannel(getChannel().name());
        yunketongRegisted.setUserId(debitCard.getUserId());
        yunketongRegisted.setName(debitCard.getName());
        yunketongRegisted.setIdcard(debitCard.getIdcard());
        yunketongRegisted.setDebitCardId(debitCard.getCardId());

        yunketongRegisted.setMerchantNo(changjieResultMap.getMerchantNo());
        yunketongRegisted.setEncryptKey(changjieResultMap.getEncryptKey());
        yunketongRegisted.setDebitRate(debitRate);
        yunketongRegisted.setCreditRate(creditRate);
        yunketongRegisted.setWithdrawDepositRate(withdrawDepositRate);
        yunketongRegisted.setWithdrawDepositSingleFee(withdrawDepositSingleFee);
        yunketongRegistedService.add(yunketongRegisted);
        return yunketongRegisted;
    }



    private Object registProduct(YunketongRegisted yunketongRegisted, double debitRate, double creditRate){
        RegisterProduct registerProduct = new RegisterProduct();
        registerProduct.setChannelProductCode(cfg.getChannelCode());
        registerProduct.setMerchantNo(yunketongRegisted.getMerchantNo());
        registerProduct.setDebitRate(debitRate + "");
        registerProduct.setDebitCapAmount("999999");
        registerProduct.setCreditRate(creditRate + "");
        registerProduct.setCreditCapAmount("999999");

        YunketongResultMap changjieResultMap = parseRes(yunketong.registProduct(registerProduct));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }else{
            return yunketongRegisted;
        }
    }


    private Object modifySettlementInfo(YunketongRegisted yunketongRegisted, Card card){

        Bank bank = card.matchBank();
        ModifySettlementInfo modifySettlementInfo = new ModifySettlementInfo();
        modifySettlementInfo.setMerchantNo(yunketongRegisted.getMerchantNo());
        modifySettlementInfo.setBindMobile(card.getPhone());
        modifySettlementInfo.setSettleBankAccountNo(card.getCardNo());
        modifySettlementInfo.setSettleBankAccountName(card.getName());
        modifySettlementInfo.setSettleBankName(bank.getBankName());
        modifySettlementInfo.setSettleBankSubName(bank.getBankName());
        modifySettlementInfo.setSettleBankAbbr(bank.getCode());
        modifySettlementInfo.setSettleBankChannelNo(bank.getUnion());
        modifySettlementInfo.setSettleBankCardProvince("浙江省");
        modifySettlementInfo.setSettleBankCardCity("杭州市");

        YunketongResultMap changjieResultMap = parseRes(yunketong.modifySettlementInfo(modifySettlementInfo));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }else{
            yunketongRegisted.setDebitCardId(card.getCardId());
            yunketongRegistedService.updateByPrimaryKey(yunketongRegisted, "debitCardId");
            return yunketongRegisted;
        }
    }


    private Object modifyFeeInfo(YunketongRegisted yunketongRegisted, String bizType, double rateValue){
        String cardType = "TRADE".equals(bizType) ? "CREDIT": "DEBIT";
        String rate = "";
        switch (bizType){
            case "TRADE": rate = rateValue + ""; break;
            case "WITHDRAW_RATE": rate = "0"; break;
            case "WITHDRAW_SIGLE": rate = rateValue / 100 + ""; break;
            default:
        }
        ModifyFeeInfo modifyFeeInfo = new ModifyFeeInfo();
        modifyFeeInfo.setMerchantNo(yunketongRegisted.getMerchantNo());
        modifyFeeInfo.setCardType(cardType);
        modifyFeeInfo.setBizType(bizType);
        modifyFeeInfo.setFeeValue(rate);

        YunketongResultMap changjieResultMap = parseRes(yunketong.modifyFeeInfo(modifyFeeInfo));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }else{
            switch (bizType){
                case "TRADE":
                    yunketongRegisted.setDebitRate(rateValue);
                    yunketongRegisted.setCreditRate(rateValue);
                    break;
                case "WITHDRAW_SIGLE":
                    yunketongRegisted.setWithdrawDepositSingleFee(rateValue);
                    break;
                default:
            }
            yunketongRegistedService.updateByPrimaryKey(yunketongRegisted, "debitRate", "creditRate", "withdrawDepositSingleFee");
            return yunketongRegisted;
        }
    }

    /**
     * 绑卡
     * @param yunketongRegisted
     * @param card
     * @return
     */
    private Object bindCard(YunketongRegisted yunketongRegisted, Card card, Bank bank, int tried){
        BindCard bindCard = new BindCard();
        if(StringUtils.isBlank(bank.getCode())){
            return "不支持该银行";
        }

        bindCard.setMerchantNo(yunketongRegisted.getMerchantNo());
        bindCard.setRequestNo(SnowflakeIdWorker.getId(0, 0));
        bindCard.setBankCardNo(card.getCardNo());
        bindCard.setBankAccountName(card.getName());
        bindCard.setCardType("CREDIT");
        bindCard.setBankMobile(card.getPhone());
        bindCard.setCertType("PRC_ID");
        bindCard.setCertNo(card.getIdcard());
        bindCard.setCvn2(card.getCvv());
        bindCard.setExpired(card.getExpiryDate().replaceAll("(\\d{2})(\\d{2})", "$2$1"));
        bindCard.setBankAbbr(bank.getCode());

        YunketongResultMap changjieResultMap = parseRes(yunketong.bindCard(bindCard));
        if(!"1".equals(changjieResultMap.getBizCode())){
            if("PB010003".equals(changjieResultMap.getBizCode())){
                try {
                    Thread.sleep((long)Math.pow(2.0, (double)tried));
                    return bindCard(yunketongRegisted, card, bank, tried + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return changjieResultMap.getBizMsg();
        }else{
            YunketongBind yunketongBind = new YunketongBind();
            yunketongBind.setCardId(card.getCardId());
            yunketongBind.setMerchantNo(yunketongRegisted.getMerchantNo());
            yunketongBind.setRequestNo(bindCard.getRequestNo());
            return yunketongBind;
        }
    }


    private Object confirmBindCard(YunketongRegisted yunketongRegisted, Card card, String bindRequestNo, String code){

        ConfirmBindCard confirmBindCard = new ConfirmBindCard();
        confirmBindCard.setMerchantNo(yunketongRegisted.getMerchantNo());
        confirmBindCard.setSmsCode(code);
        confirmBindCard.setBindRequestNo(bindRequestNo);

        YunketongResultMap changjieResultMap = parseRes(yunketong.confirmBindCard(confirmBindCard));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }else{
            YunketongBind yunketongBind = new YunketongBind();
            yunketongBind.setChannel(getChannel().name());
            yunketongBind.setCardId(card.getCardId());
            yunketongBind.setMerchantNo(yunketongRegisted.getMerchantNo());
            yunketongBind.setRequestNo(bindRequestNo);
            Object result = queryBindCard(yunketongRegisted, yunketongBind.getRequestNo(), 1);
            if(result instanceof String){
                return result;
            }
            yunketongBindService.add(yunketongBind);
            return yunketongBind;
        }
    }

    /**
     * 查询绑卡状态
     * @param yunketongRegisted
     * @param bindRequestNo
     * @param tried
     * @return
     */
    private Object queryBindCard(YunketongRegisted yunketongRegisted, String bindRequestNo, Integer tried){

        QueryBindCard queryBindCard = new QueryBindCard();
        queryBindCard.setMerchantNo(yunketongRegisted.getMerchantNo());
        queryBindCard.setRequestNo(bindRequestNo);

        YunketongResultMap changjieResultMap = parseRes(yunketong.queryBindCard(queryBindCard));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }else{
            switch (changjieResultMap.getBindStatus()){
                case "SUCCESS": return changjieResultMap;
                case "FAIL": return changjieResultMap.getBizMsg();
                default: try {
                    if(tried > 10){
                        return "查询绑卡状态超时";
                    }
                    Thread.sleep(5000 * tried);
                    return queryBindCard(yunketongRegisted, bindRequestNo, tried + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "查询绑卡状态异常";
                }
            }
        }
    }

    /**
     * 支付
     * @param yunketongRegisted
     * @param card
     * @param order
     * @return
     */
    private Object pay(YunketongRegisted yunketongRegisted, Card card, Order order){

        Pay pay = new Pay();
        pay.setMerchantNo(yunketongRegisted.getMerchantNo());
        pay.setRequestNo(order.getTrackingNo());
        pay.setBankCardNo(card.getCardNo());
        pay.setCvn2(card.getCvv());
        pay.setExpired(card.getExpiryDate().replaceAll("(\\d{2})(\\d{2})", "$2$1"));
        pay.setAmount(new DecimalFormat("#0.00").format(Double.parseDouble(order.getMoney() + "")/100));
        pay.setProductName(order.getDesc());
        pay.setProductDesc(order.getDesc());
        pay.setServerCallbackUrl(notifyUrl);

        YunketongResultMap changjieResultMap = parseRes(yunketong.pay(pay));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }else{
            changjieResultMap.setRequestNo(pay.getRequestNo());
            switch (changjieResultMap.getStatus()){
                case "FAILURE": return changjieResultMap.getBizMsg();
                default: return changjieResultMap;
            }
        }
    }

    /**
     * 查询订单
     * @param yunketongRegisted
     * @param order
     * @param tried
     * @return
     */
    private Object queryPay(YunketongRegisted yunketongRegisted, Order order, Integer tried){

        QueryPay queryPay = new QueryPay();
        queryPay.setMerchantNo(yunketongRegisted.getMerchantNo());
        queryPay.setRequestNo(order.getTrackingNo());

        YunketongResultMap changjieResultMap = parseRes(yunketong.queryPay(queryPay));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }else{
            switch (changjieResultMap.getStatus()){
                case "SUCCESS": return changjieResultMap;
                case "FAILURE": return changjieResultMap.getBizMsg();
                default:
                    try {
                        if(tried > MAX_RETRIE_TIME){
                            return "查询订单超时";
                        }
                        Thread.sleep(Double.valueOf(Math.pow(RETRIE_POW, tried)).longValue() * 1000);
                        return queryPay(yunketongRegisted, order, tried + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return "查询订单异常";
                    }
            }
        }
    }

    private Object queryMerchantWallet(YunketongRegisted yunketongRegisted, Order order, Integer tried){
        QueryMerchantWallet queryMerchantWallet = new QueryMerchantWallet();
        queryMerchantWallet.setMerchantNo(yunketongRegisted.getMerchantNo());
        YunketongResultMap changjieResultMap = parseRes(yunketong.queryMerchantWallet(queryMerchantWallet));
        if(!"200".equals(changjieResultMap.getCode())){
            return "查询钱包余额失败";
        }
        int walletMoney = 0;
        try{
            walletMoney = Integer.parseInt(changjieResultMap.getAmountD0());
        }catch (Exception e){
            // 取余额时异常
        }
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
            return queryMerchantWallet(yunketongRegisted, order, tried + 1);
        }
        return walletMoney;
    }

    private Object withdrawDeposit(YunketongRegisted yunketongRegisted, Order order, Card card, Bank bank){
        Calendar orderTime = Calendar.getInstance();
        orderTime.setTime(order.getCreateTime());
        Calendar now = Calendar.getInstance();
        String walletType = "402";
        if(now.get(Calendar.YEAR) == orderTime.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) == orderTime.get(Calendar.DAY_OF_YEAR)){
            walletType = "400";
        }

        WithdrawDeposit withdrawDeposit = new WithdrawDeposit();
        withdrawDeposit.setMerchantNo(yunketongRegisted.getMerchantNo());
        withdrawDeposit.setRequestNo(UUID.randomUUID().toString().replaceAll("-", ""));
        withdrawDeposit.setAmount(new DecimalFormat("#.00").format(Double.valueOf(order.getMoney() - order.getRateMoney())/100));
        withdrawDeposit.setBankAccountNo(card.getCardNo());
        withdrawDeposit.setBankAccountName(card.getName());
        withdrawDeposit.setBankName(bank.getBankName());
        withdrawDeposit.setBankSubName(bank.getBankName());
        withdrawDeposit.setBankChannelNo(bank.getUnion());
        withdrawDeposit.setBankCode(bank.getId());
        withdrawDeposit.setBankAbbr(bank.getCode());
        withdrawDeposit.setBankProvince("浙江省");
        withdrawDeposit.setBankCity("杭州市");
        withdrawDeposit.setBankArea("滨江区");
        withdrawDeposit.setWalletType(walletType);
        withdrawDeposit.setBankAccountType("PRIVATE");
        YunketongResultMap changjieResultMap = parseRes(yunketong.withdrawDeposit(withdrawDeposit));
        if(!"1".equals(changjieResultMap.getBizCode())){
            return changjieResultMap.getBizMsg();
        }
        return null;
    }


    /**
     * 还款，进件绑卡
     * @return
     */
    public Object repaymentFirst(ChannelParams params){
        Card card = params.getCard();
        Vip vip = params.getUser().matchVip();
        RateElement rate = vip.getDefaultRate().getRepayment();

        Bank bank = card.matchBank();
        Object result;
        // 进件
        YunketongRegisted registed = yunketongRegistedService.findByIdcard(card.getIdcard(), getChannel().name());
        if(null == registed){
            YunketongRegisted yunketongRegisted = yunketongRegistedService.findByIdcard(card.getIdcard());
            if(yunketongRegisted != null){
                registed = new YunketongRegisted();
                BeanUtils.copyProperties(yunketongRegisted, registed);
                registed.setChannel(getChannel().name());
                registed.setRegistId(null);
                registed.setCreditRate(rate.getRate());
                registed.setDebitRate(rate.getRate());
                result = registProduct(registed, rate.getRate(), rate.getRate());
                if(result instanceof String){
                    return result;
                }
                yunketongRegistedService.add(registed);
            }else{
                result = this.registMerchant(params.getDebitCard(), rate.getRate(), rate.getRate(), 0, rate.getFixedAmount());
                if(result instanceof String){
                    return result;
                }else if(result instanceof YunketongRegisted){
                    registed = (YunketongRegisted)result;
                }else{
                    return "进件失败";
                }
            }
        }

        // 绑卡
        YunketongBind yunketongBind = yunketongBindService.findByCardId(card.getCardId(), getChannel().name());
        if(null == yunketongBind){
            if(null != params.getRequestNo() && null != params.getCode()){
                result = this.confirmBindCard(registed, card, params.getRequestNo(), params.getCode());
            }else{
                result = this.bindCard(registed, card, bank, 0);
            }
            if(result instanceof String){
                return result;
            }else if(result instanceof YunketongBind){
                return result;
            }else{
                return "绑卡失败";
            }
        }
        return yunketongBind;

    }

    /**
     * 还款，封装了代还流程
     * @param params
     * @return 返回订单
     */
    public Order repayment(ChannelParams params){
        return null;
    }


    /**
     * 收款，进件绑卡
     * @param params
     * @return
     */
    @Override
    public Object cashFirst(ChannelParams params){
        Card card = params.getCard();
        Vip vip = params.getUser().matchVip();
        RateElement rate = vip.getDefaultRate().getCash();

        Bank bank = card.matchBank();
        Object result;
        // 进件
        YunketongRegisted registed = yunketongRegistedService.findByIdcard(card.getIdcard(), getChannel().name());
        if(null == registed){
            YunketongRegisted yunketongRegisted = yunketongRegistedService.findByIdcard(card.getIdcard());
            if(yunketongRegisted != null){
                registed = new YunketongRegisted();
                BeanUtils.copyProperties(yunketongRegisted, registed);
                registed.setChannel(getChannel().name());
                registed.setRegistId(null);
                registed.setDebitRate(rate.getRate());
                registed.setCreditRate(rate.getRate());
                result = registProduct(registed, rate.getRate(), rate.getRate());
                if(result instanceof String){
                    return result;
                }
                yunketongRegistedService.add(registed);
            }else{
                result = this.registMerchant(params.getDebitCard(), rate.getRate(), rate.getRate(), 0, rate.getFixedAmount());
                if(result instanceof String){
                    return result;
                }else if(result instanceof YunketongRegisted){
                    registed = (YunketongRegisted)result;
                }else{
                    return "进件失败";
                }
            }

        }

        // 绑卡
        YunketongBind yunketongBind = yunketongBindService.findByCardId(card.getCardId(), getChannel().name());
        if(null == yunketongBind){
            if(null != params.getRequestNo() && null != params.getCode()){
                result = this.confirmBindCard(registed, card, params.getRequestNo(), params.getCode());
            }else{
                result = this.bindCard(registed, card, bank, 0);
            }
            if(result instanceof String){
                return result;
            }else if(result instanceof YunketongBind){
                return result;
            }else{
                return "绑卡失败";
            }
        }
        return yunketongBind;

    }

    /**
     * 收款
     * @param params
     * @return 返回订单
     */
    @Override
    public Object cash(ChannelParams params){
        Card card = params.getCard();
        Vip vip = params.getUser().matchVip();
        RateElement rate = vip.getDefaultRate().getCash();
        Order order = orderService.createCashOrder(this.getChannel(), params.getUser(), card, params.getDebitCard(), params.getMoney());

        if(cfg.getDisabled()){
            return orderService.failOrder(order, CLOSEDMSG);
        }

        if(cfg.getBusinessType() != null && !BusinessType.CASH.equals(this.cfg.getBusinessType())){
            return orderService.failOrder(order, NOSUPPORTED_BUSINESS);
        }

        Bank bank = card.matchBank();
        Object result;

        // 拉取进件信息
        YunketongRegisted registed = yunketongRegistedService.findByIdcard(card.getIdcard(), getChannel().name());
        if(null == registed){
            return orderService.failOrder(order, "尚未进件");
        }

        //修正交易费率
        if(registed.getCreditRate() != rate.getRate() || registed.getDebitRate() != rate.getRate()){
            result = this.modifyFeeInfo(registed, "TRADE", rate.getRate());
            if(result instanceof String){
                return orderService.failOrder(order, (String)result);
            }
            registed = (YunketongRegisted)result;
        }

        // 修正提现费率
        if(registed.getWithdrawDepositSingleFee() != rate.getFixedAmount()){
            result = this.modifyFeeInfo(registed, "WITHDRAW_SIGLE", rate.getFixedAmount());
            if(result instanceof String){
                return orderService.failOrder(order, (String)result);
            }
            registed = (YunketongRegisted)result;
        }

        // 修正结算卡信息
        if(!registed.getDebitCardId().equals(params.getDebitCard().getCardId())){
            result = this.modifySettlementInfo(registed, params.getDebitCard());
            if(result instanceof String){
                return orderService.failOrder(order, (String)result);
            }
            registed = (YunketongRegisted)result;
        }


        // 支付
        order.setTrackingNo(SnowflakeIdWorker.getId(0, 0));
        Double rateMoney = Math.ceil(order.getMoney() * registed.getDebitRate());
        order.setRateMoney(order.getRateMoney() + rateMoney.intValue());
        orderService.updateByPrimaryKey(order, "trackingNo", "rateMoney");
        result = this.pay(registed, card, order);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }else if (result instanceof YunketongResultMap){
            YunketongResultMap consumeResult = (YunketongResultMap)result;
            order.setTrackingNo(consumeResult.getRequestNo());
        }else{
            return orderService.failOrder(order, "支付失败");
        }


        // 异步方式进行后续操作，以尽快响应给客户端
        YunketongRegisted finalRegisted = registed;
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
                result = withdrawDeposit(finalRegisted, order, params.getDebitCard(), bank);
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


    /**
     * 查询钱包
     * @param yunketongRegisted
     * @return
     */
    public String queryWallet(YunketongRegisted yunketongRegisted){
        QueryMerchantWallet queryMerchantWallet = new QueryMerchantWallet();
        queryMerchantWallet.setMerchantNo(yunketongRegisted.getMerchantNo());
        String result = yunketong.queryMerchantWallet(queryMerchantWallet);
        return result;
    }

    public Object queryOrder(YunketongRegisted yunketongRegisted, Order order, Integer tried){
        Object result = this.queryPay(yunketongRegisted, order, 1);
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
        Bank bank = card.matchBank();
        YunketongRegisted yunketongRegisted = yunketongRegistedService.findByIdcard(card.getIdcard(), getChannel().name());

        Object result = this.queryPay(yunketongRegisted, order, 1);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }

        result = this.withdrawDeposit(yunketongRegisted, order, card, bank);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }
        if (order.getStatus() != OrderStatus.SUCCESS.getCode()){
            Double withdrawRateMoney = yunketongRegisted.getWithdrawDepositSingleFee();
            order.setRateMoney(order.getRateMoney() + withdrawRateMoney.intValue());
            order.setArriveMoney(order.getMoney() - order.getRateMoney());
            return orderService.successOrder(order);
        }
        return order;
    }

    public static void main(String[] args) {

    }

}
