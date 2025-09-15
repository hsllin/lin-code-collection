package com.lin.bean.intradaychange;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-14 10:16
 */
@NoArgsConstructor
@Data
public class BuyChangeBean {


    @JsonProperty("rc")
    private Integer rc;
    @JsonProperty("rt")
    private Integer rt;
    @JsonProperty("svr")
    private Integer svr;
    @JsonProperty("lt")
    private Integer lt;
    @JsonProperty("full")
    private Integer full;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("tc")
        private Integer tc;
        @JsonProperty("allstock")
        private List<AllstockDTO> allstock;

        @NoArgsConstructor
        @Data
        public static class AllstockDTO {
            @JsonProperty("tm")
            private Integer tm;
            @JsonProperty("c")
            private String c;
            @JsonProperty("m")
            private Integer m;
            @JsonProperty("n")
            private String n;
            @JsonProperty("t")
            private Integer t;
            @JsonProperty("i")
            private String i;
            private Integer count=1;
            private String time;
            private double percent;
            private double buyNum;
            private String industry;
        }
    }
}