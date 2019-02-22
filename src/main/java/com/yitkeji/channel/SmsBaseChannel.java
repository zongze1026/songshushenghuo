package com.yitkeji.channel;

import com.yitkeji.channel.inter.SmsChannel;
import com.yitkeji.channel.model.ChannelLogger;
import com.yitkeji.channel.util.HttpUtil;
import com.yitkeji.channel.util.StringUtil;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.appinfo.AppInfo;
import com.yitkeji.songshushenghuo.vo.cfg.channel.SmsCfg;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.enums.SmsType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SmsBaseChannel extends BaseChannel<SmsCfg> implements SmsChannel {

    protected HttpUtil httpUtil = new HttpUtil();

    protected SmsBaseChannel(Channel channel, SmsCfg cfg){
        super(channel);
        this.initCfg(cfg);
    }

    protected void initCfg(SmsCfg cfg){
        if(needReloadCfg(cfg, super.cfg)){
            super.cfg = cfg;
            httpUtil.setLogger(new ChannelLogger(getChannel().name(), logApiService));
        }
    }

    /**
     * 发送短信
     * @param phone
     * @param content
     * @return
     */
    @Override
    public Boolean sendMsg(String phone, String content){
        if(cfg.getDisabled()){
            return false;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("action", "send");
        params.put("account", cfg.getUsername());
        params.put("password", cfg.getPassword());
        params.put("userid", cfg.getUserid());
        params.put("content", content);
        params.put("mobile", phone);
        httpUtil.postForm(cfg.getUrl(), params);
        return true;
    }

    /**
     * 发送验证码短信，缓存过期时间5分钟
     * @param phone
     * @param type
     */
    @Override
    public void sendCodeMsg(String phone, SmsType type){
        String code = StringUtil.randomNum(6);
        RedisUtil.set(CacheKey.SMSCODE.getKey(phone, type.getCode()), code, 300, TimeUnit.SECONDS);
        AppInfo appInfo = SystemCfg.getInstance().getAppinfo();
        String content = String.format(getCfg().getTemplate().getRegist(), appInfo.getName(), appInfo.getName(), code);
        sendMsg(phone, content);
    }
}
