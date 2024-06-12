package com.lin.aop;

import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 打印接口传参
 */
@Component
@Aspect
public class PrintParamLogAspect {
    private static final String DATA = "data";

    @Around(value = "@annotation(com.ybg.aop.annotation.PrintParamLog)")
    public Object printParamsLog(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        // 获取通知签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
//        获取参数名称 一一对应
        String[] parameterNames = signature.getParameterNames();
//        获取参数类型 一一对应
        StringBuilder sb = new StringBuilder();
        Object target = joinPoint.getTarget();
        List<String> targetMethodParams = Arrays.asList(parameterNames);
        for (int i = 0; i < targetMethodParams.size(); i++) {
            if (DATA.equals(targetMethodParams.get(i))) {
                String data =(String) args[i];
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String uniqueCode = request.getAttribute("uniqueCode") + "";
//                Map<String, Object> paramMap = decryptFromClient(data,uniqueCode);
//                paramMap.put("userId",request.getAttribute("userId"));
//                paramMap.put("userType",request.getAttribute("userType"));
//                LogUtil.info("进入《{}》方法, 参数为: {}",target.getClass()+ methodName, JSONUtil.toJsonStr(paramMap));
            }
        }

        try {

        } catch (Exception e) {
//            LogUtil.error("获取打印日志出错{}", e);
        }

//        LogUtil.info("进入《{}》方法, 参数为: {}", methodName, sb.toString());

        Object object = null;
        // 继续执行方法
        try {
            object = joinPoint.proceed();

        } catch (Throwable throwable) {
//            LogUtil.error("打印日志处理error。。", throwable);
        }
        return object;
    }

}
