package com.yitkeji.songshushenghuo.vo.req.app;


import com.yitkeji.songshushenghuo.vo.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class DocInfoReq extends PageReq {

    @ApiModelProperty(value = "文章ID", example = "1", required = true)
    private long docId;
}
