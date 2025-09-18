package com.lin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存清除注解
 * 标记需要清除缓存的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEvict {
    
    /**
     * 要清除的缓存键名
     * 支持SpEL表达式，如: "stock:#{#date}"
     */
    String key();
    
    /**
     * 是否清除所有匹配的缓存
     */
    boolean allEntries() default false;
    
    /**
     * 清除条件
     * 支持SpEL表达式，如: "#{#result != null}"
     */
    String condition() default "";
    
    /**
     * 是否在方法执行前清除缓存
     */
    boolean beforeInvocation() default false;
}
