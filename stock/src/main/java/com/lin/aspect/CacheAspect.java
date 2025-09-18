package com.lin.aspect;

import com.lin.annotation.CacheEvict;
import com.lin.annotation.Cacheable;
import com.lin.service.CacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 缓存AOP切面
 * 实现真正的缓存逻辑
 */
@Aspect
@Component
public class CacheAspect {
    
    @Autowired
    private CacheService cacheService;
    
    private final ExpressionParser parser = new SpelExpressionParser();
    
    /**
     * 处理@Cacheable注解
     */
    @Around("@annotation(cacheable)")
    public Object handleCacheable(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        // 生成缓存键
        String cacheKey = generateCacheKey(joinPoint, cacheable.key());
        
        // 检查缓存条件
        if (!evaluateCondition(joinPoint, cacheable.condition())) {
            System.out.println("缓存条件不满足，直接执行方法: " + cacheKey);
            return joinPoint.proceed();
        }
        
        // 尝试从缓存获取数据
        Object result = cacheService.get(cacheKey, Object.class);
        if (result != null) {
            System.out.println("缓存命中: " + cacheKey);
            
            // 检查方法返回类型，如果是ResponseEntity，需要重新包装
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Class<?> returnType = signature.getReturnType();
            
            if (ResponseEntity.class.isAssignableFrom(returnType)) {
                return ResponseEntity.ok(result);
            }
            
            return result;
        }
        
        System.out.println("缓存未命中，执行方法: " + cacheKey);
        
        // 执行方法
        result = joinPoint.proceed();
        
        // 缓存结果
        if (result != null || cacheable.cacheNull()) {
            long ttl = cacheable.ttl() > 0 ? cacheable.ttl() : 
                      cacheService.getTtlByType(cacheable.type());
            
            // 如果结果是ResponseEntity，只缓存body部分
            Object cacheValue = result;
            if (result instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                cacheValue = responseEntity.getBody();
            }
            
            cacheService.set(cacheKey, cacheValue, ttl);
            System.out.println("缓存设置成功: " + cacheKey + ", TTL: " + ttl + "秒");
        }
        
        return result;
    }
    
    /**
     * 处理@CacheEvict注解
     */
    @Around("@annotation(cacheEvict)")
    public Object handleCacheEvict(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        // 检查清除条件
        if (!evaluateCondition(joinPoint, cacheEvict.condition())) {
            return joinPoint.proceed();
        }
        
        // 方法执行前清除缓存
        if (cacheEvict.beforeInvocation()) {
            clearCache(joinPoint, cacheEvict);
        }
        
        // 执行方法
        Object result = joinPoint.proceed();
        
        // 方法执行后清除缓存
        if (!cacheEvict.beforeInvocation()) {
            clearCache(joinPoint, cacheEvict);
        }
        
        return result;
    }
    
    /**
     * 生成缓存键
     */
    private String generateCacheKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        try {
            // 如果键表达式不包含SpEL语法（#{}），直接使用字符串
            if (!keyExpression.contains("#{")) {
                return cacheService.generateKey(keyExpression);
            }
            
            // 创建SpEL上下文
            EvaluationContext context = new StandardEvaluationContext();
            
            // 添加方法参数
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            
            // 添加方法名和类名
            context.setVariable("methodName", method.getName());
            context.setVariable("className", method.getDeclaringClass().getSimpleName());
            
            // 添加HttpServletRequest支持
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    context.setVariable("request", arg);
                    break;
                }
            }
            
            // 解析表达式
            Expression expression = parser.parseExpression(keyExpression);
            Object keyValue = expression.getValue(context);
            
            return cacheService.generateKey(String.valueOf(keyValue));
        } catch (Exception e) {
            // 如果SpEL解析失败，使用默认键
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String defaultKey = signature.getDeclaringTypeName() + "." + signature.getName() + 
                              ":" + Arrays.hashCode(joinPoint.getArgs());
            System.err.println("SpEL解析失败，使用默认键: " + defaultKey + ", 错误: " + e.getMessage());
            return cacheService.generateKey(defaultKey);
        }
    }
    
    /**
     * 评估条件表达式
     */
    private boolean evaluateCondition(ProceedingJoinPoint joinPoint, String condition) {
        if (condition == null || condition.trim().isEmpty()) {
            return true;
        }
        
        try {
            EvaluationContext context = new StandardEvaluationContext();
            
            // 添加方法参数
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            
            Expression expression = parser.parseExpression(condition);
            Boolean result = expression.getValue(context, Boolean.class);
            return result != null && result;
        } catch (Exception e) {
            System.err.println("条件评估失败: " + condition + ", 错误: " + e.getMessage());
            return true; // 默认执行
        }
    }
    
    /**
     * 清除缓存
     */
    private void clearCache(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) {
        try {
            if (cacheEvict.allEntries()) {
                // 清除所有匹配的缓存
                String pattern = generateCacheKey(joinPoint, cacheEvict.key());
                cacheService.deletePattern(pattern + "*");
                System.out.println("清除缓存模式: " + pattern + "*");
            } else {
                // 清除特定缓存
                String cacheKey = generateCacheKey(joinPoint, cacheEvict.key());
                cacheService.delete(cacheKey);
                System.out.println("清除缓存: " + cacheKey);
            }
        } catch (Exception e) {
            System.err.println("清除缓存失败: " + e.getMessage());
        }
    }
}