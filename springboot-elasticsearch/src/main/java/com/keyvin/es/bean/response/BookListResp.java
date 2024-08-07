package com.keyvin.es.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.keyvin.es.bean.entity.BookModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author weiwh
 * @date 2020/7/12 11:55
 */
@Data
public class BookListResp extends ResponseCommon {
//    @ApiModelProperty(value = "当前页", example = "1")
//    @JSONField(ordinal = 1)
//    private int pageNo;
//
//    @ApiModelProperty(value = "每页大小", example = "10")
//    @JSONField(ordinal = 2)
//    private int pageSize;
//
//    @ApiModelProperty(value = "总数", example = "255")
//    @JSONField(ordinal = 3)
//    private long totalHits;

    @ApiModelProperty(value = "数据")
    @JSONField(ordinal = 4)
    private List<BookModel> list;

}
