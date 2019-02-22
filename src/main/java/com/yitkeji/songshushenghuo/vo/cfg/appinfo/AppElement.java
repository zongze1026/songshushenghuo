package com.yitkeji.songshushenghuo.vo.cfg.appinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class AppElement {


    @ApiModelProperty("包名")
    private String pack;

    @ApiModelProperty("版本")
    private String version;

    @ApiModelProperty("版本备注")
    private String comment;

    @ApiModelProperty("下载地址")
    private String download;

}
