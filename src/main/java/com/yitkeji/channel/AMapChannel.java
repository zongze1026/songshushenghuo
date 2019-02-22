package com.yitkeji.channel;

import com.alibaba.fastjson.JSON;
import com.yitkeji.channel.inter.MapChannel;
import com.yitkeji.channel.model.Position;
import com.yitkeji.channel.util.HttpUtil;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.channel.AMapCfg;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

import java.util.concurrent.TimeUnit;

public class AMapChannel extends BaseChannel<AMapCfg> implements MapChannel<AMapCfg> {

    private HttpUtil httpUtil = new HttpUtil();
    private AMapChannel(){
        super(Channel.AMAP);
        this.initCfg();
    }

    private static final class ChannelHolder{
        private static final AMapChannel CHANNEL = new AMapChannel();
    }

    public static final AMapChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg();
        return ChannelHolder.CHANNEL;
    }


    private void initCfg(){
        if(needReloadCfg(SystemCfg.getInstance().getChannel().getAlipay(), cfg)){
            httpUtil.setLogger(channelLogger);
            cfg = SystemCfg.getInstance().getChannel().getAmap();
        }
    }


    private String sendRequest(String ip){
        String data = "key=" + cfg.getApiKey() + "&ip=" + ip;
        return httpUtil.callHttp(cfg.getBaseurl(), data, HttpUtil.METHOD_POST, HttpUtil.MEDIA_TYPE_FORM, null);
    }

    @Override
    public Position position(String ip) {
        if(!SystemCfg.getInstance().getAdmin().getCache()){
            return JSON.parseObject(sendRequest(ip), Position.class);
        }
        Object cache = RedisUtil.get(CacheKey.AMAP.getKey(ip));
        if(cache != null){
            return (Position)cache;
        }
        Position position = JSON.parseObject(sendRequest(ip), Position.class);
        RedisUtil.set(CacheKey.AMAP.getKey(ip), position, 7, TimeUnit.DAYS);
        return position;

    }


}
