package com.yitkeji.songshushenghuo.Filter;

import com.yitkeji.songshushenghuo.service.RoleService;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.util.ZGLTool;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.ResCode;
import com.yitkeji.songshushenghuo.vo.model.Admin;
import com.yitkeji.songshushenghuo.vo.model.Role;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AdminInterfaceFilter implements Filter {

    private Logger logger = Logger.getLogger(AdminInterfaceFilter.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String path = servletRequest.getRequestURI();
        String token = servletRequest.getHeader("token");
        try {
            if (ignored(path)) {
                chain.doFilter(request, response);
                return;
            }
            if(StringUtils.isBlank(token)){
                ZGLTool.outToPrint(servletResponse, Result.fail(ResCode.NEEDLOGIN));
                return;
            }

            Object cacheAdmin = redisTemplate.opsForValue().get(CacheKey.ADMIN_TOKEN.getKey(token));
            if(cacheAdmin == null){
                ZGLTool.outToPrint(servletResponse, Result.fail(ResCode.NEEDLOGIN));
                return;
            }

            Admin admin = (Admin) cacheAdmin;
            Role role = roleService.findByPrimaryKey(admin.getRoleId());

            servletRequest.setAttribute("admin", admin);
            servletRequest.setAttribute("role", role);

            if(!role.checkAuth(path)){
                ZGLTool.outToPrint(servletResponse, Result.fail("无权访问"));
                return;
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ZGLTool.outToPrint(servletResponse, Result.fail());
            return;
        }
    }

    private boolean ignored(String uri){
        String[] urls = {"/admin/admin/login"};
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
