package com.yitkeji.songshushenghuo.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserRate {

    @ApiModelProperty("代还费率")
    private double rate;

    @ApiModelProperty("代还固定手续费（分）")
    private double fixedAmount;

    @ApiModelProperty("收款费率")
    private double cashRate;

    @ApiModelProperty("收款固定手续费（分）")
    private double cashFixedAmount;

    @ApiModelProperty("提现费率")
    private double withdrawRate;

    @ApiModelProperty("提现固定手续费（分）")
    private double withdrawFixedAmount;

    @ApiModelProperty("云闪付费率")
    private double yunshanfuRate;

    @ApiModelProperty("云闪付固定手续费（分）")
    private double yunshanfuFixedAmount;

}
