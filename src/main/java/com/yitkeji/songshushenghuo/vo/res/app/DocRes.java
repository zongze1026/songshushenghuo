package com.yitkeji.songshushenghuo.vo.res.app;

import com.yitkeji.songshushenghuo.vo.enums.DocType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ApiModel
public class DocRes{

    @ApiModelProperty("文档ID")
    private Long docId;

    @ApiModelProperty("文档标题")
    private String title;

    @ApiModelProperty("模块")
    private String module;

    @ApiModelProperty("文档图片")
    private String image;

    @ApiModelProperty("背景色")
    private String color;

    @ApiModelProperty("文档描述")
    private String desc;

    @ApiModelProperty("链接地址")
    private String url;

    @ApiModelProperty("文档类型")
    private DocType type;

    @ApiModelProperty("安卓跳转网页标识")
    private String androidPageLabel;

    @ApiModelProperty("IOS跳转网页标识")
    private String iosPageLabel;

    @ApiModelProperty("热门银行卡")
    private List<DocRes> hotCard;

    @ApiModelProperty("生活精选")
    private List<DocRes> lifeChoice;

    @ApiModelProperty("文档状态")
    private int status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date lastupdatetime;

    @ApiModelProperty("文档内容")
    private String content;

    @ApiModelProperty("新闻数据")
    private List<NewsRes> newsData;

}
