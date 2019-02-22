package com.yitkeji.test.songshushenghuo.app;

import com.alibaba.fastjson.JSON;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单记录测试用例
 */
public class OrderTest extends BaseTest {

    /**
     * 订单列表
     */
    @Test
    public void list() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", 0);
        postSignJson(baseurl + "/app/order/list", params);

    }

    /**
     * 查询详情
     */
    @Test
    public void orderInfo() throws InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", "834");
        postJson(baseurl + "/admin/order/info", JSON.toJSONString(params));
    }
}
