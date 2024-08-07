package com.keyvin.es.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author weiwh
 * @date 2020/8/1 11:57
 */
@Data
public class SuggestResp {
    @ApiModelProperty(value = "类型，cs自动补齐completion suggester，ts术语推荐term suggester", example = "cs")
    @JSONField(ordinal = 1)
    private String type;

    @ApiModelProperty(value = "es推荐值", example = "love")
    @JSONField(ordinal = 2)
    private String es;

}
