package com.yitkeji.channel;

import com.alibaba.fastjson.JSONObject;
import com.yitkeji.channel.util.HttpUtil;
import com.yitkeji.songshushenghuo.controller.app.UserController;
import com.yitkeji.songshushenghuo.service.CommissionService;
import com.yitkeji.songshushenghuo.util.SignUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.util.ZGLTool;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.YoudunCfg;
import com.yitkeji.songshushenghuo.vo.enums.AuthStatus;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.enums.CommissionType;
import com.yitkeji.songshushenghuo.vo.model.User;
import org.apache.log4j.Logger;

import java.util.Date;

public class YoudunChannel extends BaseChannel {
    private static HttpUtil httpUtil = new HttpUtil();
    private static CommissionService commissionService = null;
    private static YoudunCfg youdunCfg;

    private Logger logger = Logger.getLogger(UserController.class);

    private static final class ChannelHolder{
        private static final YoudunChannel CHANNEL = new YoudunChannel();
    }

    private YoudunChannel() {
        super(Channel.YOUDUN);
        commissionService = SpringUtil.getBean(CommissionService.class);
    }

    public static final YoudunChannel getInstance(){
        return ChannelHolder.CHANNEL;
    }

    static {
        youdunCfg = SystemCfg.getInstance().getChannel().getYoudun();
    }

    /**
     * 有盾参数效验
     */
    private static JSONObject youdun(String partnerOrderId){
        JSONObject renJson = new JSONObject();
        String sign_time = ZGLTool.getStringDate(new Date());
        String sign = SignUtil.getMD5Sign(youdunCfg.getPub_key(), partnerOrderId, sign_time, youdunCfg.getSecurity_key());
        renJson.put("partner_order_id", partnerOrderId);
        renJson.put("sign", sign);
        renJson.put("sign_time", sign_time);
        renJson.put("url", youdunCfg.getUrl());
        String strMsg = httpUtil.postJson(SystemCfg.getInstance().getAppinfo().getProxy(), renJson.toJSONString());
        JSONObject result = JSONObject.parseObject(strMsg);
        return result;
    }

    /**
     * 有盾效验结果
     */
    public Object youdunVerify(User user, String partnerOrderId){
        JSONObject result = YoudunChannel.youdun(partnerOrderId);
        String resResp = result.getString("result");
        JSONObject resJson = JSONObject.parseObject(resResp);

        if (resJson.getBoolean("success")) {
            String dataResp = result.getString("data");
            JSONObject dataJson = JSONObject.parseObject(dataResp);

            // 更新用户信息
            user.setIdcard(dataJson.getString("id_number"));
            user.setRealName(dataJson.getString("id_name"));
            user.setAuthStatus(AuthStatus.PASS.getCode());
            userService.updateByPrimaryKey(user, "idcard", "realName", "authStatus");

            // 分发新手实名奖励
            int noviceAuthMoney = SystemCfg.getInstance().getSwitcher().getNoviceAuthAndCashMoney();
            commissionService.noviceTaskCommission(user,null, CommissionType.NOVICE_AUTH, CommissionType.REFERRAL_AUTH, noviceAuthMoney);
        } else {
            return resJson.getString("message");
        }
        return user;
    }
}
