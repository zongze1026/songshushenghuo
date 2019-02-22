package com.yitkeji.songshushenghuo.vo.res;

import com.yitkeji.songshushenghuo.vo.model.Statistics;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class CountRes {

    @ApiModelProperty("派发分润")
    private long sumCommission;

    @ApiModelProperty("收款分润")
    private long sumCommissionCash;

    @ApiModelProperty("升级Vip奖励")
    private long sumCommissionUpVip;

    @ApiModelProperty("推荐奖励")
    private long sumCommissionReferral;

    @ApiModelProperty("推荐奖励-实名")
    private long sumCommissionReferralAuth;

    @ApiModelProperty("推荐奖励-收款")
    private long sumCommissionReferralCash;

    @ApiModelProperty("收款总额")
    private long sumCash;

    @ApiModelProperty("VIP充值")
    private long sumUpVip;

    @ApiModelProperty("总用户")
    private long cUser;

    @ApiModelProperty("普通用户")
    private long cVip1;

    @ApiModelProperty("高级会员")
    private long cVip2;

    @ApiModelProperty("服务中心")
    private long cVip3;

    @ApiModelProperty("运营中心")
    private long cVip4;

    public CountRes(){}

    public CountRes(Statistics statistics){
        this.sumCommission = statistics.getCommissionSum();
        this.sumCommissionCash = statistics.getCommissionCashSum();
        this.sumCommissionUpVip = statistics.getCommissionUpvipSum();

        this.sumCash = statistics.getOrderCashSum();
        this.sumUpVip = statistics.getOrderUpvipSum();

        this.cUser = statistics.getUserCount();
        this.cVip1 = statistics.getUserUpvip1Count();
        this.cVip2 = statistics.getUserUpvip2Count();
        this.cVip3 = statistics.getUserUpvip3Count();
        this.cVip4 = statistics.getUserUpvip4Count();
    }
}
