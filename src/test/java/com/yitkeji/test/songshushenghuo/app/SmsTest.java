package com.yitkeji.test.songshushenghuo.app;

import com.yitkeji.test.songshushenghuo.BaseTest;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class SmsTest extends BaseTest {
    private Logger logger = Logger.getLogger(SmsTest.class);


    @Test
    public void sendSms(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "13757318383");
        params.put("type", 2);
        postSignJson(baseurl + "/app/sms/code", params);
    }
}
