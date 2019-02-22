package com.yitkeji.channel.inter;

import com.yitkeji.channel.model.Position;
import com.yitkeji.songshushenghuo.vo.cfg.channel.BaseChannelCfg;

public interface MapChannel <T extends BaseChannelCfg> extends ChannelInterface<T> {
    Position position(String ip);
}
