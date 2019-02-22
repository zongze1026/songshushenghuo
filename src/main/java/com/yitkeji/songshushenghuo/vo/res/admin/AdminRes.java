package com.yitkeji.songshushenghuo.vo.res.admin;

import com.yitkeji.songshushenghuo.vo.enums.AdminStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class AdminRes {

    @ApiModelProperty("管理员ID")
    private Long adminId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("令牌")
    private String token;

    @ApiModelProperty("角色")
    private Long roleId;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("登录次数")
    private Integer loginCount;

    @ApiModelProperty("登录IP")
    private String loginIp;

    @ApiModelProperty("登录时间")
    private Date loginTime;

    @ApiModelProperty("状态")
    private AdminStatus status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date lastupdatetime;

}
