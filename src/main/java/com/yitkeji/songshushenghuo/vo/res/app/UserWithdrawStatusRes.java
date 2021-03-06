package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserWithdrawStatusRes {

    @ApiModelProperty("提现状态描述")
    private String desc;

    @ApiModelProperty("状态码")
    private int code;
}
