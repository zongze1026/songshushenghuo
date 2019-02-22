package com.yitkeji.songshushenghuo.vo.res.app;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class NewsRes {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("作者")
    private String author_name;

    @ApiModelProperty("图片")
    private String thumbnail_pic_s;

    @ApiModelProperty("秘钥")
    private String uniquekey;

    @ApiModelProperty("新闻标题")
    private String title;

    @ApiModelProperty("新闻类型")
    private String category;

    @ApiModelProperty("新闻链接")
    private String url;

    @ApiModelProperty("安卓跳转网页标识")
    private String androidPageLabel = "com.yitkeji.wukongkabao.ui.activity.other.SimpleWebActivity";

    @ApiModelProperty("IOS跳转网页标识")
    private String iosPageLabel;

}
