package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class PlanPageReq extends BaseAppPageReq {

    @ApiModelProperty("银行卡ID")
    private Long cardId;

    @ApiModelProperty("还款状态")
    private int status;

}
