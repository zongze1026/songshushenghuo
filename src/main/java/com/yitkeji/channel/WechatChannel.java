package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.yitkeji.channel.inter.PayChannel;
import com.yitkeji.channel.model.ChannelLogger;
import com.yitkeji.channel.model.WechatRes;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.WechatCfg;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.wechat.Wechat;
import com.yitkeji.wechat.model.NotifyReq;
import com.yitkeji.wechat.model.WechatReq;

import java.util.concurrent.TimeUnit;

public class WechatChannel extends BaseChannel<WechatCfg> implements PayChannel<WechatCfg> {

    private static Wechat wechat;

    private static final class ChannelHolder{
        private static final WechatChannel CHANNEL = new WechatChannel();
    }

    private WechatChannel(){
        super(Channel.WECHAT);
        this.initCfg();
    }

    public static final WechatChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg();
        return ChannelHolder.CHANNEL;
    }

    private void initCfg(){
        if(needReloadCfg(cfg, SystemCfg.getInstance().getChannel().getWechat())){
            cfg = SystemCfg.getInstance().getChannel().getWechat();
            wechat = new Wechat(cfg.getAppid_app(), cfg.getMch_id_app(), cfg.getKey());
            wechat.setLogger(new ChannelLogger(Channel.WECHAT.name(), logApiService));
        }
    }


    /**
     * 生成预支付订单，
     * 成功是返回WechatRes对象，失败时返回失败原因
     * @return
     */
    private Object unifiedorder(Order order, String ip){
        WechatReq wechatReq = new WechatReq();
        wechatReq.setNonce_str("yitkeji.com");
        wechatReq.setBody(order.getDesc());
        wechatReq.setOut_trade_no(order.getOrderNo());
        wechatReq.setTotal_fee(order.getMoney() + "");
        wechatReq.setSpbill_create_ip(ip);
        wechatReq.setNotify_url("http://" + SystemCfg.getInstance().getAppinfo().getDomain() + cfg.getNotifyUrl());
        wechatReq.setTrade_type("APP");

        String result = wechat.unifiedorder(wechatReq);
        WechatRes wechatRes = ObjectUtil.convertXmlStr(result, WechatRes.class);
        if(!"SUCCESS".equals(wechatRes.getReturn_code())){
            return wechatRes.getReturn_msg();
        }
        if(!"SUCCESS".equals(wechatRes.getResult_code())){
            return wechatRes.getErr_code_des();
        }
        return wechatRes;
    }


    public Object closeOrder(String orderNo){
        String result = wechat.closeOrder(orderNo);
        WechatRes wechatRes = ObjectUtil.convertXmlStr(result, WechatRes.class);
        if(!"SUCCESS".equals(wechatRes.getResult_code())){
            return wechatRes.getReturn_msg();
        }
        if(!"SUCCESS".equals(wechatRes.getResult_code())){
            return wechatRes.getErr_code_des();
        }
        return wechatRes;
    }


    public NotifyReq parseNotify(String notifyStr){
        return wechat.parseNotify(notifyStr);
    }


    /**
     * 升级vip，成功时返回app参数，失败时返回订单信息
     * @param order
     * @param params
     * @return
     */
    @Override
    public Object upvip(Order order, Object... params){

        if(cfg.getDisabled()){
            return CLOSEDMSG;
        }

        if(cfg.getBusinessType() != null && !BusinessType.UPVIP.equals(cfg.getBusinessType())){
            return NOSUPPORTED_BUSINESS;
        }

        Object result = unifiedorder(order, (String)params[0]);
        if(result instanceof String){
            return result;
        }
        RedisUtil.set(CacheKey.WECHAT.getKey(order.getOrderNo()), JSON.toJSONString(order), 1, TimeUnit.DAYS);
        WechatRes wechatRes = (WechatRes)result;
        WechatReq wechatReq = wechat.createAppPayParams(wechatRes.getPrepay_id());
        return wechatReq;
    }

    @Override
    public Object cardMeasure(Order order, Object... params) {
        if(cfg.getDisabled()){
            return CLOSEDMSG;
        }

        if(cfg.getBusinessType() != null && !BusinessType.CARDMEASURE.equals(cfg.getBusinessType())){
            return NOSUPPORTED_BUSINESS;
        }

        Object result = unifiedorder(order, (String)params[0]);
        if(result instanceof String){
            return result;
        }
        RedisUtil.set(CacheKey.WECHAT.getKey(order.getOrderNo()), JSON.toJSONString(order), 1, TimeUnit.DAYS);
        WechatRes wechatRes = (WechatRes)result;
        WechatReq wechatReq = wechat.createAppPayParams(wechatRes.getPrepay_id());
        return wechatReq;
    }

}
