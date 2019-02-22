package com.yitkeji.songshushenghuo.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.yitkeji.channel.AlipayChannel;
import com.yitkeji.channel.WechatChannel;
import com.yitkeji.songshushenghuo.service.OrderService;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.cfg.channel.AlipayCfg;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.OrderStatus;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.res.CallbackWechatRes;
import com.yitkeji.wechat.model.NotifyReq;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;


@Controller
@RequestMapping("/callback")
public class CallbackController {
    private Logger logger = Logger.getLogger(CallbackController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;


    @RequestMapping(value = "/wechat", method = RequestMethod.POST, produces = { "application/xml;charset=UTF-8" })
    @ResponseBody
    public synchronized String wechat(@RequestBody String notifyStr) {
        NotifyReq notifyReq = WechatChannel.getInstance().parseNotify(notifyStr);
        String orderNo = notifyReq.getOut_trade_no();
        if(notifyReq == null){
            return CallbackWechatRes.fail("解析失败或签名不对");
        }

        Order order = RedisUtil.get(CacheKey.WECHAT.getKey(orderNo));
        if(order == null || order.getMoney() != Integer.parseInt(notifyReq.getTotal_fee())){
            return CallbackWechatRes.fail("订单信息不符");
        }
        if(!order.matchOrderStatus().equals(OrderStatus.CREATED)){
            return CallbackWechatRes.success();
        }
        if(!"SUCCESS".equals(notifyReq.getResult_code())){
            orderService.failOrder(order, notifyReq.getErr_code_des());
            return CallbackWechatRes.success();
        }
        order.setTrackingNo(notifyReq.getTransaction_id());
        orderService.add(order);
        orderService.successOrder(order);
        return CallbackWechatRes.success();
    }

    @RequestMapping(value = "/alipay", method = RequestMethod.POST)
    @ResponseBody
    public synchronized String alipay(ModelMap map, @RequestBody String notifyStr) throws IOException {
        Map<String, String> paramsMap = new LinkedMap();
        String[] kvs = notifyStr.split("&|=");
        for(int i=0; i<kvs.length; i+=2){
            paramsMap.put(kvs[i], URLDecoder.decode(kvs[i+1], "UTF-8"));
        }
        paramsMap.remove("sign_type");

        try {
            AlipayCfg channelCfg = AlipayChannel.getInstance().getCfg();
            // 调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, channelCfg.getPublicKey(), "UTF-8");

            // TODO 签名问题。。
            if(!signVerified){
                System.out.println("验签失败");
                //return "failure";
            }
            String orderNo = paramsMap.get("out_trade_no");
            Order order = JSON.parseObject((String)redisTemplate.opsForValue().get(CacheKey.ALIPAY.getKey(orderNo)), Order.class);
            if(order == null
                    || order.getMoney() != Double.valueOf(paramsMap.get("total_amount")) * 100
                    || !paramsMap.get("app_id").equals(channelCfg.getAppId())){
                return "failure";
            }
            if("TRADE_SUCCESS".equals(paramsMap.get("trade_status"))){
                order.setTrackingNo(paramsMap.get("trade_no"));
                orderService.add(order);
                orderService.successOrder(order);
                return "success";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
        return "failure";
    }


    @RequestMapping(value = "/normal", method = RequestMethod.POST)
    @ResponseBody
    public String normal(){
        return "SUCCESS";
    }

}
