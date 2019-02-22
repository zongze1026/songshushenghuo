package com.yitkeji.songshushenghuo.vo.req.admin;

import com.yitkeji.songshushenghuo.vo.enums.StatisticsUnit;
import com.yitkeji.songshushenghuo.vo.req.CondReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class CountChartReq extends CondReq {

    @ApiModelProperty("统计单位")
    private StatisticsUnit statisticsUnit;

}
