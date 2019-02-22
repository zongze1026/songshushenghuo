package com.yitkeji.songshushenghuo.vo.res.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class CardRes {

    @ApiModelProperty("卡ID")
    private Long cardId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("账号")
    private String userName;

    @ApiModelProperty("卡号")
    private String cardNo;

    @ApiModelProperty("卡名称")
    private String cardBank;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("身份证号")
    private String idcard;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮件")
    private String email;

    @ApiModelProperty("卡类型")
    private int type;

    @ApiModelProperty("卡状态")
    private int status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date lastupdatetime;

}