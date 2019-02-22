package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieSlChannel extends ChangjieNewBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieSlChannel CHANNEL = new ChangjieSlChannel();
    }

    private ChangjieSlChannel(){
        super(Channel.CHANGJIESL, SystemCfg.getInstance().getChannel().getChangjiesl());
    }

    public static final ChangjieSlChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getChangjiesl());
        return ChannelHolder.CHANNEL;
    }

}
