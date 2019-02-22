package com.yitkeji.songshushenghuo.vo.res.pc;

import com.yitkeji.songshushenghuo.vo.cfg.appinfo.AppElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class AppElementRes {


    @ApiModelProperty("版本")
    private String version;

    @ApiModelProperty("版本备注")
    private String comment;

    @ApiModelProperty("下载地址")
    private String download;

    public AppElementRes(AppElement element){
        this.version = element.getVersion();
        this.comment = element.getComment();
        this.download = element.getDownload();
    }
}
