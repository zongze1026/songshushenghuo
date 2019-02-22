package com.yitkeji.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public class ChangjieJsSlChannel extends ChangjieJsBaseChannel {


    private static final class ChannelHolder{
        private static final ChangjieJsSlChannel channel = new ChangjieJsSlChannel();
    }

    private ChangjieJsSlChannel(){
        super(Channel.CHANGJIEJSSL, SystemCfg.getInstance().getChannel().getChangjiejssl());
    }

    public static final ChangjieJsSlChannel getInstance(){
        ChannelHolder.channel.initCfg(SystemCfg.getInstance().getChannel().getChangjiejssl());
        return ChannelHolder.channel;
    }

}
