package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.yitkeji.channel.inter.PayChannel;
import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.AlipayCfg;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.Order;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AlipayChannel extends BaseChannel<AlipayCfg> implements PayChannel<AlipayCfg> {

    private AlipayClient alipayClient;
    private String notifyUrl;

    private AlipayChannel(){
        super(Channel.ALIPAY);
        this.initCfg();
    }

    private static final class ChannelHolder{
        private static final AlipayChannel CHANNEL = new AlipayChannel();
    }

    public static final AlipayChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg();
        return ChannelHolder.CHANNEL;
    }


    private void initCfg(){
        if(needReloadCfg(SystemCfg.getInstance().getChannel().getAlipay(), cfg)){
            cfg = SystemCfg.getInstance().getChannel().getAlipay();
            notifyUrl = "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + cfg.getNotifyUrl();
            alipayClient = new DefaultAlipayClient(cfg.getUrl(), cfg.getAppId(), cfg.getPrivateKey(), "json", "UTF-8", cfg.getPublicKey(), "RSA2");
        }
    }


    private String sendRequest(AlipayRequest request){
        AlipayResponse response = null;
        Date startTime = new Date();
        try {
            response = alipayClient.sdkExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } finally {
            channelLogger.httpLog(cfg.getUrl(), null, JSON.toJSONString(request.getBizModel()), response.getBody(), startTime, new Date());
        }
        return null;
    }


    private String pay(Order order){
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(order.getDesc());
        model.setSubject(order.getDesc());
        model.setOutTradeNo(order.getOrderNo());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(new DecimalFormat("#0.00").format(Double.valueOf(order.getMoney()) / 100));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return sendRequest(request);
    }


    @Override
    public Object upvip(Order order, Object... params){
        if(cfg.getDisabled()){
            return CLOSEDMSG;
        }
        if(cfg.getBusinessType() != null && !BusinessType.UPVIP.equals(cfg.getBusinessType())){
            return NOSUPPORTED_BUSINESS;
        }

        RedisUtil.set(CacheKey.ALIPAY.getKey(order.getOrderNo()), JSON.toJSONString(order), 1, TimeUnit.HOURS);
        Map<String, Object> temMap = new HashMap<>();
        temMap.put("paySign",pay(order));
        temMap.put("money", order.getMoney());
        temMap.put("createTime", DateUtil.format(order.getCreateTime(), DateUtil.DATEFORMAT_NINTEEN));
        return temMap;
    }

    @Override
    public Object cardMeasure(Order order, Object... params) {
        if(cfg.getDisabled()){
            return CLOSEDMSG;
        }

        if(cfg.getBusinessType() != null && !BusinessType.CARDMEASURE.equals(cfg.getBusinessType())){
            return NOSUPPORTED_BUSINESS;
        }

        RedisUtil.set(CacheKey.ALIPAY.getKey(order.getOrderNo()), JSON.toJSONString(order), 1, TimeUnit.HOURS);
        Map<String, Object> temMap = new HashMap<>();
        temMap.put("paySign",pay(order));
        temMap.put("money", order.getMoney());
        temMap.put("createTime", DateUtil.format(order.getCreateTime(), DateUtil.DATEFORMAT_NINTEEN));
        return temMap;
    }
}
