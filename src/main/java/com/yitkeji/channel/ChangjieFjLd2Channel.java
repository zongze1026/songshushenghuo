package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieFjLd2Channel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieFjLd2Channel channel = new ChangjieFjLd2Channel();
    }

    private ChangjieFjLd2Channel(){
        super(Channel.CHANGJIEFJLD2, SystemCfg.getInstance().getChannel().getChangjiefjld2());
    }

    public static final ChangjieFjLd2Channel getInstance(){
        ChannelHolder.channel.initCfg(SystemCfg.getInstance().getChannel().getChangjiefjld2());
        return ChannelHolder.channel;
    }

}
