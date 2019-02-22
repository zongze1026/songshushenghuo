package com.yitkeji.test.songshushenghuo.admin;

import com.alibaba.fastjson.JSON;
import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTest extends BaseTest {

    /**
     * 批量测试分页查询接口
     */
    @Test
    public void getList() throws InterruptedException {
        String moduleStr = "order";
        String[] modules = moduleStr.split(",");
        for(String module: modules){
            Map<String, Object> params = new HashMap<>();
            params.put("total", 0);
            params.put("pageSize", 20);

            List<Object> list = new ArrayList<>();
            list.add("createTime");
            list.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtil.getTodayStartTime()));
            list.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtil.getTodayEndTime()));
//            params.put("cond", "{\"between\": "+JSON.toJSONString(list)+"}");
            postJson(baseurl + "/admin/"+module+"/list", JSON.toJSONString(params));
            Thread.sleep(500);
        }
    }

    /**
     * 管理员登录
     */
    @Test
    public void login(){
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "administrator");
        params.put("password", "a123456");
        postJson(baseurl + "/admin/admin/login", JSON.toJSONString(params));
    }



    @Test
    public void modify(){
        Map<String, Object> params = new HashMap<>();
        params.put("password", "wz12345");
        params.put("newPassword", "wz12345");
        postJson(baseurl + "/admin/admin/modify", JSON.toJSONString(params));
    }
}
