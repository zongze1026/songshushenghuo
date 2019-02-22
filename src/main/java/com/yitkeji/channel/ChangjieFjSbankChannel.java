package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieFjSbankChannel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieFjSbankChannel channel = new ChangjieFjSbankChannel();
    }

    private ChangjieFjSbankChannel(){
        super(Channel.CHANGJIEFJSBANK, SystemCfg.getInstance().getChannel().getChangjiefjsbank());
    }

    public static final ChangjieFjSbankChannel getInstance(){
        ChannelHolder.channel.initCfg(SystemCfg.getInstance().getChannel().getChangjiefjsbank());
        return ChannelHolder.channel;
    }

}
