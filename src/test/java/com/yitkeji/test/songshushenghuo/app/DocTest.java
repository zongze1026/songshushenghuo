package com.yitkeji.test.songshushenghuo.app;

import com.yitkeji.test.songshushenghuo.BaseTest;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 文章功能测试用例
 */
public class DocTest extends BaseTest {
    private Logger logger = Logger.getLogger(DocTest.class);

    @Test
    public void list() {
        Map<String, Object> params = new HashMap<>();
        params.put("types", "AGREEMENT");
        params.put("npage", 0);
        params.put("pageSize", 5);
        postSignJson(baseurl + "/app/doc/list", params);

    }

    @Test
    public void docInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("docId", "10");
        postSignJson(baseurl + "/app/doc/info", params);

    }

    @Test
    public void add() {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "开机广告");
        params.put("image", "http://m.akzngj.com/static/images/doc6_55.png");
        params.put("desc", "开机广告");
        params.put("type", "8");
        params.put("content", "hi第四方法isGIF告诉对方水电费三个分");
        postSignJson(baseurl + "/app/doc/add", params);

    }
}
