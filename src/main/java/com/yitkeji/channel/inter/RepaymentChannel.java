package com.yitkeji.channel.inter;

import com.yitkeji.channel.model.ChannelParams;
import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.vo.cfg.channel.BaseChannelCfg;
import com.yitkeji.songshushenghuo.vo.model.Order;

public interface RepaymentChannel <T extends BaseChannelCfg> extends ChannelInterface<T> {

    Object repaymentFirst(ChannelParams params) throws BaseException;
    Order repayment(ChannelParams params) throws BaseException;
}
