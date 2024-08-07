package com.keyvin.es.bean.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class BookAddVo {

    @NotBlank(message = "图书名称不能为空")
    @ApiModelProperty(value = "图书名称", example = "瓦尔登湖", required = true)
    private String name;

    @NotBlank(message = "作者不能为空")
    @ApiModelProperty(value = "作者", example = "亨利·戴维·梭罗", required = true)
    private String author;

    @NotNull(message = "图书分类不能为空")
    @ApiModelProperty(value = "图书分类(10010文学小说, 20010科幻小说, 30010杂志其他)", example = "10010", required = true)
    private Integer category;

    @NotNull(message = "图书价格不能为空")
    @ApiModelProperty(value = "图书价格", example = "99.98", required = true)
    private Double price;

    @NotBlank(message = "图书说明不能为空")
    @ApiModelProperty(value = "图书说明", example = "出版", required = true)
    private String content;

    @NotNull(message = "上架时间不能为空")
    @ApiModelProperty(value = "上架时间（yyyy-MM-dd HH:mm:ss）", example = "2020-07-12 12:00:01", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态（1：在售，0：已下架）", example = "1", required = true)
    private Integer status;

}
