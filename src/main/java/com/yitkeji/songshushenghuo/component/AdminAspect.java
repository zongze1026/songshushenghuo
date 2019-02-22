package com.yitkeji.songshushenghuo.component;

import com.alibaba.fastjson.JSON;
import com.yitkeji.songshushenghuo.service.LogActionService;
import com.yitkeji.songshushenghuo.util.IPAddressHelper;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.vo.model.Admin;
import com.yitkeji.songshushenghuo.vo.model.LogAction;
import com.yitkeji.songshushenghuo.vo.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Configuration
public class AdminAspect {
    private static final Logger log = LoggerFactory.getLogger(AdminAspect.class);

    @Autowired
    private LogActionService logActionService;

    public AdminAspect() {
    }

    // 不能拦截@RequestMapping，这样会导致swagger无法使用
    // @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @Pointcut("execution(public * com.yitkeji.songshushenghuo.controller.admin.*.*(..))")
    public void pointCutMethod() {
    }

    // 声明环绕通知
    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Class controllerClass = pjp.getTarget().getClass();
        HttpServletRequest request = null;
        Object result = pjp.proceed();

        List args = new ArrayList();
        for(int i=0; i<pjp.getArgs().length; i++){
            Object object = pjp.getArgs()[i];
            if(object instanceof HttpServletRequest){
                request = (HttpServletRequest)object;
            }
            if(object == null || object instanceof HttpServletRequest){
                continue;
            }
            args.add(object);
        }

        Method controllerMethod = ObjectUtil.getMethod(controllerClass, pjp.getSignature().getName());

        String[] tags = (String[])getAnnotationValue(controllerClass, Api.class, "tags");
        Object operation = getAnnotationValue(controllerMethod, ApiOperation.class, "value");
        RequestMethod[] requestMethods = (RequestMethod[])getAnnotationValue(controllerMethod, RequestMapping.class, "method");
        if(requestMethods.length < 1 ){
            return result;
        }
        boolean needLog = false;
        out: for(RequestMethod method: requestMethods){
            switch (method){
                case POST:
                case PUT:
                case DELETE: needLog = true; break out;
                default:
            }
        }

        if(!needLog || null == request){
            return result;
        }
        LogAction logAction = new LogAction();
        logAction.setPath(request.getRequestURI());
        if(request.getAttribute("admin") != null){
            Admin admin = (Admin)request.getAttribute("admin");
            logAction.setAdminId(admin.getAdminId());
        }
        if(request.getAttribute("agent") != null){
            User agent = (User)request.getAttribute("agent");
            logAction.setUserId(agent.getUserId());
        }
        logAction.setClientIp(IPAddressHelper.getClientIP(request));
        logAction.setModule(StringUtils.join(tags, ","));
        logAction.setName(operation.toString());
        logAction.setData(JSON.toJSONString(args));
        logAction.setResult(JSON.toJSONString(result));
        logActionService.add(logAction);
        return result;
    }


    private Object getAnnotationValue(Class controller, Class annotationClass, String methodName){
        Annotation anno = controller.getAnnotation(annotationClass);
        try {
            Method method = anno.getClass().getMethod(methodName);
            return method.invoke(anno);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }



    private Object getAnnotationValue(Method method, Class annotationClass, String methodName){
        Annotation anno = method.getAnnotation(annotationClass);
        try {
            Method annoMethod = anno.getClass().getMethod(methodName);
            return annoMethod.invoke(anno);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }
}