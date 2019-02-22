package com.yitkeji.test.songshushenghuo.app;

import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UserWithdrawTest extends BaseTest {

    /**
     * 申请提现状态列表
     */
    @Test
    public void withdrawStatus(){
        Map<String, Object> withdrawStatusMap = new HashMap<String, Object>();
        postSignJson(baseurl + "/app/withdraw/status", withdrawStatusMap);

    }
}
