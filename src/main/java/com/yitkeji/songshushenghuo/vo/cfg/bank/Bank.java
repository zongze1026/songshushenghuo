package com.yitkeji.songshushenghuo.vo.cfg.bank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class Bank {

    @ApiModelProperty("银行编号")
    private String id;

    @ApiModelProperty("快付通行别代码")
    private String kftCode;

    @ApiModelProperty("银行简码")
    private String code;

    @ApiModelProperty("联行号")
    private String union = "000000000000";

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("LOGO主色调")
    private String color;

    @ApiModelProperty("银行LOGO图标")
    private String icon;

    @ApiModelProperty("银行卡图片")
    private String image;

}
