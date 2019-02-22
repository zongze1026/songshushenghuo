package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class SendCodeReq extends BaseAppReq {

    @ApiModelProperty("账号")
    private String userName;

    @ApiModelProperty("派码数量")
    private int sendCodeNum = 0;
}
