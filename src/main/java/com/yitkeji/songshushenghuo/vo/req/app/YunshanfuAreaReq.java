package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class YunshanfuAreaReq {

    @ApiModelProperty("省份编码，不传默认获取所有省份")
    private Integer provinceCode;
}
