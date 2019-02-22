package com.yitkeji.songshushenghuo.vo.cfg.rate;

import com.yitkeji.songshushenghuo.vo.cfg.vip.RateElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class RateCfg {

    @ApiModelProperty("代还费率")
    private RateElement repayment;

    @ApiModelProperty("收款费率")
    private RateElement cash;

    @ApiModelProperty("提现费率")
    private RateElement withdraw;
}
