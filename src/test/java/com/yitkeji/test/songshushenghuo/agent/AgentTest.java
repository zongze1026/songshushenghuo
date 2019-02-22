package com.yitkeji.test.songshushenghuo.agent;

import com.alibaba.fastjson.JSON;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AgentTest extends BaseTest {

    @Test
    public static void login(){
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "17321001793");
        params.put("password", "a123456");
        postJson(baseurl + "/agent/agent/login", JSON.toJSONString(params));
    }
}
