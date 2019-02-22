package com.yitkeji.test.songshushenghuo.agent;

import com.alibaba.fastjson.JSON;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CountTest extends BaseTest {

    @Test
    public void count(){
        AgentTest agentTest = new AgentTest();
        AgentTest.login();

        Map<String, Object> params = new HashMap<>();
        params.put("cond", "{\"between\": [\"createTime\", \"2018-10-11 10:10:00\", \"2018-11-11 10:10:00\"]}");
        postJson(baseurl + "/agent/count/info", JSON.toJSONString(params));
    }
}
