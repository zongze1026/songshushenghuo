package com.yitkeji.songshushenghuo.vo.req.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class UpdateWithdrawReq {

    @ApiModelProperty("提现Id")
    private long userWithdrawId;

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("银行卡Id")
    private Long cardId;

    @ApiModelProperty("账单Id")
    private Long orderId;

    @ApiModelProperty("提现金额")
    private int money;

    @ApiModelProperty("提现状态")
    private int status;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("申请时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date lastupdatetime;
}
