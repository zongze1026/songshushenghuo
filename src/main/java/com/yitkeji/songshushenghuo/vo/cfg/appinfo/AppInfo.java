package com.yitkeji.songshushenghuo.vo.cfg.appinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class AppInfo {

    @ApiModelProperty("APP名称")
    private String name;

    @ApiModelProperty("简称")
    private String subname;

    @ApiModelProperty("包名")
    private String pack;

    @ApiModelProperty("域名")
    private String domain;

    @ApiModelProperty("静态资源地址")
    private String staticDomain;

    @ApiModelProperty("代理地址")
    private String proxy;

    @ApiModelProperty("客服电话")
    private String phone;

    @ApiModelProperty("客服时间")
    private String time;

    @ApiModelProperty("Android版配置")
    private AppElement android;

    @ApiModelProperty("IOS版配置")
    private AppElement ios;

}
