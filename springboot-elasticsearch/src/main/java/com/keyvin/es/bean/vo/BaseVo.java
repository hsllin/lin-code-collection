package com.keyvin.es.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author weiwh
 * @date 2020/8/1 9:43
 */
@Data
public class BaseVo {
    @ApiModelProperty(value = "当前页", example = "1")
    private int pageNo=1;

    @ApiModelProperty(value = "每页大小", example = "10")
    private int pageSize=10;

}
