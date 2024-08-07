package com.keyvin.es.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author weiwh
 * @date 2020/8/1 11:20
 */
@Data
public class StudentListResp extends ResponseCommon {


    @ApiModelProperty(value = "数据")
    @JSONField(ordinal = 4)
    private List<StudentResp> list;

}
