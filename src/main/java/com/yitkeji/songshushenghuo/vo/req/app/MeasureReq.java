package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MeasureReq extends BaseAppReq {

    @ApiModelProperty("银行卡ID")
    private Long cardId;

    @ApiModelProperty("序列号")
    private String serialNumber;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty(value = "短信验证码", allowEmptyValue = true)
    private String code;
}
