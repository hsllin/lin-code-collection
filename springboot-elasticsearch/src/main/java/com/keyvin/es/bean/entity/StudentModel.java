package com.keyvin.es.bean.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import java.util.Date;

/**
 * @author weiwh
 * @date 2020/7/19 11:34
 */
@Data
public class StudentModel {
    @JSONField(ordinal = 1)
    private String id;

    @JSONField(ordinal = 2)
    private Integer age;

    @JSONField(ordinal = 3)
    private String address;

    @JSONField(ordinal = 4)
    private String name;

    @JSONField(ordinal = 5)
    private String content;

    @JSONField(ordinal = 6)
    private String schoolName;

    @JSONField(ordinal = 7)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date schoolTime;

}
