package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieFjLdChannel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieFjLdChannel channel = new ChangjieFjLdChannel();
    }

    private ChangjieFjLdChannel(){
        super(Channel.CHANGJIEJSLD, SystemCfg.getInstance().getChannel().getChangjiejsld1());
    }

    public static final ChangjieFjLdChannel getInstance(){
        ChannelHolder.channel.initCfg(SystemCfg.getInstance().getChannel().getChangjiejsld1());
        return ChannelHolder.channel;
    }

}
