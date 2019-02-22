package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WechatRes {

    private String return_code;
    private String return_msg;


    // 以下字段在return_code为SUCCESS的时候有返回
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;

    // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
    private String trade_type;
    private String prepay_id;

}
