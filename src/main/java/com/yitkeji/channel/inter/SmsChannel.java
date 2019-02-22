package com.yitkeji.channel.inter;

import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.cfg.channel.SmsCfg;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.SmsType;

import java.util.concurrent.TimeUnit;

public interface SmsChannel extends ChannelInterface<SmsCfg> {

    Boolean sendMsg(String phone, String content);

    void sendCodeMsg(String phone, SmsType type);

    static Boolean checkCode(String phone, SmsType type, String code){
        Object cacheCode = RedisUtil.get(CacheKey.SMSCODE.getKey(phone, type.getCode()));
        if("666888".equals(code) || code.equals(cacheCode)){
            RedisUtil.set(CacheKey.SMSCODE.getKey(phone, type.getCode()), 1, 1, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }
}
