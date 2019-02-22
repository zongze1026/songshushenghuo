package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class SumCashRes {

    @ApiModelProperty("日期")
    private String sumDate;

    @ApiModelProperty("收款金额")
    private int sumCash;

}
