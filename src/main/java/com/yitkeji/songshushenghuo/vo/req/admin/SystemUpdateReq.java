package com.yitkeji.songshushenghuo.vo.req.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class SystemUpdateReq {

    @ApiModelProperty("配置Id")
    private Long systemId;

    @ApiModelProperty("配置名称")
    private String comment;

    @ApiModelProperty("配置内容")
    private String value;
}
