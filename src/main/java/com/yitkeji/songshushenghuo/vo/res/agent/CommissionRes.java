package com.yitkeji.songshushenghuo.vo.res.agent;

import com.yitkeji.songshushenghuo.vo.enums.CommissionLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class CommissionRes {


    @ApiModelProperty("分润ID")
    private Long commissionId;

    @ApiModelProperty("触发用户ID")
    private Long sourceUserId;

    @ApiModelProperty("触发用户姓名")
    private String sourceUserRealName;

    @ApiModelProperty("触发用户账号")
    private String sourceUserName;

    @ApiModelProperty("目标代理ID")
    private Long targetAgentId;

    @ApiModelProperty("目标代理姓名")
    private String targetAgentName;

    @ApiModelProperty("目标代理账号")
    private String targetAgentUserName;

    @ApiModelProperty("目标用户ID")
    private Long targetUserId;

    @ApiModelProperty("目标用户姓名")
    private String targetUserRealName;

    @ApiModelProperty("目标用户账号")
    private String targetUserName;

    @ApiModelProperty("分润层级")
    private CommissionLevel level;

    @ApiModelProperty("分润类型")
    private Integer type;

    @ApiModelProperty("触发金额")
    private int orderMoney;

    @ApiModelProperty("触发订单号")
    private String orderNo;

    @ApiModelProperty("分润金额")
    private int money;

    @ApiModelProperty("创建时间")
    private Date createTime;

}