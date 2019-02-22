package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class CashReq extends BaseAppReq{

    @ApiModelProperty("出账卡")
    private Long scardId;

    @ApiModelProperty("入账卡")
    private Long tcardId;

    @ApiModelProperty("收款金额（分）")
    private Integer money;

    @ApiModelProperty("短信验证码")
    private String code;

    @ApiModelProperty("订单ID")
    private String orderId;

    @ApiModelProperty("请求号")
    private String requestNo;

}
