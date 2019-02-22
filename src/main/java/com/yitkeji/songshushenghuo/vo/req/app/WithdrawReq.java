package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class WithdrawReq extends BaseAppReq{

    @ApiModelProperty("提现金额（分）")
    private Integer money;

    @ApiModelProperty("入账卡")
    private Long tcardId;

    @ApiModelProperty(value = "短信验证码", allowEmptyValue = true)
    private String code;

}
