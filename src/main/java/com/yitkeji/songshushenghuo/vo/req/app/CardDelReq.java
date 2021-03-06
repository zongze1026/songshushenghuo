package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class CardDelReq extends BaseAppReq{

    @ApiModelProperty("银行卡ID")
    private long cardId;

}
