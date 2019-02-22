package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieFjSlChannel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieFjSlChannel channel = new ChangjieFjSlChannel();
    }

    private ChangjieFjSlChannel(){
        super(Channel.CHANGJIEFJSL, SystemCfg.getInstance().getChannel().getChangjiefjsl());
    }

    public static final ChangjieFjSlChannel getInstance(){
        ChannelHolder.channel.initCfg(SystemCfg.getInstance().getChannel().getChangjiefjsl());
        return ChannelHolder.channel;
    }

}
