package com.lin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解
 * 标记需要缓存的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
    
    /**
     * 缓存键名
     * 支持SpEL表达式，如: "stock:#{#date}"
     */
    String key();
    
    /**
     * 缓存时间（秒）
     * 如果为-1，则使用配置的默认时间
     */
    long ttl() default -1;
    
    /**
     * 缓存类型
     */
    CacheType type() default CacheType.DEFAULT;
    
    /**
     * 是否缓存null值
     */
    boolean cacheNull() default false;
    
    /**
     * 缓存条件
     * 支持SpEL表达式，如: "#{#date != null}"
     */
    String condition() default "";
    
    /**
     * 缓存类型枚举
     */
    enum CacheType {
        DEFAULT,        // 默认缓存
        STOCK_DATA,     // 股票数据
        HOT_DATA,       // 热门数据
        REALTIME_DATA,  // 实时数据
        STATISTICS_DATA // 统计数据
    }
}
