package com.yitkeji.test.songshushenghuo.admin;

import com.alibaba.fastjson.JSON;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OrderTest extends BaseTest {
    AdminTest adminTest = new AdminTest();

    /**
     * 查询列表
     */
    @Test
    public void orderList(){
        adminTest.login();
        Map<String, Object> params = new HashMap<>();
        params.put("total", 0);
        params.put("pageSize", 20);
        postJson(baseurl + "/admin/order/list", JSON.toJSONString(params));
    }
}
