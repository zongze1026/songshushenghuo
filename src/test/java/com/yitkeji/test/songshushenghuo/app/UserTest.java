package com.yitkeji.test.songshushenghuo.app;

import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.vo.req.app.UserAuthReq;
import com.yitkeji.songshushenghuo.vo.req.app.UserCheckIdcardReq;
import com.yitkeji.songshushenghuo.vo.req.app.UserLoginReq;
import com.yitkeji.songshushenghuo.vo.req.app.UserRegisterReq;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class UserTest extends BaseTest {
    private Logger logger = Logger.getLogger(UserTest.class);

    //登录
    @Test
    public void login(){
        UserLoginReq userLoginReq = new UserLoginReq();
        userLoginReq.setUserName("17321001793");
        userLoginReq.setPassword("a123456");
        postSignJson(baseurl + "/app/user/login", userLoginReq);

    }

    //注册
    @Test
    public void register(){
        UserRegisterReq userRegisterReq = new UserRegisterReq();
        userRegisterReq.setPhone("17321001793");
        userRegisterReq.setPassword("a123");
        userRegisterReq.setCode("6688");
        userRegisterReq.setReferralCode("36rz8ja9");
        userRegisterReq.setLongitude("118.946997");
        userRegisterReq.setLatitude("28.950242");
        userRegisterReq.setCity("衢州市");
        postSignJson(baseurl + "/app/user/register", userRegisterReq);
    }

    //账号退出
    @Test
    public void exit(){
        getForm(baseurl + "/app/user/exit", null);
    }

    //获取用户信息
    @Test
    public void get(){
        getForm(baseurl + "/app/user/get", null);
    }

    //找回密码
    @Test
    public void foundpwd(){
        Map<String, Object> foundpwdMap = new HashMap<String, Object>();
        foundpwdMap.put("phone","17321001793");
        foundpwdMap.put("password","a1234567");
        foundpwdMap.put("code","6688");

        postSignJson(baseurl + "/app/user/foundpwd", foundpwdMap);

    }

    //修改登录密码
    @Test
    public void editpwd(){
        Map<String, Object> editpwdMap = new HashMap<String, Object>();
        editpwdMap.put("password","1");
        editpwdMap.put("oldPwd","a123456");

        postSignJson(baseurl + "/app/user/editpwd", editpwdMap);

    }


    @Test
    public void userAuth(){
        UserAuthReq userAuthReq = new UserAuthReq();
        userAuthReq.setPartnerOrderId("1533016358075");
        postSignJson(baseurl + "/app/user/auth", ObjectUtil.objectToMap(userAuthReq));
    }


    public void checkIdcard(){
        UserCheckIdcardReq req = new UserCheckIdcardReq();
        req.setIdcard("41272819910310451X");
        getForm(baseurl + "/app/user/checkIdcard", req);
    }
}
