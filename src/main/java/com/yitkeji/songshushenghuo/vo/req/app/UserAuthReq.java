package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserAuthReq extends BaseAppReq{

    @ApiModelProperty("商户订单号")
    private String partnerOrderId;
}
