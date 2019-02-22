package com.yitkeji.test.songshushenghuo.app;

import com.yitkeji.songshushenghuo.vo.req.app.CardAddReq;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class CardTest extends BaseTest {
    private Logger logger = Logger.getLogger(CardTest.class);

    //身份认证
    @Test
    public void identify(){
        Map<String, Object> identifyMap = new HashMap<String, Object>();
        identifyMap.put("cardNo","6228480425002504677");
        identifyMap.put("idcard","320684199505226175");
        identifyMap.put("name","朱志鹏");
        identifyMap.put("phone","13791904546");
        identifyMap.put("partnerOrderId","");
        identifyMap.put("userStatus","0");

        postSignJson(baseurl + "/app/card/identify", identifyMap);
    }

    //添加信用卡
    @Test
    public void add(){

        CardAddReq req = new CardAddReq();
        req.setType(0);
        req.setCardNo("6222081204001911076");
        req.setPhone("13967337731");
//        req.setCvv("689");
//        req.setExpiryDate("1222");
        postSignJson(baseurl + "/app/card/add", req);

    }

    //修改信用卡
    @Test
    public void updcard(){
        Map<String, Object> cardMap = new HashMap<String, Object>();
        cardMap.put("cardId","65");
        cardMap.put("phone","18110275162");
        cardMap.put("repaymentDay","10");
        cardMap.put("billDay","30");

        postSignJson(baseurl + "/app/card/updcard", cardMap);

    }


    @Test
    public void bankList(){

        getJson(baseurl + "/app/bank/list", null);

    }

    //获取信用卡列表
    @Test
    public void cardlist(){
        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("type", 1); // 可以为空
        params.put("npage", 0);
        params.put("pageSize", 100);
        getForm(baseurl + "/app/card/list", params);

    }

    //删除信用卡
    @Test
    public void delcard(){
        Map<String, Object> cardMap = new HashMap<String, Object>();
        cardMap.put("cardNo","6464464211213213213");
        postSignJson(baseurl + "/app/card/delcard", cardMap);
    }
}
