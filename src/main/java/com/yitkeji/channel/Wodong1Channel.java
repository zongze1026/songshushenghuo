package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

import java.util.HashMap;
import java.util.Map;

public class Wodong1Channel extends SmsBaseChannel {

    private static final class ChannelHolder{
        private static final Wodong1Channel CHANNEL = new Wodong1Channel();
    }

    private Wodong1Channel(){
        super(Channel.WODONG1, SystemCfg.getInstance().getChannel().getWodong1());
    }
    public static final Wodong1Channel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getWodong1());
        return ChannelHolder.CHANNEL;
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
}
