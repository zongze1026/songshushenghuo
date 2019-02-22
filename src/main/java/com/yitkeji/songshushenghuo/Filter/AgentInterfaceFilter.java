package com.yitkeji.songshushenghuo.Filter;

import com.yitkeji.songshushenghuo.service.LogActionService;
import com.yitkeji.songshushenghuo.service.RoleService;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.util.ZGLTool;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.ResCode;
import com.yitkeji.songshushenghuo.vo.model.LogAction;
import com.yitkeji.songshushenghuo.vo.model.Role;
import com.yitkeji.songshushenghuo.vo.model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AgentInterfaceFilter implements Filter {

    private Logger logger = Logger.getLogger(AgentInterfaceFilter.class);

    @Autowired
    private LogActionService logActionService;

    @Autowired
    private RoleService roleService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String token = servletRequest.getHeader("token");
        String path = servletRequest.getRequestURI();
        try {

            if (ignored(path)) {
                chain.doFilter(request, response);
                return;
            }
            if(StringUtils.isBlank(token)){
                ZGLTool.outToPrint(servletResponse, Result.fail(ResCode.NEEDLOGIN));
                return;
            }

            User agent = RedisUtil.get(CacheKey.AGENT_TOKEN.getKey(token));
            if(agent == null){
                ZGLTool.outToPrint(servletResponse, Result.fail(ResCode.NEEDLOGIN));
                return;
            }
            Role role = roleService.findByPrimaryKey(agent.getRoleId());

            servletRequest.setAttribute("agent", agent);
            servletRequest.setAttribute("role", role);

            if(!role.checkAuth(path)){
                ZGLTool.outToPrint(servletResponse, Result.fail("无权访问"));
                return;
            }
            chain.doFilter(request, response);
            createActionLog(agent, servletRequest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ZGLTool.outToPrint(servletResponse, Result.fail());
            return;
        }
    }

    private boolean ignored(String uri){
        String[] urls = {"/agent/agent/login"};
        for (String url: urls){
            if(url.equals(uri)){
                return true;
            }
        }
        return false;
    }



    private void createActionLog(User agent, HttpServletRequest request){
        String path = request.getRequestURI();
        String params = request.getAttribute("params") == null ? "": (String)request.getAttribute("params");
        LogAction logAction = new LogAction();
        logAction.setUserId(agent.getUserId());
        logAction.setPath(path);
        logAction.setData(params);
//        logActionService.add(logAction);
    }

    @Override
    public void destroy() {

    }
}
