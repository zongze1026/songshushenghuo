package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class YunketongLd2Channel extends YunketongBaseChannel {


    private static final class ChannelHolder{
        private static final YunketongLd2Channel CHANNEL = new YunketongLd2Channel();
    }

    private YunketongLd2Channel(){
        super(Channel.YUNKETONGLD2, SystemCfg.getInstance().getChannel().getYunketongld2());
    }

    public static final YunketongLd2Channel getInstance(){
        ChannelHolder.CHANNEL.initCfg(SystemCfg.getInstance().getChannel().getYunketongld2());
        return ChannelHolder.CHANNEL;
    }

}
