package com.keyvin.es.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author weiwh
 * @date 2020/8/1 9:44 
 */
@Data
public class StudentListVo extends BaseVo{

    @NotBlank(message = "关键字不能为空")
    @ApiModelProperty(value = "关键字", example = "张三", required = true)
    private String keyword;

}
