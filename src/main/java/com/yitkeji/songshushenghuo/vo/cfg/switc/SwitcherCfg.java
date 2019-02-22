package com.yitkeji.songshushenghuo.vo.cfg.switc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class SwitcherCfg {

    @ApiModelProperty("提现申请开关")
    private Boolean appLyWithdraw;

    @ApiModelProperty("秘钥")
    private String apiKey;

    @ApiModelProperty("邀请码注册，1是，0否")
    private Long referralRegist;

    @ApiModelProperty("卡测评价格（分）")
    private Integer cardMeasurePrice;

    @ApiModelProperty("计划每日最高笔数")
    private Integer maxDayPlan;

    @ApiModelProperty("验卡金额（分）")
    private Integer validCardMoney;

    @ApiModelProperty("计划单笔最低金额（分）")
    private Integer minPlanMoney;

    @ApiModelProperty("计划单笔最高金额（分）")
    private Integer maxPlanMoney;

    @ApiModelProperty("最低收款金额（分）")
    private Integer minCashMoney;

    @ApiModelProperty("最高收款金额（分）")
    private Integer maxCashMoney;

    @ApiModelProperty("最低提现金额（分）")
    private Integer minWithdrawMoney;

    @ApiModelProperty("最低代理商提现金额（分）")
    private Integer minAgentWithdrawMoney;

    @ApiModelProperty("最高提现金额（分）")
    private Integer maxWithdrawMoney;

    @ApiModelProperty("最高代理商提现金额（分）")
    private Integer maxAgentWithdrawMoney;

    @ApiModelProperty("新手实名、收款满1000奖励金额（分）")
    private Integer noviceAuthAndCashMoney;

    @ApiModelProperty("交易满奖金额（分）")
    private Integer orderFullRewardMoney;

    @ApiModelProperty("交易满自动升级金额（分）")
    private Integer orderFullUpVipMoney;

    @ApiModelProperty("订单有效期")
    private Integer expiredOrder;  // 订单过期时间

    @ApiModelProperty("锁定计划的有效期")
    private Integer expiredPlan;  // 订单过期时间

    @ApiModelProperty("创建计划时间段")
    private TimeRange createPlanTimeRange;

    @ApiModelProperty("执行计划时间段")
    private TimeRange runPlanTimeRange;

    @ApiModelProperty("收款时间段")
    private TimeRange cashTimeRange;

    @ApiModelProperty("添加银行卡时间段")
    private TimeRange addCardTimeRange;

}
