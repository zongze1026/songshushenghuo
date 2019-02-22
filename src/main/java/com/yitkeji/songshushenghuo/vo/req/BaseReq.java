package com.yitkeji.songshushenghuo.vo.req;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Setter
@Getter
public class BaseReq{
    public static final String getRealIp(HttpServletRequest request){

        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotBlank(ip)){
            return ip.split(",")[0];
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotBlank(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
}
