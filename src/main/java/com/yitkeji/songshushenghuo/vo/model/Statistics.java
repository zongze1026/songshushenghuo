package com.yitkeji.songshushenghuo.vo.model;

import com.alibaba.fastjson.JSON;
import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Setter
@Getter
@Table("tb_statistics")
public class Statistics {
    public Statistics(){}

    public Statistics(StatisticsSum statisticsSum){
        if(statisticsSum != null){
            BeanUtils.copyProperties(JSON.parseObject(JSON.toJSONString(statisticsSum), Statistics.class), this);
        }
    }

    @PrimaryKey("statistics_id")
    private Long statisticsId;
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
    private long commissionReferralAuthCount;
    private long commissionReferralAuthSum;
    private long commissionReferralCashCount;
    private long commissionReferralCashSum;
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
    private Date createTime;


}