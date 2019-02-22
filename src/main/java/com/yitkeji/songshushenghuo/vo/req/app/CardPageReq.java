package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class CardPageReq extends BaseAppPageReq {

    @ApiModelProperty("银行卡类型（1信用卡，0储蓄卡）")
    private Integer type;

}
