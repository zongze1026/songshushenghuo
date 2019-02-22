package com.yitkeji.test.songshushenghuo.app;

import com.yitkeji.test.songshushenghuo.BaseTest;

import java.util.HashMap;
import java.util.Map;

public class Test extends BaseTest {

    @org.junit.Test
    public void bindCard(){
        Map<String, Object> params = new HashMap<>();
        params.put("cardId", "3");
        params.put("planMoney", "1");
        params.put("requestNo", "468744733368254464");
        params.put("smsCode", "523307");
        postSignJson(baseurl + "/app/plan/preview", params);
    }

    @org.junit.Test
    public void runPlan(){
        Map<String, Object> params = new HashMap<>();
        params.put("planId", "1");
        postSignJson(baseurl + "/app/plan/run", params);
    }

    @org.junit.Test
    public void smsAuth(){
        Map<String, Object> params = new HashMap<>();
        params.put("scardId", "1");
        postSignJson(baseurl + "/app/test/smsAuth", params);
    }
}
