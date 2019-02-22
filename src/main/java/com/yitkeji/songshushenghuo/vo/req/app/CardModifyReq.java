package com.yitkeji.songshushenghuo.vo.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class CardModifyReq extends BaseAppReq{


    @ApiModelProperty("银行卡ID")
    private long cardId;

    @ApiModelProperty("预留手机号")
    private String phone;

    @ApiModelProperty("账单日")
    private Integer billDay;

    @ApiModelProperty("还款日")
    private Integer repaymentDay;

}
