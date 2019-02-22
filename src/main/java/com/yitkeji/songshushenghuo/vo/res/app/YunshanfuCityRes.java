package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@ApiModel
public class YunshanfuCityRes {
    @ApiModelProperty("地区ID")
    private Long areaId;

    @ApiModelProperty("地区编码")
    private Integer areaCode;

    @ApiModelProperty("地区名称")
    private String areaName;

    @ApiModelProperty("省份编码")
    private Integer provinceCode;

    @ApiModelProperty("层级")
    private Integer level;
}
