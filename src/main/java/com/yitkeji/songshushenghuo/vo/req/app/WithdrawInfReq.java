package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class WithdrawInfReq extends BaseAppReq {

    @ApiModelProperty("提现Id")
    private long userWithdrawId;
}
