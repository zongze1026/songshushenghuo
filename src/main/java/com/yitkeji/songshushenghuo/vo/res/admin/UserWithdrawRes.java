package com.yitkeji.songshushenghuo.vo.res.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class UserWithdrawRes {

    @ApiModelProperty("申请ID")
    private Long userWithdrawId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userRealName;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("银行卡ID")
    private Long cardId;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("金额")
    private int money;

    @ApiModelProperty("状态")
    private int status;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date lastupdatetime;
}
