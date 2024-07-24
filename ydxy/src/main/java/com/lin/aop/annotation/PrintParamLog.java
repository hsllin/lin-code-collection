package com.lin.aop.annotation;

import java.lang.annotation.*;

/**
 * 打印接口传参
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
public @interface PrintParamLog {
}
