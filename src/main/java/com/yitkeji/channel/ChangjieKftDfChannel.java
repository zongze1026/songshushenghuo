package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.yitkeji.changjiekftdf.ChangjieKftDf;
import com.yitkeji.changjiekftdf.model.Daifu;
import com.yitkeji.channel.inter.WithdrawChannel;
import com.yitkeji.channel.model.ChangjieKftDfRes;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Bank;
import com.yitkeji.songshushenghuo.vo.cfg.channel.ChangjieKftDfCfg;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.model.UserRate;

public class ChangjieKftDfChannel extends BaseChannel<ChangjieKftDfCfg> implements WithdrawChannel<ChangjieKftDfCfg> {

    private ChangjieKftDf changjieKftDf;
    private String notifyUrl;

    private ChangjieKftDfChannel(){
        super(Channel.CHANGJIEKFTDF);
        this.initCfg();
    }

    private static final class ChannelHolder{
        private static final ChangjieKftDfChannel CHANNEL = new ChangjieKftDfChannel();
    }

    public static final ChangjieKftDfChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg();
        return ChannelHolder.CHANNEL;
    }

    protected void initCfg(){
        if(needReloadCfg(SystemCfg.getInstance().getChannel().getChangjiekftdf(), cfg)){
            cfg = SystemCfg.getInstance().getChannel().getChangjiekftdf();
            changjieKftDf = new ChangjieKftDf(cfg.getBaseurl(), cfg.getMerchantNo(), cfg.getKey());
            changjieKftDf.setLogger(channelLogger);
            notifyUrl = "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + cfg.getNotifyUrl();
        }
    }

    private ChangjieKftDfRes parseRes(String result){
        ChangjieKftDfRes changjieKftDfRes = new ChangjieKftDfRes();
        try {
            changjieKftDfRes = JSON.parseObject(result, ChangjieKftDfRes.class);
        }catch (Exception e){
            e.printStackTrace();
            changjieKftDfRes.setCode("");
            changjieKftDfRes.setMessage("接口响应异常");
        }
        return changjieKftDfRes;
    }

    /**
     * 代付
     * @param debitCard
     * @param order
     * @return
     */
    private Object daifu(Card debitCard, Order order){
        Bank debitBank = debitCard.matchBank();

        Daifu daifu = new Daifu();
        daifu.setOrderNo(order.getOrderNo());
        daifu.setTradeName(order.getDesc());
        daifu.setAmount(order.getArriveMoney() + "");
        daifu.setRemark(order.getDesc());
        daifu.setCustBankNo(debitBank.getKftCode());
        daifu.setCustBankAccountNo(debitCard.getCardNo());
        daifu.setCustName(debitCard.getName());
        daifu.setCustBankAcctType("1");
        daifu.setCustID(debitCard.getIdcard());
        ChangjieKftDfRes res = parseRes(changjieKftDf.daifu(daifu));
        if(!res.isSuccess()){
            return res.getErrorMsg();
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
        return orderService.successOrder(order);
    }

}
