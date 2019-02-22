package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

@Setter
@Getter
public class ChangjieRes {
    private String code;
    private String msg;
    private String message;
    private ChangjieResultMap data;

    // 请求成功
    public final boolean isSuccess(){
        return "000000".equals(code);
    }

    // 业务请求成功
    public final boolean isActionSuccess(){
        return isSuccess() && "000000".equals(data.getRespCode());
    }

    // 错误信息
    public final String getErrorMsg(){
        if(data != null && data.getRespMsg() != null){
            return data.getRespMsg();
        }
        if(StringUtils.isNotBlank(message)){
            return message;
        }
        return msg;
    }
}
