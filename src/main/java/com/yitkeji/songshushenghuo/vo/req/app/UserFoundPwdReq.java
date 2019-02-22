package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.validation.Password;
import com.yitkeji.songshushenghuo.validation.Phone;
import com.yitkeji.songshushenghuo.validation.SmsCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserFoundPwdReq {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("短信验证码")
    private String code;

    @Phone
    public String getPhone() {
        return phone;
    }

    @Password
    public String getPassword() {
        return password;
    }

    @SmsCode
    public String getCode() {
        return code;
    }
}
