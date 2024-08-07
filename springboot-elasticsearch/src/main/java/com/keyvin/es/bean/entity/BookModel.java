package com.keyvin.es.bean.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author weiwh
 * @date 2020/7/12 9:26
 */
@Data
public class BookModel implements Serializable {

    @ApiModelProperty(value = "图书ID", example = "162ca564eb63430ea875ba0358699ae9")
    private String id;

    @ApiModelProperty(value = "图书名称", example = "瓦尔登湖")
    private String name;

    @ApiModelProperty(value = "作者", example = "亨利·戴维·梭罗", required = true)
    private String author;

    @ApiModelProperty(value = "图书分类(10010文学小说, 20010科幻小说, 30010杂志其他)", example = "10010")
    private Integer category;

    @ApiModelProperty(value = "图书价格", example = "99.98")
    private Double price;

    @ApiModelProperty(value = "图书说明", example = "出版")
    private String content;

    @ApiModelProperty(value = "上架时间（yyyy-MM-dd HH:mm:ss）", example = "2020-07-12 12:00:01")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @ApiModelProperty(value = "状态（1：在售，0：已下架）", example = "1")
    private Integer status;

}
