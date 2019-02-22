package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieJsLd2Channel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieJsLd2Channel channel = new ChangjieJsLd2Channel();
    }

    private ChangjieJsLd2Channel(){
        super(Channel.CHANGJIEJSLD2, SystemCfg.getInstance().getChannel().getChangjiejsld2());
    }

    public static final ChangjieJsLd2Channel getInstance(){
        ChannelHolder.channel.initCfg(SystemCfg.getInstance().getChannel().getChangjiejsld2());
        return ChannelHolder.channel;
    }

}
