package com.keyvin.es.bean.response;

/**
 * @author weiwh
 * @date 2020/7/12 11:20
 */
public enum ResponseEnum {
    SUCCESS(200, "成功"),
    PARAMETER_ERROR(400, "请求参数异常！"),
    REQUEST_METHOD_ERROR(401, "请求方法不正确，请检查是GET还是POST！"),
    INNER_SERVER_ERROR(500, "系统内部错误！");

    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(Integer code){
        for(ResponseEnum anEnum: ResponseEnum.values()){
            if(anEnum.getCode() == code){
                return anEnum.message;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
