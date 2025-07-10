package com.lin.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-07-09 11:27
 */
@NoArgsConstructor
@Data
public class TimeStock {

    @JsonProperty("errno")
    private Integer errno;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("symbol_code")
        private String symbolCode;
        @JsonProperty("symbol_name")
        private String symbolName;
        @JsonProperty("article_id")
        private Integer articleId;
        @JsonProperty("c_time")
        private String cTime;
        @JsonProperty("float")
        private String floatX;
        @JsonProperty("schema")
        private String schema;
    }
}