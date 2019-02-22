package com.yitkeji.channel.inter;

import com.yitkeji.channel.model.ChannelParams;
import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.vo.cfg.channel.BaseChannelCfg;

public interface CashChannel <T extends BaseChannelCfg> extends ChannelInterface<T> {
    Object cashFirst(ChannelParams params);
    Object cash(ChannelParams params) throws BaseException;
}
