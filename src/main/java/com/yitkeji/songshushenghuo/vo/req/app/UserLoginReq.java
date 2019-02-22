package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.validation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserLoginReq{

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @Phone
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
