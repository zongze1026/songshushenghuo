package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table("tb_statistics_week")
public class StatisticsWeek {

    @PrimaryKey("statistics_week_id")
    private Long statisticsWeekId;
    private Long userId;
    private long commissionSum;
    private long commissionCount;
    private long commissionCashSum;
    private long commissionCashCount;
    private long commissionUpvipSum;
    private long commissionUpvipCount;
    private long commissionRegistSum;
    private long commissionRegistCount;
    private long commissionNoviceAuthSum;
    private long commissionNoviceAuthCount;
    private long commissionNoviceCashSum;
    private long commissionNoviceCashCount;
    private long orderSum;
    private long orderCount;
    private long orderCashSum;
    private long orderCashCount;
    private long orderUpvipSum;
    private long orderUpvipCount;
    private long orderWithdrawSum;
    private long orderWithdrawCount;
    private long userCount;
    private long userUpvip1Count;
    private long userUpvip2Count;
    private long userUpvip3Count;
    private long userUpvip4Count;
    private int yearWeek;
}