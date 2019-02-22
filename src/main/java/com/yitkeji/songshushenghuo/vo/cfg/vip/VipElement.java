package com.yitkeji.songshushenghuo.vo.cfg.vip;

import com.yitkeji.songshushenghuo.vo.cfg.rate.RateCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class VipElement extends RateElement {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("价格（分）")
    private Double price;

    @ApiModelProperty("有效期（天）")
    private int periodOfValidity;

    @ApiModelProperty("自动推荐升级数量")
    private int autoUpgradeNum;

    @Deprecated
    @ApiModelProperty("交易费率（已弃用，请参考defaultRate字段）")
    private Double rate;

    @Deprecated
    @ApiModelProperty("固定手续费（分）（已弃用，请参考defaultRate字段）")
    private Long fixedAmount;

    @Deprecated
    @ApiModelProperty("收款费率（已弃用，请参考defaultRate字段）")
    private Double cashRate;

    @Deprecated
    @ApiModelProperty("收款固定手续费（分）（已弃用，请参考defaultRate字段）")
    private Long cashFixedAmount;

    @ApiModelProperty("vip图标")
    private String icon;

    @ApiModelProperty("分润配置")
    private VipShare share;

    @ApiModelProperty("手续费配置")
    private RateCfg defaultRate;

    @ApiModelProperty("云闪付手续费配置")
    private RateCfg yunshanfuRate;

}
