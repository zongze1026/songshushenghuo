package com.yitkeji.test.songshushenghuo.admin;

import com.alibaba.fastjson.JSON;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ChannelTest extends BaseTest {
    AdminTest adminTest = new AdminTest();

    @Test
    public void changjieSettlement(){
        adminTest.login();
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", 1049);
        postJson(baseurl + "/admin/channel/yunshanfu/settlement", JSON.toJSONString(params));
    }

    @Test
    public void queryWallet(){
        adminTest.login();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1);
        postJson(baseurl + "/admin/channel/yunshanfu/queryWallet", JSON.toJSONString(params));
    }

}
