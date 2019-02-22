package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserWithdrawPagReq extends BaseAppPageReq {

    @ApiModelProperty("申请提现状态")
    private Integer status;
}
