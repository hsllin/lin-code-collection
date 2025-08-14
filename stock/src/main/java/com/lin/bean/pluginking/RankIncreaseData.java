package com.lin.bean.pluginking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-08-04 22:08
 */
@NoArgsConstructor
@Data
public class RankIncreaseData {

    @JsonProperty("List")
    private List<List<String>> List;
    @JsonProperty("Count")
    private Integer Count;
    @JsonProperty("ttag")
    private Double ttag;
    @JsonProperty("errcode")
    private String errcode;
}