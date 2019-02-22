package com.yitkeji.songshushenghuo.vo.req.admin;

import com.yitkeji.songshushenghuo.vo.req.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WithdrawListReq extends PageReq {

    @ApiModelProperty("账号")
    private String userName;

    @ApiModelProperty("申请提现状态")
    private Integer status;
}
