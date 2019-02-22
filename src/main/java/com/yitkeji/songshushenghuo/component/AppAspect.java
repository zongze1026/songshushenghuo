package com.yitkeji.songshushenghuo.component;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Configuration
public class AppAspect {
    private static final Logger logger = LoggerFactory.getLogger(AppAspect.class);

    public AppAspect() {
    }

    // 不能拦截@RequestMapping，这样会导致swagger无法使用
    // @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @Pointcut("execution(public * com.yitkeji.songshushenghuo.controller.app.*.*(..))")
    public void pointCutMethod() {
    }

    // 声明环绕通知
    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
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

        if(null == request){
            return result;
        }
        System.out.println("接收请求："+ request.getRequestURI());
        System.out.println("请求参数：" + JSON.toJSONString(request.getParameterMap()));
        System.out.println("响应结果：" + JSON.toJSONString(result));
        return result;
    }

}