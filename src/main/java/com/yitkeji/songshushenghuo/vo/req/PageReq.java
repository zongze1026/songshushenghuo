package com.yitkeji.songshushenghuo.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class PageReq extends CondReq {

    @ApiModelProperty(value = "当前页码", allowEmptyValue = true)
    private int npage;

    @ApiModelProperty(value = "总条数", allowEmptyValue = true)
    private long total;

    @ApiModelProperty(value = "每页条数", allowEmptyValue = true)
    private int pageSize = 20;
}
