package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ApiModel
public class YunshanfuAreaRes {

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

    @ApiModelProperty("层级")
    private List<YunshanfuAreaRes> cities = new ArrayList<>();
}