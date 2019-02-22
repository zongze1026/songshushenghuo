package com.yitkeji.songshushenghuo.vo.req.agent;

import com.yitkeji.songshushenghuo.validation.Password;
import com.yitkeji.songshushenghuo.validation.Phone;
import com.yitkeji.songshushenghuo.validation.UserName;
import com.yitkeji.songshushenghuo.vo.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Setter
@Getter
@ApiModel
public class AgentModifyReq {

    @ApiModelProperty("代理商ID")
    private Long userId;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("用户名称")
    private String realName;

    @ApiModelProperty("权限ID")
    private Long roleId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("用户状态")
    private UserStatus status;

    @UserName
    public String getUserName() {
        return userName;
    }

    @Password
    public String getPassword() {
        return password;
    }

    @Password
    public String getNewPassword() {
        return newPassword;
    }

    @Phone
    public String getPhone() {
        return phone;
    }

    @Email
    public String getEmail() {
        return email;
    }
}
