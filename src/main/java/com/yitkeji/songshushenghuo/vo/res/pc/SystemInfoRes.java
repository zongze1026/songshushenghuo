package com.yitkeji.songshushenghuo.vo.res.pc;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Setter
@Getter
public class SystemInfoRes {

    @ApiModelProperty("APP基本信息 appinfo")
    private AppInfoRes appinfo;

    @ApiModelProperty("系统开关 switcher")
    private SwitcherCfgRes switcher;

    @ApiModelProperty("VIP等级 vip")
    private VipCfgRes vip;

    public SystemInfoRes(){
        SystemCfg cfg = SystemCfg.getInstance();
        this.appinfo = new AppInfoRes(cfg.getAppinfo());
        this.switcher = new SwitcherCfgRes(cfg.getSwitcher());
        this.vip = new VipCfgRes(cfg.getVip());
    }
}
