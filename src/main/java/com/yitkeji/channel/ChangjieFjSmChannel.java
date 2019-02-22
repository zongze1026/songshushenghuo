package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieFjSmChannel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieFjSmChannel CHANNEL = new ChangjieFjSmChannel();
    }

    private ChangjieFjSmChannel(){
        super(Channel.CHANGJIEFJSM, SystemCfg.getInstance().getChannel().getChangjiefjsm());
    }

    public static final ChangjieFjSmChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getChangjiefjsm());
        return ChannelHolder.CHANNEL;
    }


}
