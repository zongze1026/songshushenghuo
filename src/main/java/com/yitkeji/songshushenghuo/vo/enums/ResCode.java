package com.yitkeji.songshushenghuo.vo.enums;


import lombok.Getter;

/**
 * 接口通用状态码
 * 取消单模块状态码，只保有通用状态码
 */
@Getter
public enum ResCode {
    ARGERROR(7,"参数错误"),
    NEEDCHECK(6, "需要确认操作"),
    ACCOUNT_EXPIRED(5, "账号已过期"),
    NEEDPAY(4, "需要支付"),
    NEEDWEB(3, "需要网页验证"),
    NEEDLOGIN(2, "请登录"),
    NEEDCODE(1, "需要短信验证"),
    SUCCESS(0, "请求成功"),
    FAIL(-1, "请求失败"),
    ERROR(-2, "服务端异常");


    ResCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    private String msg;
    private int code;

    public String getMsg(String... params) {
        if(null == params || params.length < 1){
            return msg;
        }
        StringBuffer sb = new StringBuffer();
        for(String param: params){
            sb.append(param);
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

}
