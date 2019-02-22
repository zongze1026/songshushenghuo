package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class NativePayRes {

    @ApiModelProperty("签名参数")
    private Object signData;

    @ApiModelProperty("订单信息")
    private OrderRes order;

}
