package com.yitkeji.songshushenghuo.vo.cfg.vip;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class VipShare {

    @ApiModelProperty("注册分润")
    private Double[] regist;

    @ApiModelProperty("实名认证分润")
    private Double[] auth;

    @ApiModelProperty("新增银行卡分润")
    private Double[] addcard;

    @ApiModelProperty("代还分润")
    private Double[] repayment;

    @ApiModelProperty("升级VIP分润")
    private Double[] upvip;

    @ApiModelProperty("注册办卡分润")
    private Double[] registcard;

    @ApiModelProperty("贷款分润")
    private Double[] loan;

    @ApiModelProperty("收款分润")
    private Double[] cash;

    @ApiModelProperty("贷还分润")
    private Double[] loanpay;

}
