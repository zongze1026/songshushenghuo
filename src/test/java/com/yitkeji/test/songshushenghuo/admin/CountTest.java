package com.yitkeji.test.songshushenghuo.admin;

import com.alibaba.fastjson.JSON;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CountTest extends BaseTest {
    AdminTest adminTest = new AdminTest();

    @Test
    public void getCountInfo(){
        adminTest.login();
        Map<String, Object> params = new HashMap<>();
        postJson(baseurl + "/admin/count/info", JSON.toJSONString(params));
    }
}
