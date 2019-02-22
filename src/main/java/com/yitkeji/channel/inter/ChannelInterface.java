package com.yitkeji.channel.inter;

import com.yitkeji.songshushenghuo.vo.cfg.channel.BaseChannelCfg;
import com.yitkeji.songshushenghuo.vo.enums.Channel;

public interface ChannelInterface<T extends BaseChannelCfg> {
    T getCfg();
    Channel getChannel();
}
