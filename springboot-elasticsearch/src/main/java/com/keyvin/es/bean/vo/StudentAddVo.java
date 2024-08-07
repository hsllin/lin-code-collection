package com.keyvin.es.bean.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author weiwh
 * @date 2020/7/12 11:30
 */
@Data
public class StudentAddVo {

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态（1：在售，0：已下架）", example = "1", required = true)
    private Integer status;


    @JSONField(ordinal = 2)
    @ApiModelProperty(value = "年龄", example = "18", required = true)
    private Integer age;

    @JSONField(ordinal = 3)
    @ApiModelProperty(value = "地址", example = "镇江市", required = true)
    private String address;

    @JSONField(ordinal = 4)
    @ApiModelProperty(value = "名字", example = "张三", required = true)
    private String name;

    @JSONField(ordinal = 5)
    @ApiModelProperty(value = "内容", example = "啊是大部分你大哥", required = true)
    private String content;

    @JSONField(ordinal = 6)
    @ApiModelProperty("学校名")
    private String schoolName;

    @JSONField(ordinal = 7)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间（yyyy-MM-dd HH:mm:ss）", example = "2020-07-12 12:00:01", required = true)
    private Date schoolTime;


}
