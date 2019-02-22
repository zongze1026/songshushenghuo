package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieJsLdChannel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieJsLdChannel channel = new ChangjieJsLdChannel();
    }

    private ChangjieJsLdChannel(){
        super(Channel.CHANGJIEJSLD, SystemCfg.getInstance().getChannel().getChangjiejsld1());
    }

    public static final ChangjieJsLdChannel getInstance(){
        ChannelHolder.channel.initCfg(SystemCfg.getInstance().getChannel().getChangjiejsld1());
        return ChannelHolder.channel;
    }

}
