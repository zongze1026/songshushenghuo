package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.validation.Password;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserModifyPwdReq extends BaseAppReq{

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("密码")
    private String oldPwd; // TODO 改名为 oldPassword
    @ApiModelProperty("用户ID")
    private String userId;

    @Password
    public String getPassword() {
        return password;
    }

    @Password
    public String getOldPwd() {
        return oldPwd;
    }

}
