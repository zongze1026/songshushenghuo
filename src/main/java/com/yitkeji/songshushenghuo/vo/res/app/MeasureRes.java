package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MeasureRes {

    @ApiModelProperty("序列号")
    private String serialNumber;

    @ApiModelProperty("测评访问地址")
    private String measureUrl;
}
