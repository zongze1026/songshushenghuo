package com.yitkeji.songshushenghuo.Filter;


import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.util.ZGLTool;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.ResCode;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.BaseReq;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AppInterfaceFilter implements Filter {
    private Logger logger = Logger.getLogger(AppInterfaceFilter.class);
    private static final String HTTP_HEADER_TOKEN = "token";

    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig){

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        try {

            WrappedHttpServletRequest requestWrapper = new WrappedHttpServletRequest((HttpServletRequest) request);
            String path = servletRequest.getRequestURI();

            if(ignored(path)){
                chain.doFilter(requestWrapper, response);
                return;
            }
            String token = requestWrapper.getHeader(HTTP_HEADER_TOKEN);
            if(StringUtils.isBlank(token)){
                ZGLTool.outToPrint(servletResponse, Result.fail(ResCode.NEEDLOGIN));
                return;
            }

            User user = RedisUtil.get(CacheKey.TOKEN.getKey(token));
            if(user == null){
                ZGLTool.outToPrint(servletResponse, Result.fail(ResCode.NEEDLOGIN));
                return;
            }

            // 更新最后一次IP
            String ip = BaseReq.getRealIp(requestWrapper);
            if(!ip.equals(user.getLastLoginIp())){
                user.setLastLoginIp(ip);
                userService.updateByPrimaryKey(user, "lastLoginIp");
            }
            request.setAttribute("user", user);

            chain.doFilter(requestWrapper, response);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            ZGLTool.outToPrint(servletResponse, Result.fail());
            return ;
        }
    }


    private boolean ignored(String uri){
        String[] urls = {"/app/user/login", "/app/user/foundpwd", "/app/user/register", "/app/sms/code", "/app/doc/list", "/app/doc/info","/app/user/verify","/app/user/imgcode","/app/channel/news"};
        for (String url: urls){
            if(url.equals(uri)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
