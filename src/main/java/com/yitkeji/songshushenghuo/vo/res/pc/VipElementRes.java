package com.yitkeji.songshushenghuo.vo.res.pc;

import com.yitkeji.songshushenghuo.vo.cfg.vip.VipElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class VipElementRes{

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("价格（分）")
    private Double price;

    @ApiModelProperty("自动推荐升级数量")
    private Integer autoUpgradeNum;

    @ApiModelProperty("vip图标")
    private String icon;

    @ApiModelProperty("手续费配置")
    private RateCfgRes defaultRate;

    public VipElementRes(VipElement element){
        this.name = element.getName();
        this.price = element.getPrice();
        this.autoUpgradeNum = element.getAutoUpgradeNum();
        this.icon = element.getIcon();
        this.defaultRate = new RateCfgRes(element.getDefaultRate());
    }

}
