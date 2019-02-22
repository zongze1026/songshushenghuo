package com.yitkeji.songshushenghuo.vo.res.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel("增量统计信息")
public class StatisticsRes {

    @ApiModelProperty("统计ID")
    private Long statisticsId;

    @ApiModelProperty("分润总金额")
    private long commissionSum;

    @ApiModelProperty("分润总笔数")
    private long commissionCount;

    @ApiModelProperty("分润总金额（收款）")
    private long commissionCashSum;

    @ApiModelProperty("分润总笔数（收款）")
    private long commissionCashCount;

    @ApiModelProperty("分润总金额（升级VIP）")
    private long commissionUpvipSum;

    @ApiModelProperty("分润总笔数（升级VIP）")
    private long commissionUpvipCount;

    @ApiModelProperty("分润总金额（分享奖励）")
    private long commissionRegistSum;

    @ApiModelProperty("分润总笔数（分享奖励）")
    private long commissionRegistCount;

    @ApiModelProperty("分润总笔数（推荐奖励）")
    private long commissionReferralAuthCount;

    @ApiModelProperty("分润总金额（推荐奖励）")
    private long commissionReferralAuthSum;

    @ApiModelProperty("分润总笔数（推荐收款）")
    private long commissionReferralCashCount;

    @ApiModelProperty("分润总金额（推荐收款）")
    private long commissionReferralCashSum;

    @ApiModelProperty("分润总金额（新手实名）")
    private long commissionNoviceAuthSum;

    @ApiModelProperty("分润总笔数（新手实名）")
    private long commissionNoviceAuthCount;

    @ApiModelProperty("分润总金额（新手收款）")
    private long commissionNoviceCashSum;

    @ApiModelProperty("分润总笔数（新手收款）")
    private long commissionNoviceCashCount;

    @ApiModelProperty("交易总金额")
    private long orderSum;

    @ApiModelProperty("交易总笔数")
    private long orderCount;

    @ApiModelProperty("交易总金额（收款）")
    private long orderCashSum;

    @ApiModelProperty("交易总笔数（收款）")
    private long orderCashCount;

    @ApiModelProperty("交易总金额（升级VIP）")
    private long orderUpvipSum;

    @ApiModelProperty("交易总笔数（升级VIP）")
    private long orderUpvipCount;

    @ApiModelProperty("交易总金额（余额提现）")
    private long orderWithdrawSum;

    @ApiModelProperty("交易总笔数（余额提现）")
    private long orderWithdrawCount;

    @ApiModelProperty("注册用户总数")
    private long userCount;

    @ApiModelProperty("升级VIP总数")
    private long userUpvip1Count;

    @ApiModelProperty("升级VIP总数")
    private long userUpvip2Count;

    @ApiModelProperty("升级VIP总数")
    private long userUpvip3Count;

    @ApiModelProperty("升级VIP总数")
    private long userUpvip4Count;

    @ApiModelProperty("记录时间")
    private Date createTime;

    @ApiModelProperty(value = "年份 + 年度第几周", example = "201904")
    private int yearWeek;

    @ApiModelProperty(value = "年份 + 年度第几个月", example = "201901")
    private int yearMonth;


}