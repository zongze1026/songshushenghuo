package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieJsxdhsmChannel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieJsxdhsmChannel CHANNEL = new ChangjieJsxdhsmChannel();
    }

    private ChangjieJsxdhsmChannel(){
        super(Channel.CHANGJIEJSXDHSM, SystemCfg.getInstance().getChannel().getChangjiejsxdhsm());
    }

    public static final ChangjieJsxdhsmChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getChangjiejsxdhsm());
        return ChannelHolder.CHANNEL;
    }


}
