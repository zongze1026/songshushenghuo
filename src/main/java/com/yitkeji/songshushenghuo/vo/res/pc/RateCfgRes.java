package com.yitkeji.songshushenghuo.vo.res.pc;

import com.yitkeji.songshushenghuo.vo.cfg.rate.RateCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class RateCfgRes {

    @ApiModelProperty("收款费率")
    private RateElementRes cash;

    @ApiModelProperty("提现费率")
    private RateElementRes withdraw;

    public RateCfgRes(RateCfg cfg){
        this.cash = new RateElementRes(cfg.getCash());
        this.withdraw = new RateElementRes(cfg.getWithdraw());
    }
}
