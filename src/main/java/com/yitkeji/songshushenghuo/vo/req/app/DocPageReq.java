package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.vo.enums.DocType;
import com.yitkeji.songshushenghuo.vo.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class DocPageReq extends PageReq {

    @ApiModelProperty(value = "文章类型（不传或者传空查詢所有）", example = "NORMAL")
    private DocType type;
}
