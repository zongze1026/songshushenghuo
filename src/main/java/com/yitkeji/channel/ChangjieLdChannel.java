package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieLdChannel extends ChangjieNewBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieLdChannel CHANNEL = new ChangjieLdChannel();
    }

    private ChangjieLdChannel(){
        super(Channel.CHANGJIELD, SystemCfg.getInstance().getChannel().getChangjield1());
    }

    public static final ChangjieLdChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getChangjield1());
        return ChannelHolder.CHANNEL;
    }

}
