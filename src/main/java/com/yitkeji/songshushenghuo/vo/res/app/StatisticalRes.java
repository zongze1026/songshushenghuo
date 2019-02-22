package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel
public class StatisticalRes {

    @ApiModelProperty("统计用户集合")
    private List<CountUserRes> countUsers;

    @ApiModelProperty("统计收款金额集合")
    private List<SumCashRes> sumCashs;
}
