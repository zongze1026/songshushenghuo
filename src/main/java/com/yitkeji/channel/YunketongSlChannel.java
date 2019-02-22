package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class YunketongSlChannel extends YunketongBaseChannel {


    private static final class ChannelHolder{
        private static final YunketongSlChannel CHANNEL = new YunketongSlChannel();
    }

    private YunketongSlChannel(){
        super(Channel.YUNKETONGSL, SystemCfg.getInstance().getChannel().getYunketongsl());
    }

    public static final YunketongSlChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getYunketongsl());
        return ChannelHolder.CHANNEL;
    }

}
