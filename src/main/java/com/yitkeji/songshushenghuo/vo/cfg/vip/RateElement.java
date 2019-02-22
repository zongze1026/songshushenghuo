package com.yitkeji.songshushenghuo.vo.cfg.vip;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class RateElement {


    @ApiModelProperty("交易费率")
    private Double rate;

    @ApiModelProperty("固定手续费（分）")
    private Long fixedAmount;

}
