package com.yitkeji.songshushenghuo.vo.res.agent;

import com.yitkeji.songshushenghuo.vo.enums.PlanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class PlanRes{

    @ApiModelProperty("计划ID")
    private Long planId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userRealName;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("银行卡ID")
    private Long cardId;

    @ApiModelProperty("卡号")
    private String cardNo;

    @ApiModelProperty("消费金额")
    private int money;

    @ApiModelProperty("还款金额")
    private int repayMoney;

    @ApiModelProperty("计划执行时间")
    private Date runTime;

    @ApiModelProperty("实际执行时间")
    private Date runTimeReal;

    @ApiModelProperty("撤销时间")
    private Date removeTime;

    @ApiModelProperty("计划类型")
    private PlanType type;

    @ApiModelProperty("计划状态")
    private int status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date lastupdatetime;

    @ApiModelProperty("备注")
    private String comment;
}