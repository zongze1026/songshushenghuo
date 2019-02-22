package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

@Setter
@Getter
public class ChangjieDfRes {
    private String code;
    private String message;
    private String msg;
    private ChangjieResultMap resultMap;
    private String state;

    /**
     * 是否成功
     * @return
     */
    public final boolean isSuccess(){
        if(resultMap != null && "FAIL".equals(resultMap.getStatus())){
            return false;
        }
        if(resultMap != null && resultMap.getBizCode() != null && !resultMap.getBizCode().equals("1")){
            return false;
        }
        return "SUCCESS".equals(state);
    }

    /**
     * 获取错误信息
     * @return
     */
    public final String getErrorMsg(){
        if(resultMap != null && resultMap.getBizMsg() != null){
            return resultMap.getBizMsg();
        }
        if(StringUtils.isNotBlank(msg)){
            return msg;
        }
        return message;
    }
}
