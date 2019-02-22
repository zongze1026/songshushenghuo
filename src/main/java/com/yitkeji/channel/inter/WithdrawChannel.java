package com.yitkeji.channel.inter;

import com.yitkeji.songshushenghuo.vo.cfg.channel.BaseChannelCfg;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;

public interface WithdrawChannel <T extends BaseChannelCfg> extends ChannelInterface<T> {

    Order withPayForCustomer(User user, Card debitCard, Integer money);
}
