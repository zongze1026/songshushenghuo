package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.vo.enums.Channel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UpvipReq extends BaseAppReq{

    @ApiModelProperty("VIP等级")
    private int vip;

    @ApiModelProperty("支付通道")
    private Channel channel;
}
