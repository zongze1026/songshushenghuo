package com.yitkeji.songshushenghuo.vo.res.pc;

import com.yitkeji.songshushenghuo.vo.cfg.vip.RateElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class RateElementRes {


    @ApiModelProperty("交易费率")
    private Double rate;

    @ApiModelProperty("固定手续费（分）")
    private Long fixedAmount;

    public RateElementRes(RateElement element){
        this.rate = element.getRate();
        this.fixedAmount = element.getFixedAmount();
    }
}
