package com.yitkeji.songshushenghuo.vo.res.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class MeasureListRes {

    @ApiModelProperty("卡测评Id")
    private Long measureId;

    @ApiModelProperty("持卡人姓名")
    private String name;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("身份证号")
    private String idcard;

    @ApiModelProperty("银行名称")
    private String cardBank;

    @ApiModelProperty("测评访问的url")
    private String measureUrl;

    @ApiModelProperty("创建时间(Date类型)")
    private Date measureCreateTime;

    @ApiModelProperty("创建时间(字符串类型)")
    private String strCreateTime;
}
