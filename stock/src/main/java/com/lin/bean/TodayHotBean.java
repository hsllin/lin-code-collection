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
 * @create 2025-05-20 10:58
 */
@NoArgsConstructor
@Data
public class TodayHotBean {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private List<DataDTO> data;
    @JsonProperty("reserve")
    private Object reserve;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("costTime")
    private Integer costTime;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("eid")
        private String eid;
        @JsonProperty("tradeDate")
        private String tradeDate;
        @JsonProperty("sortNum")
        private Integer sortNum;
        @JsonProperty("title")
        private String title;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("newsCode")
        private String newsCode;
        @JsonProperty("postId")
        private String postId;
        @JsonProperty("themeCode")
        private String themeCode;
        @JsonProperty("themeName")
        private String themeName;
        @JsonProperty("euTime")
        private Object euTime;
        @JsonProperty("fex3")
        private Integer fex3;
        @JsonProperty("stockList")
        private List<StockListDTO> stockList;
        @JsonProperty("kLineList")
        private List<Double> kLineList;
        @JsonProperty("cumulateF3")
        private Double cumulateF3;
        @JsonProperty("isHot")
        private Integer isHot;
        @JsonProperty("isNew")
        private Integer isNew;

        @NoArgsConstructor
        @Data
        public static class StockListDTO {
            @JsonProperty("code")
            private String code;
            @JsonProperty("name")
            private String name;
            @JsonProperty("f3")
            private Double f3;
        }
    }
}