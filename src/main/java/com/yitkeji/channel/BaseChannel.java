package com.yitkeji.channel;


import com.alibaba.fastjson.JSON;
import com.yitkeji.channel.inter.ChannelInterface;
import com.yitkeji.channel.model.ChannelLogger;
import com.yitkeji.songshushenghuo.service.*;
import com.yitkeji.songshushenghuo.util.EncryptUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.channel.BaseChannelCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public abstract class BaseChannel<T extends BaseChannelCfg> implements ChannelInterface<T> {

    protected T cfg;
    protected UserService userService;
    protected CardService cardService;
    protected OrderService orderService;
    protected MeasureService measureService;
    protected static LogApiService logApiService;
    protected ChannelLogger channelLogger;
    private Channel channel;
    public static final String CLOSEDMSG = "该功能暂不可用";
    public static final String NOSUPPORTED_BUSINESS = "不支持该业务类型";
    public static final String MILIANLOANTYPE = "01";

    // 最大重试次数
    protected static final int MAX_RETRIE_TIME = 10;

    // 重试时间指数
    protected static final double RETRIE_POW = 2.0;


    @Override
    public Channel getChannel(){
        return channel;
    }


    protected BaseChannel(Channel channel){
        this.channel = channel;
        userService = SpringUtil.getBean(UserService.class);
        cardService = SpringUtil.getBean(CardService.class);
        orderService = SpringUtil.getBean(OrderService.class);
        measureService = SpringUtil.getBean(MeasureService.class);
        logApiService = SpringUtil.getBean(LogApiService.class);
        channelLogger = new ChannelLogger(channel.name(), logApiService);
    }


    /**
     * 对比两个对象值是否相同，用于刷新配置
     * @param obj1
     * @param obj2
     * @return
     */
    protected static boolean needReloadCfg(Object obj1, Object obj2){
        String sign1 = EncryptUtil.md5Encrypt(JSON.toJSONString(obj1));
        String sign2 = EncryptUtil.md5Encrypt(JSON.toJSONString(obj2));
        return !sign1.equals(sign2);
    }

    @Override
    public T getCfg(){
        return cfg;
    }

}
