package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum SmsType {

    REGIST("注册", 1),
    FOUNDPWD("找回密码", 2),
    CASH("收款", 3),
    WITHDRAW("余额提现", 4);


    private int code;
    private String desc;

    SmsType(String desc, int code){
        this.code = code;
        this.desc = desc;
    }

    public static final SmsType getType(int code){
        for(SmsType smsType: SmsType.values()){
            if(smsType.getCode() == code){
                return smsType;
            }
        }
        return null;
    }
}
