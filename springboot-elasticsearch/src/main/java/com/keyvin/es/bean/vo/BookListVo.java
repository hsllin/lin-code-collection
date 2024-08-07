package com.keyvin.es.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author weiwh
 * @date 2020/7/12 9:49
 */
@Data
public class BookListVo extends BaseVo{

    @ApiModelProperty(value = "图书名称", example = "瓦尔登湖")
    private String name;

    @ApiModelProperty(value = "作者", example = "梭罗")
    private String author;

    @ApiModelProperty(value = "状态（1：在售，0：已下架）", example = "1")
    private String status;

    @ApiModelProperty(value = "上架时间（yyyy-MM-dd HH:mm:ss）", example = "2020-07-12 12:00:01")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @ApiModelProperty(value = "图书分类(10010文学小说, 20010科幻小说, 30010杂志其他)", example = "10010")
    private String categories;

}
