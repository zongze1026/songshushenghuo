package com.yitkeji.songshushenghuo.vo.res.pc;

import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.vo.cfg.switc.SwitcherCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Setter
@Getter
public class SwitcherCfgRes{

    @ApiModelProperty("最低收款金额（分）")
    private Integer minCashMoney;

    @ApiModelProperty("最高收款金额（分）")
    private Integer maxCashMoney;

    @ApiModelProperty("最低提现金额（分）")
    private Integer minWithdrawMoney;

    @ApiModelProperty("最高提现金额（分）")
    private Integer maxWithdrawMoney;

    public SwitcherCfgRes(SwitcherCfg cfg){
        ObjectUtil.convert(cfg, this);
    }
}