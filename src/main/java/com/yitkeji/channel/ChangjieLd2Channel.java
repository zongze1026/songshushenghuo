package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieLd2Channel extends ChangjieNewBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieLd2Channel CHANNEL = new ChangjieLd2Channel();
    }

    private ChangjieLd2Channel(){
        super(Channel.CHANGJIELD2, SystemCfg.getInstance().getChannel().getChangjield2());
    }

    public static final ChangjieLd2Channel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getChangjield2());
        return ChannelHolder.CHANNEL;
    }

}
