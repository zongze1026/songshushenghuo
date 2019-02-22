package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class YunketongLdChannel extends YunketongBaseChannel {


    private static final class ChannelHolder{
        private static final YunketongLdChannel CHANNEL = new YunketongLdChannel();
    }

    private YunketongLdChannel(){
        super(Channel.YUNKETONGLD, SystemCfg.getInstance().getChannel().getYunketongld());
    }

    public static final YunketongLdChannel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getYunketongld());
        return ChannelHolder.CHANNEL;
    }

}
