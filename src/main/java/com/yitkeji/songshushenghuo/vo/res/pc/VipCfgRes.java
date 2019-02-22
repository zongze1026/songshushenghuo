package com.yitkeji.songshushenghuo.vo.res.pc;

import com.yitkeji.songshushenghuo.vo.cfg.vip.VipCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class VipCfgRes {


    @ApiModelProperty("VIP1")
    private VipElementRes vip1;

    @ApiModelProperty("VIP2")
    private VipElementRes vip2;

    @ApiModelProperty("VIP3")
    private VipElementRes vip3;

    @ApiModelProperty("VIP4")
    private VipElementRes vip4;

    public VipCfgRes(VipCfg cfg){
        this.vip1 = new VipElementRes(cfg.getVip1());
        this.vip2 = new VipElementRes(cfg.getVip2());
        this.vip3 = new VipElementRes(cfg.getVip3());
        this.vip4 = new VipElementRes(cfg.getVip4());

    }
}
