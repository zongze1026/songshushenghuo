package com.yitkeji.channel.inter;

import com.yitkeji.songshushenghuo.vo.cfg.channel.BaseChannelCfg;
import com.yitkeji.songshushenghuo.vo.model.Order;

public interface PayChannel <T extends BaseChannelCfg> extends ChannelInterface<T> {

    Object upvip(Order order, Object... params);

    Object cardMeasure(Order order, Object... params);
}
