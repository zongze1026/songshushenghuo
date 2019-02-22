package com.yitkeji.songshushenghuo.vo.res.agent;

import com.yitkeji.songshushenghuo.vo.enums.UserStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserRes {

    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("储蓄卡ID")
    private Long debitCardId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("身份证号码")
    private String idcard;

    @ApiModelProperty("银行卡号")
    private String debitCardNo;

    @ApiModelProperty("银行名称")
    private String debitCardBank;

    @ApiModelProperty("代理商令牌")
    private String agentToken;

    @ApiModelProperty("角色")
    private Long roleId;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("VIP等级")
    private Integer vip;

    @ApiModelProperty("账户余额")
    private int money;

    @ApiModelProperty("认证状态")
    private int authStatus;

    @ApiModelProperty("用户状态")
    private UserStatus status;

    @ApiModelProperty("推荐人ID")
    private Long referenceId;

    @ApiModelProperty("推荐人姓名")
    private String referenceName;

    @ApiModelProperty("推荐人账号")
    private String referenceUserName;

    @ApiModelProperty("代理ID")
    private Long agentId;

    @ApiModelProperty("代理姓名")
    private String agentName;

    @ApiModelProperty("代理姓名")
    private String agentUserName;

    @ApiModelProperty("注册时间")
    private Date createTime;

}