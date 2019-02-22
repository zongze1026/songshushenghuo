package com.yitkeji.songshushenghuo.vo.req.admin;

import com.yitkeji.songshushenghuo.vo.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserUpdateReq {

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("邮件")
    private String email;

    @ApiModelProperty("VIP等级")
    private Integer vip;

    @ApiModelProperty("推荐人编号")
    private Long referenceId;

    @ApiModelProperty("代理编号")
    private Long agentId;

    @ApiModelProperty("用户状态")
    private UserStatus status;

    @ApiModelProperty("确认操作结果")
    private Boolean checked = false;

    @ApiModelProperty("余额转化")
    private Integer turnMoney;

}
