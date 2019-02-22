package com.yitkeji.songshushenghuo.vo.req.admin;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelReq {
    private long userId;
    private long cardId;
    private int money;
    private long orderId;
    private String channel;

}
