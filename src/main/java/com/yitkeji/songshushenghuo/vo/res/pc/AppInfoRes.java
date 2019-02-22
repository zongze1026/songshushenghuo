package com.yitkeji.songshushenghuo.vo.res.pc;

import com.yitkeji.songshushenghuo.vo.cfg.appinfo.AppInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class AppInfoRes {

    @ApiModelProperty("APP名称")
    private String name;

    @ApiModelProperty("静态资源地址")
    private String staticDomain;

    @ApiModelProperty("Android版配置")
    private AppElementRes android;

    @ApiModelProperty("IOS版配置")
    private AppElementRes ios;

    public AppInfoRes(AppInfo info){
        this.name = info.getName();
        this.staticDomain = info.getStaticDomain();
        this.android = new AppElementRes(info.getAndroid());
        this.ios = new AppElementRes(info.getIos());
    }
}
