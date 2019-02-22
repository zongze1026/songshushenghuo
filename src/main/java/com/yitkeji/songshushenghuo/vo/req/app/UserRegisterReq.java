package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.validation.Password;
import com.yitkeji.songshushenghuo.validation.Phone;
import com.yitkeji.songshushenghuo.validation.ReferralCode;
import com.yitkeji.songshushenghuo.validation.SmsCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class UserRegisterReq extends BaseAppReq{

    @ApiModelProperty("用户名")
    private String phone;

    @ApiModelProperty("短信验证码")
    private String code;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty(value = "邀请码", allowEmptyValue = true)
    private String referralCode;

    @ApiModelProperty(value = "定位城市", allowEmptyValue = true)
    private String city;

    @ApiModelProperty(value = "经度", allowEmptyValue = true)
    private String longitude;

    @ApiModelProperty(value = "维度", allowEmptyValue = true)
    private String latitude;

    @Phone
    public String getPhone() {
        return phone;
    }

    @ReferralCode
    public String getReferralCode(){
        return referralCode;
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
