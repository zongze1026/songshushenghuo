package com.yitkeji.songshushenghuo.vo.res.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class TbSystemRes {

    @ApiModelProperty("主键ID")
    private Long systemId;

    @ApiModelProperty("类型")
    private String key;

    @ApiModelProperty("内容")
    private String value;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("描述")
    private String comment;

    @ApiModelProperty("权限")
    private Integer readOnly;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("更新时间")
    private Date lastupdatetime;
}
