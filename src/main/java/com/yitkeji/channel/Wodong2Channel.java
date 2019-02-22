package com.yitkeji.channel;

import com.yitkeji.channel.util.StringUtil;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

import java.util.HashMap;
import java.util.Map;

public class Wodong2Channel extends SmsBaseChannel {

    private static final class ChannelHolder{
        private static final Wodong2Channel CHANNEL = new Wodong2Channel();
    }

    private Wodong2Channel(){
        super(Channel.WODONG2, SystemCfg.getInstance().getChannel().getWodong2());
    }
    public static final Wodong2Channel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getWodong2());
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
        params.put("SpCode", cfg.getUserid());
        params.put("LoginName", cfg.getUsername());
        params.put("Password", cfg.getPassword());
        params.put("MessageContent", content);
        params.put("UserNumber", phone);
        params.put("SerialNumber", System.currentTimeMillis() + StringUtil.randomNum(3));
        httpUtil.postForm(cfg.getUrl(), ObjectUtil.objectToFormStr(params));
        return true;
    }
}
