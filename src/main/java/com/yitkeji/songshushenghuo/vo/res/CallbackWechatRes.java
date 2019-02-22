package com.yitkeji.songshushenghuo.vo.res;

import com.yitkeji.songshushenghuo.util.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信回调的响应
 */
@Setter
@Getter
public class CallbackWechatRes {
    private String return_code;
    private String return_msg;

    public CallbackWechatRes(String return_code, String return_msg){
        this.return_code = return_code;
        this.return_msg = return_msg;
    }

    public static final String success(){
        return ObjectUtil.convertToXml(new CallbackWechatRes("SUCCESS", null)).asXML();
    }

    public static final String fail(String msg){
        return ObjectUtil.convertToXml(new CallbackWechatRes("SUCCESS", msg)).asXML();
    }

}
