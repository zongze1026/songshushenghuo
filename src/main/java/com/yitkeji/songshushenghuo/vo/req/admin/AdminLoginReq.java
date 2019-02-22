package com.yitkeji.songshushenghuo.vo.req.admin;

import com.yitkeji.songshushenghuo.validation.Password;
import com.yitkeji.songshushenghuo.validation.UserName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class AdminLoginReq {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @UserName
    public String getUserName() {
        return userName;
    }

    @Password
    public String getPassword() {
        return password;
    }
}
