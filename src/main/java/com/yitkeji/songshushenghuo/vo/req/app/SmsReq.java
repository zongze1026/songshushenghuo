package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.validation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class SmsReq{

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("短信类型 1:注册 2:找回密码 3:收款 4:余额提现")
    private int type;

    @ApiModelProperty("图片验证码")
    private String imgCode;

    @ApiModelProperty("短信验证码识别Id")
    private String codeId;


    @Phone
    public String getPhone() {
        return phone;
    }

}
