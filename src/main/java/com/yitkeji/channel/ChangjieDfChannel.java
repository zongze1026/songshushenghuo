package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.yitkeji.changjiedf.ChangjieDf;
import com.yitkeji.changjiedf.model.Daifu;
import com.yitkeji.changjiedf.model.QueryDaifu;
import com.yitkeji.channel.inter.WithdrawChannel;
import com.yitkeji.channel.model.ChangjieDfRes;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Bank;
import com.yitkeji.songshushenghuo.vo.cfg.channel.ChangjieDfCfg;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.model.UserRate;

import java.util.Timer;
import java.util.TimerTask;

public class ChangjieDfChannel extends BaseChannel<ChangjieDfCfg> implements WithdrawChannel<ChangjieDfCfg> {

    private ChangjieDf changjieDf;
    private String notifyUrl;

    private ChangjieDfChannel(){
        super(Channel.CHANGJIEDF);
        this.initCfg();
    }

    private static final class ChannelHolder{
        private static final ChangjieDfChannel CHANNEL = new ChangjieDfChannel();
    }

    public static final ChangjieDfChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg();
        return ChannelHolder.CHANNEL;
    }

    protected void initCfg(){
        if(needReloadCfg(SystemCfg.getInstance().getChannel().getChangjiedf(), cfg)){
            cfg = SystemCfg.getInstance().getChannel().getChangjiedf();
            changjieDf = new ChangjieDf(cfg.getBaseurl(), cfg.getMerchantNo(), cfg.getKey());
            changjieDf.setLogger(channelLogger);
            notifyUrl = "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + cfg.getNotifyUrl();
        }
    }

    private ChangjieDfRes parseRes(String result){
        ChangjieDfRes changjieDfRes = new ChangjieDfRes();
        try {
            changjieDfRes = JSON.parseObject(result, ChangjieDfRes.class);
        }catch (Exception e){
            e.printStackTrace();
            changjieDfRes.setCode("");
            changjieDfRes.setMessage("接口响应异常");
        }
        return changjieDfRes;
    }

    /**
     * 代付
     * @param debitCard
     * @param order
     * @return
     */
    private Object daifu(Card debitCard, Order order){
        Bank bank = debitCard.matchBank();
        Daifu daifu = new Daifu();
        daifu.setRequestNo(order.getOrderNo());
        daifu.setAmount(Double.valueOf(order.getArriveMoney()) / 100 + "");
        daifu.setBankAccountName(debitCard.getName());
        daifu.setBankAccountNo(debitCard.getCardNo());
        daifu.setBankName(bank.getBankName());
        daifu.setBankSubName(bank.getBankName());
        daifu.setLiceneceNo(debitCard.getIdcard());
        daifu.setPhone(debitCard.getPhone());
        daifu.setPostScript(order.getDesc());
        ChangjieDfRes res = parseRes(changjieDf.daifu(daifu));
        if(!res.isSuccess()){
            return res.getErrorMsg();
        }
        return res;
    }


    private Object queryDaifu(String requestNo, Integer tried){

        QueryDaifu queryDaifu = new QueryDaifu();
        queryDaifu.setRequestNo(requestNo);
        ChangjieDfRes res = parseRes(changjieDf.queryDaifu(queryDaifu));
        if(!res.isSuccess()){
            return res.getErrorMsg();
        }
        switch (res.getResultMap().getStatus()){
            case "SUCCESS": return res;
            case "REMITING":
                if(tried > MAX_RETRIE_TIME){
                    return "查询订单超时";
                }
                try {
                    Thread.sleep(Double.valueOf(Math.pow(RETRIE_POW, tried)).longValue() * 1000);
                    return queryDaifu(requestNo, tried + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "查询订单异常";
                }
            case "FAIL": return res.getResultMap().getBizMsg();
            default:
        }
        return res;
    }

    @Override
    public Order withPayForCustomer(User user, Card debitCard, Integer money){

        UserRate userRate = userService.computeUserRate(user);
        Order order = orderService.createWithdrawOrder(getChannel(), user, debitCard, money);
        Double rateMoney = money * userRate.getWithdrawRate() + userRate.getWithdrawFixedAmount();
        order.setRateMoney(rateMoney.intValue());
        order.setArriveMoney(money - rateMoney.intValue());

        if(cfg.getDisabled()){
            return orderService.failOrder(order, CLOSEDMSG);
        }

        if(cfg.getBusinessType() != null && !BusinessType.WITHDRAW.equals(this.cfg.getBusinessType())){
            return orderService.failOrder(order, NOSUPPORTED_BUSINESS);
        }


        Object result = this.daifu(debitCard, order);
        if(result instanceof String){
            return orderService.failOrder(order, (String)result);
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Object res = queryDaifu(order.getOrderNo(), 1);
                if(res instanceof String){
                    orderService.failOrder(order, (String)res);
                    return;
                }
                orderService.successOrder(order);
            }
        }, 0);
        return order;
    }

}
