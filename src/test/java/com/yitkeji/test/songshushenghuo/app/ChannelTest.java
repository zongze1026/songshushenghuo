package com.yitkeji.test.songshushenghuo.app;

import com.yitkeji.songshushenghuo.util.HttpUtils;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ChannelTest extends BaseTest {


    /**
     * 收款
     */
    @Test
    public void cash(){
        Map<String, Object> params = new HashMap<>();
        params.put("scardId", 1);
        params.put("tcardId", 2);
        params.put("money", 12000);
        params.put("code", "6688");
//        params.put("orderId", "346");
        postSignJson(baseurl + "/app/channel/cash", params);

    }


    /**
     * 代付
     */
    @Test
    public void withdraw(){
        Map<String, Object> params = new HashMap<>();
        params.put("tcardId", 2);
        params.put("money", 300);
        params.put("code", 6688);
        postSignJson(baseurl + "/app/channel/withdraw", params);

    }


    @Test
    public void upvip(){
        Map<String, Object> params = new HashMap<>();
        params.put("channel", "WECHAT");
        params.put("vip", 2);
        postSignJson(baseurl + "/app/channel/upvip", params);
    }

    @Test
    public void list(){
        Map<String, Object> params = new HashMap<>();
        params.put("businessType", "UPVIP");
        postSignJson(baseurl + "/app/channel/list", params);
    }

    @Test
    public void pay(){
        Map<String, Object> params = new HashMap<>();
        params.put("businessType", "CARDMEASURE");
//        params.put("vip", "2");
        params.put("channel", "ALIPAY");
        postSignJson(baseurl + "/app/channel/pay", params);
    }

    @Test
    public void cardMeasure(){
        Map<String, Object> params = new HashMap<>();
        params.put("cardId", "1");
//        params.put("serialNumber", "");
        params.put("orderNo", "W15403825243541153");
//        params.put("code", "2");
        postSignJson(baseurl + "/app/channel/measure", params);
    }

    /**
     * 银行四要素验证
     */
    @Test
    public void bankAuth() {
        Map<String, String> headers = new HashMap();
        headers.put("Authorization", "APPCODE " + "86d51f5a987c4c6382389f7ac3d745f1");

        Map<String, String> body = new HashMap<>();
        body.put("ReturnBankInfo", "YES");
        body.put("cardNo", "6222600260007527540");
        body.put("idNo", "340521199211125216");
        body.put("name", "殷书刚");
        body.put("phoneNo", "18110275162");

        // 根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        Map<String, String> query = new HashMap<>();
        new HttpUtils().doPost("http://yunyidata.market.alicloudapi.com", "/bankAuthenticate4", headers, query, body);
    }

    @Test
    public void YunshanfuAreas() {
        Map<String, Object> params = new HashMap<>();
        postSignJson(baseurl + "/app/channel/yunshanfu/areas", params);
    }
}
