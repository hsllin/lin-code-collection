package com.keyvin.es.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.keyvin.es.bean.response.ResponseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author weiwh
 * @date 2020/7/12 11:21
 */
@Data
public class ResultBody implements Serializable {
    @JSONField(name = "code", ordinal = 1)
    private Integer code;

    @JSONField(name = "message", ordinal = 2)
    private String message;

    @JSONField(name = "body", ordinal = 3)
    private Object body;

    public ResultBody() {
    }

    public ResultBody(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 成功
     */
    public static String success(){
        return success(null);
    }

    /**
     * 成功
     */
    public static String success(Object data){
        ResultBody rb = new ResultBody();
        rb.setCode(ResponseEnum.SUCCESS.getCode());
        rb.setMessage(ResponseEnum.SUCCESS.getMessage());
        rb.setBody(data);
        return JSON.toJSONString(rb, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 失败
     */
    public static String error(Integer code){
        ResultBody rb = new ResultBody();
        rb.setCode(code);
        rb.setMessage(ResponseEnum.getMessage(code));
        rb.setBody(null);
        return JSON.toJSONString(rb, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 失败
     */
    public static String error(Integer code, String message){
        ResultBody rb = new ResultBody();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setBody(null);
        return JSON.toJSONString(rb, SerializerFeature.WriteMapNullValue);
    }

}
