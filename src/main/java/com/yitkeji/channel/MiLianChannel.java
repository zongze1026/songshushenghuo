package com.yitkeji.channel;

import com.alibaba.fastjson.JSONObject;
import com.yitkeji.channel.model.ChannelLogger;
import com.yitkeji.channel.util.HttpUtil;
import com.yitkeji.songshushenghuo.service.LogApiService;
import com.yitkeji.songshushenghuo.util.SignUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.MiLianCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.User;
import net.sf.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * 福建米联
 */
public class MiLianChannel extends BaseChannel<MiLianCfg> {

    private static HttpUtil httpUtil = new HttpUtil();
    private static MiLianCfg miLianCfg;

    private MiLianChannel() {
        super(Channel.MILIAN);
        logApiService = SpringUtil.getBean(LogApiService.class);
        httpUtil.setLogger(new ChannelLogger(Channel.MILIAN.name(), logApiService));
    }

    private static final class ChannelHolder{
        private static final MiLianChannel CHANNEL = new MiLianChannel();
    }

    public static final MiLianChannel getInstance(){
        return ChannelHolder.CHANNEL;
    }

    static {
        miLianCfg = SystemCfg.getInstance().getChannel().getMilian();
    }

    /**
     * 借贷产品列表接口
     */
    private static Object productList(){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", miLianCfg.getUserId());
        params.put("type", MILIANLOANTYPE);
        String sign = SignUtil.getSign(params, miLianCfg.getKey());
        params.put("sign", sign);
        String result = httpUtil.postForm(miLianCfg.getUrl() + "/productList", params);
        return getObject(result);
    }

    /**
     * 申请借贷接口
     */
    private static Object createInfo(User user, String categoryId){
        Map<String, Object> params = new HashMap<>();
        params.put("busCode", "2003");
        params.put("categoryId", categoryId);
        params.put("orderId", "W" + System.currentTimeMillis());
        params.put("userId", miLianCfg.getUserId());
        params.put("type", MILIANLOANTYPE);
        String sign = SignUtil.getSign(params, miLianCfg.getKey());
        params.put("sign", sign);
        params.put("bgUrl", "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + miLianCfg.getNotifyUrl());
        params.put("phoneNo", user.getPhone());
        params.put("idNo", user.getIdcard());
        params.put("name", user.getRealName());
        String result = httpUtil.postForm(miLianCfg.getUrl() + "/createInfo", params);
        return result;
    }

    /**
     *  积分兑换,银行列表接口
     */
    private static Object bankList(){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", "1810002812");
        String sign = SignUtil.getSign(params, "DD4DA7BE5A4E8DFECD8CE4C189C4A07D");
        params.put("sign", sign);
        String result = httpUtil.postForm("http://trade.bf-ifs.com/OrgIntf/payment" + "/bankList", params);
        return result;
    }

    /**
     *  积分兑换,银行详情接口
     */
    private static Object bankDetail(){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", miLianCfg.getUserId());
        String sign = SignUtil.getSign(params, miLianCfg.getKey());
        params.put("sign", sign);
        params.put("bankId", "2");
        String result = httpUtil.postForm("http://trade.bf-ifs.com/OrgIntf/payment" + "/bankDetail", params);
        return result;
    }

    /**
     *  积分兑换,分类详情接口
     */
    private static Object bankClassifyDetail(){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", miLianCfg.getUserId());
        String sign = SignUtil.getSign(params, miLianCfg.getKey());
        params.put("sign", sign);
        params.put("classifyId", "2");
        String result = httpUtil.postForm("http://trade.bf-ifs.com/OrgIntf/payment" + "/bankClassifyDetail", params);
        return result;
    }

    /**
     *  积分兑换,提交报单接口
     */
    private static Object integralInfo(){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", miLianCfg.getUserId());
        params.put("busCode", "2009");
        params.put("orderId", "W" + System.currentTimeMillis());
        String sign = SignUtil.getSign(params, miLianCfg.getKey());
        params.put("sign", sign);

        params.put("category", "");
        params.put("categoryId", miLianCfg.getUserId());
        params.put("bankId", MILIANLOANTYPE);
        params.put("bgUrl", "http://" + SystemCfg.getInstance().getAppinfo().getDomain() + miLianCfg.getNotifyUrl());
        params.put("bankName", "");
        params.put("classifyId", "");
        params.put("coupon", "");
        String result = httpUtil.postForm("http://trade.bf-ifs.com/OrgIntf/payment" + "/IntegralInfo", params);
        return getObject(result);
    }

    /**
     * 处理返回结果
     */
    private static Object getObject(String retMsg) {
        if (retMsg == null) {
            return "系统异常，请联系客服";
        }
        JSONObject result = (JSONObject) JSONObject.parse(retMsg);
        String data = result.getString("data");
        JSONObject jsonData = (JSONObject) JSONObject.parse(data);
        if ("0000".equals(result.getString("respCode"))){
            return jsonData;
        }else {
            return result.getString("respMsg");
        }
    }

    /**
     * 获取借贷产品列表
     */
    public Object getProductList(){
        Object result = productList();
        if (result instanceof String){
            return result;
        }
        JSONObject jsonResult = (JSONObject) JSONObject.toJSON(result);
        JSONArray jsonArray = JSONArray.fromObject(jsonResult.getString("productList"));
        return jsonArray;
    }

    public static void main(String[] args) {
        MiLianChannel.bankList();
    }
}
