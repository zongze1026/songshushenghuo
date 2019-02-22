package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class BankListRes {

    @ApiModelProperty("银行编号")
    private String id;

    @ApiModelProperty("银行简码")
    private String code;

    @ApiModelProperty("联行号")
    private String union;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行LOGO图标")
    private String icon;

}
