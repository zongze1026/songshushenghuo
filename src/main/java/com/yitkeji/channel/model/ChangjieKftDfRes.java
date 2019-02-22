package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

@Setter
@Getter
public class ChangjieKftDfRes {
    private String code;
    private String message;
    private String msg;
    private String state;
    private String orderNo;
    private String status;
    private String errorCode;
    private String failureDetails;


    /**
     * 是否成功
     * @return
     */
    public final boolean isSuccess(){
        return "1".equals(status);
    }

    /**
     * 获取错误信息
     * @return
     */
    public final String getErrorMsg(){
        if(failureDetails != null){
            return failureDetails;
        }
        if(StringUtils.isNotBlank(msg)){
            return msg;
        }
        return message;
    }
}
