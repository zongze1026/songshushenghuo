package com.yitkeji.songshushenghuo.vo.res.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class OrderRes {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("通道")
    private String channel;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("流水号")
    private String trackingNo;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户身份证号")
    private String idcard;

    @ApiModelProperty("用户姓名")
    private String userRealName;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("代理商ID")
    private Long agentId;

    @ApiModelProperty("代理商")
    private String agentName;

    @ApiModelProperty("代理商账号")
    private String agentUserName;

    @ApiModelProperty("支付卡")
    private Long sourceCardId;

    @ApiModelProperty("支付卡卡号")
    private String sourceCardNo;

    @ApiModelProperty("支付卡名称")
    private String sourceCardBank;

    @ApiModelProperty("入账卡")
    private Long targetCardId;

    @ApiModelProperty("入账卡卡号")
    private String targetCardNo;

    @ApiModelProperty("入账卡名称")
    private String targetCardBank;

    @ApiModelProperty("订单类型")
    private int type;

    @ApiModelProperty("金额")
    private int money = 0;

    @ApiModelProperty("手续费")
    private int rateMoney;

    @ApiModelProperty("到账金额")
    private int arriveMoney;

    @ApiModelProperty("订单描述")
    private String desc;

    @ApiModelProperty("订单状态")
    private String status;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date lastupdatetime;

}
