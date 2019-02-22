package com.yitkeji.songshushenghuo.vo.req.pc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class SystemInfoReq {

    @ApiModelProperty("配置名称")
    private String name;
}
