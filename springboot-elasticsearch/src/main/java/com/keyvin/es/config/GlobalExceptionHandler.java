package com.keyvin.es.config;

import com.keyvin.es.bean.response.ResponseEnum;
import com.keyvin.es.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理
 * @author weiwh
 * @date 2020/7/12 11:24
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 通用异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e){
        log.error("未知异常，原因是：", e);
        return ResultBody.error(ResponseEnum.INNER_SERVER_ERROR.getCode());
    }

    /**
     * 参数异常处理
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public String missingRequestParameterHandler(MissingServletRequestParameterException e){
        log.error("请求参数异常，原因是：", e);
        return ResultBody.error(ResponseEnum.PARAMETER_ERROR.getCode());
    }

    /**
     * 方法异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        log.error("请求方法异常！", e);
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return ResultBody.error(ResponseEnum.PARAMETER_ERROR.getCode(), message);
    }

    /**
     * 空指针异常处理
     */
    @ExceptionHandler(value = NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException e){
        log.error("发生空指针异常！原因是：", e);
        return ResultBody.error(ResponseEnum.INNER_SERVER_ERROR.getCode());
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(value = CustomException.class)
    public String customExceptionHandler(CustomException e){
        log.error("自定义异常！", e.getMessage());
        return ResultBody.error(e.getCode(), e.getMessage());
    }

    /**
     * 请求方法异常处理
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public String methodNotSupportedHandler(HttpRequestMethodNotSupportedException e){
        log.error("请求方法异常！", e);
        return ResultBody.error(ResponseEnum.REQUEST_METHOD_ERROR.getCode());
    }

    /**
     * 验证异常处理
     */
    @ExceptionHandler(value = BindException.class)
    public String bindExceptionHandler(BindException e){
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("验证异常！"+ message);
        return ResultBody.error(ResponseEnum.PARAMETER_ERROR.getCode(), message);
    }



}
