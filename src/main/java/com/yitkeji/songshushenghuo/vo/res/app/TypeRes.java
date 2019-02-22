package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class TypeRes {

    @ApiModelProperty("类型描述")
    private String desc;

    @ApiModelProperty("类型码")
    private int code;
}
