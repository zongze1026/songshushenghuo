package com.yitkeji.test.songshushenghuo.app;

import com.yitkeji.songshushenghuo.vo.req.app.PlanPreviewReq;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 还款计划功能测试用例
 */
public class PlanTest  extends BaseTest {

    /**
     * 已创建的还款列表测试
     */
    @Test
    public void query() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", "17");
        params.put("cardId", "29");
        postSignJson(baseurl + "/app/plan/query", params);

    }

    /**
     * 创建还款计划测试
     */
    @Test
    public void add() {
        Map<String, Object> params = new HashMap<>();
        params.put("cardId", "1");
        postSignJson(baseurl + "/app/plan/add", params);

    }

    /**
     * 获取还款日期测试
     */
    @Test
    public void repaydate() {
        Map<String, Object> params = new HashMap<>();
        params.put("cardId", "29");
        postSignJson(baseurl + "/app/plan/repaydate", params);

    }

    /**
     * 检索卡是否有已经创建的计划测试
     */
    @Test
    public void retrieve() {
        Map<String, Object> params = new HashMap<>();
        params.put("cardId", "29");
        postSignJson(baseurl + "/app/plan/retrieve", params);

    }

    /**
     * 终止还款计划测试
     */
    @Test
    public void remove() {
        Map<String, Object> params = new HashMap<>();
        params.put("cardNo", "6258101665631158");
        postSignJson(baseurl + "/app/plan/remove", params);

    }

    /**
     * 预览还款计划测试
     */
    @Test
    public void preview() {
        PlanPreviewReq req = new PlanPreviewReq();
//        req.setToken("ebed7e9633f24f03868f0857300c6ca7");
//        req.setRequestNo("514840314708762624");
//        req.setCode("914871");
        req.setCardId(1L);
        req.setPlanMoney(300000);
        req.setDayPlanNum(2);

        List<String> planDays = new ArrayList<>();
        planDays.add("2018-12-11");
        planDays.add("2018-12-12");

        req.setPlanDays(planDays);

        postSignJson(baseurl + "/app/plan/preview", req);

    }



    /**
     * 还款记录
     */
    @Test
    public void list() {
        Map<String, Object> params = new HashMap<>();
        params.put("cardId", "941");
        params.put("status", "3");

        postSignJson(baseurl + "/app/plan/list", params);

    }
}
