package com.yitkeji.songshushenghuo.vo.res.app;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class UserWithdrawRes {

    @ApiModelProperty("申请提现Id")
    private Long userWithdrawId;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("银行卡Id")
    private String cardId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("账户余额")
    private int userMoney = 0;

    @ApiModelProperty("持卡人姓名")
    private String name;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("身份证号")
    private String idcard;

    @ApiModelProperty("银行名称")
    private String cardBank;

    @ApiModelProperty("提现金额")
    private int withdrawMoney = 0;

    @ApiModelProperty("提现状态")
    private int status;

    @ApiModelProperty("提现状态描述")
    private String statusDesc;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("创建时间(Date类型)")
    private Date withdrawCreateTime;

    @ApiModelProperty("创建时间(字符串类型)")
    private String strCreateTime;

    @ApiModelProperty("状态图标")
    private String statusIcon;

    public String getStatusIcon() {
        String staticDomain = SystemCfg.getInstance().getAppinfo().getStaticDomain();
        return staticDomain + "images/userWithdrawStatusIcon" + getStatus() + ".png";
    }
}
