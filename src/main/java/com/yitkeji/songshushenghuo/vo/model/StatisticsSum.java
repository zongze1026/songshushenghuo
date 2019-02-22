package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Table("tb_statistics")
public class StatisticsSum{

    @PrimaryKey("statistics_id")
    private Long statisticsId;
    private Long userId;
    private BigDecimal commissionSum;
    private BigDecimal commissionCount;
    private BigDecimal commissionCashSum;
    private BigDecimal commissionCashCount;
    private BigDecimal commissionUpvipSum;
    private BigDecimal commissionUpvipCount;
    private BigDecimal commissionRegistSum;
    private BigDecimal commissionRegistCount;
    private BigDecimal commissionNoviceAuthSum;
    private BigDecimal commissionNoviceAuthCount;
    private BigDecimal commissionNoviceCashSum;
    private BigDecimal commissionNoviceCashCount;
    private BigDecimal commissionReferralAuthCount;
    private BigDecimal commissionReferralAuthSum;
    private BigDecimal commissionReferralCashCount;
    private BigDecimal commissionReferralCashSum;
    private BigDecimal orderSum;
    private BigDecimal orderCount;
    private BigDecimal orderCashSum;
    private BigDecimal orderCashCount;
    private BigDecimal orderUpvipSum;
    private BigDecimal orderUpvipCount;
    private BigDecimal orderWithdrawSum;
    private BigDecimal orderWithdrawCount;
    private BigDecimal userCount;
    private BigDecimal userUpvip1Count;
    private BigDecimal userUpvip2Count;
    private BigDecimal userUpvip3Count;
    private BigDecimal userUpvip4Count;
    private Date createTime;

}