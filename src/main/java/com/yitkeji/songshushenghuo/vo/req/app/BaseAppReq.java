package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.vo.req.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class BaseAppReq extends BaseReq {

    @ApiModelProperty("签名")
    private String sign;
}
