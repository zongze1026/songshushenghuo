package com.yitkeji.songshushenghuo.Filter;

import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.util.ZGLTool;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class MainInterfaceFilter implements Filter {

    private Logger logger = Logger.getLogger(MainInterfaceFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 类型转换
            HttpServletRequest mHttpServletRequest = (HttpServletRequest) request;
            // 类型转换
            HttpServletResponse mHttpServletResponse = (HttpServletResponse) response;

            String origin = mHttpServletRequest.getHeader("Origin");
            mHttpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
            mHttpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
            mHttpServletResponse.setHeader("Access-Control-Allow-Headers","Origin,Content-Type,Accept,token,X-Requested-With");
            mHttpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");

            // 请求频率限制
            if(isTooOften(mHttpServletRequest)){
                ZGLTool.outToPrint(mHttpServletResponse, Result.fail("操作太频繁啦"));
                return;
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {

    }


    /**
     * 判断请求是否太频繁
     * @param request
     * @return
     */
    private boolean isTooOften(HttpServletRequest request){
        String uri = request.getRequestURI();
        String ip = getIpAddress(request);
        if(StringUtils.isBlank(ip.trim()) || StringUtils.isBlank(uri)){
            return false;
        }
        String cacheKey = CacheKey.FREQUENCY.getKey(ip, uri.replaceAll("\\W", "_"));

        Integer integer = RedisUtil.get(cacheKey);
        int count = 0;
        if(integer != null){
            count = integer;
            // 2秒钟10次
            if(count > 10){
                return true;
            }
        }
        RedisUtil.set(cacheKey,count+1,2, TimeUnit.SECONDS);
        return false;
    }


    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }
}
