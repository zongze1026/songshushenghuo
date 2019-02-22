package com.yitkeji.songshushenghuo.vo.res.admin;

import com.yitkeji.songshushenghuo.vo.enums.DocType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class DocRes {

    @ApiModelProperty("文章Id")
    private Long docId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("模块")
    private String module;

    @ApiModelProperty("图标")
    private String image;

    @ApiModelProperty("分享背景色")
    private String color;

    @ApiModelProperty("文章详情")
    private String desc;

    @ApiModelProperty("URL")
    private String url;

    @ApiModelProperty("文章类型")
    private DocType type;

    @ApiModelProperty("文章状态")
    private int status;

    @ApiModelProperty("开关")
    private boolean readOnly;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date lastupdatetime;

    @ApiModelProperty("文章内容")
    private String content;
}
