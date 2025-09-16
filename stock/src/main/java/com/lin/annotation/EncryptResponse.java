package com.lin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密响应注解
 * 标记需要加密的API接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptResponse {
    
    /**
     * 是否加密整个响应体
     * @return true-加密整个响应体，false-只加密data字段
     */
    boolean encryptAll() default false;
    
    /**
     * 需要加密的字段名（当encryptAll为false时生效）
     * @return 字段名数组
     */
    String[] fields() default {};
}
