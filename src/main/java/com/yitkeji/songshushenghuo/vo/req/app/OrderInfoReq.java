package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class OrderInfoReq extends BaseAppReq {

    @ApiModelProperty("订单记录ID")
    private String orderId;

}
