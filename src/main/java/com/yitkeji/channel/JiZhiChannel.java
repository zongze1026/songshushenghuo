package com.yitkeji.channel;

import cn.hihippo.util.ParamsSign;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yitkeji.channel.model.ChannelLogger;
import com.yitkeji.channel.util.HttpUtil;
import com.yitkeji.songshushenghuo.service.LogApiService;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.JiZhiCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.Measure;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.res.app.MeasureRes;

import java.util.TreeMap;

public class JiZhiChannel extends BaseChannel<JiZhiCfg> {

    private static HttpUtil httpUtil = new HttpUtil();
    private static JiZhiCfg jiZhiCfg;

    private JiZhiChannel() {
        super(Channel.JIZHI);
        logApiService = SpringUtil.getBean(LogApiService.class);
        httpUtil.setLogger(new ChannelLogger(Channel.JIZHI.name(), logApiService));
    }

    private static final class ChannelHolder{
        private static final JiZhiChannel CHANNEL = new JiZhiChannel();
    }

    public static final JiZhiChannel getInstance(){
        return ChannelHolder.CHANNEL;
    }

    static {
        jiZhiCfg = SystemCfg.getInstance().getChannel().getJizhi();
    }

    /**
     * 短信授权
     */
    private static Object authorizedSms(Card card){
        TreeMap<String, String> params = new TreeMap<>();
        params.put("account", jiZhiCfg.getAccount());
        params.put("bankCard", card.getCardNo());
        params.put("mobile", card.getPhone());
        params.put("idCard", card.getIdcard());
        params.put("name", card.getName());
        params.put("sign", ParamsSign.value(params,jiZhiCfg.getAppSecret()));

        String  paramsJsonString = JSON.toJSONString(params);
        String result = httpUtil.postJson(jiZhiCfg.getUrl() + "/authorizedSms", paramsJsonString);
        JSONObject jsonResult = JSONObject.parseObject(result);
        return getObject(jsonResult);
    }

    /**
     * 卡测评
     */
    private Object reportCardNoData(Card card, String code, String serialNumber){
        TreeMap<String, String> params = new TreeMap<>();
        params.put("account", jiZhiCfg.getAccount());
        params.put("orderNo", "W" + System.currentTimeMillis());
        params.put("bankCard", card.getCardNo());
        params.put("mobile", card.getPhone());
        params.put("idCard", card.getIdcard());
        params.put("name", card.getName());
        params.put("serialNumber", serialNumber);
        params.put("identifyingCode", code);
        params.put("sign", ParamsSign.value(params,jiZhiCfg.getAppSecret()));

        String  paramsJsonString = JSON.toJSONString(params);
        String result = httpUtil.postJson(jiZhiCfg.getUrl() + "/reportCardNoData",paramsJsonString);
        JSONObject jsonResult = JSONObject.parseObject(result);
        return getObject(jsonResult);
    }

    /**
     * 处理返回结果
     */
    private static Object getObject(JSONObject result) {
        if (result == null) {
            return "系统异常，请联系客服";
        }
        if ("0000".equals(result.getString("code"))){
            return result;

        }else {
            return result.getString("msg");
        }
    }

    /**
     * 短信获取序列号
     */
    public Object getSerialNumber(Card card){
        Object result = authorizedSms(card);
        if (result instanceof String){
            return result;
        }
        JSONObject jsonResult = (JSONObject) JSONObject.toJSON(result);
        JSONObject data = (JSONObject) JSONObject.parse(jsonResult.getString("data"));
        MeasureRes measureRes = new MeasureRes();
        measureRes.setSerialNumber(data.getString("serialNumber"));
        return measureRes;
    }

    /**
     * 获取卡测评结果
     */
    public Object getCardMeasurementResult(Card card, User user, String code, String serialNumber){
        JiZhiCfg cfg = SystemCfg.getInstance().getChannel().getJizhi();
        if(cfg.getDisabled()){
            return CLOSEDMSG;
        }
        Object result = reportCardNoData(card, code, serialNumber);
        if (result instanceof String){
            return result;
        }
        JSONObject jsonResult = (JSONObject) JSONObject.toJSON(result);
        JSONObject data = (JSONObject) JSONObject.parse(jsonResult.getString("data"));
        Measure measure = measureService.createMeasure(data.getString("url"), card, user);
        return measure;
    }
}
