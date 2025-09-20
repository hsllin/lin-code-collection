package com.lin.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 安全输入验证注解
 * 
 * @author lin
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SafeInputValidator.class)
@Documented
public @interface SafeInput {
    
    String message() default "输入内容包含不安全字符";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 是否允许HTML标签
     */
    boolean allowHtml() default false;
    
    /**
     * 是否允许特殊字符
     */
    boolean allowSpecialChars() default false;
    
    /**
     * 最大长度
     */
    int maxLength() default 1000;
    
    /**
     * 最小长度
     */
    int minLength() default 0;
    
    /**
     * 允许的字符模式（正则表达式）
     */
    String pattern() default "";
    
    /**
     * 是否检查XSS
     */
    boolean checkXss() default true;
    
    /**
     * 是否检查SQL注入
     */
    boolean checkSqlInjection() default true;
}
