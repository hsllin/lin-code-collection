package com.lin.bean.jfzt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-06-25 22:19
 */
@NoArgsConstructor
@Data
public class InflowAndOutFlowRankBean {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("currentTime")
    private Long currentTime;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("pageCount")
        private Integer pageCount;
        @JsonProperty("list")
        private List<ListDTO> list;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @JsonProperty("symbol")
            private String symbol;
            @JsonProperty("market")
            private String market;
            @JsonProperty("name")
            private String name;
            @JsonProperty("lastPx")
            private Integer lastPx;
            @JsonProperty("pxChangeRate")
            private Double pxChangeRate;
            @JsonProperty("superNetTurnover")
            private Long superNetTurnover;
            @JsonProperty("largeNetTurnover")
            private Integer largeNetTurnover;
            @JsonProperty("mediumNetTurnover")
            private Integer mediumNetTurnover;
            @JsonProperty("littleNetTurnover")
            private Integer littleNetTurnover;
            @JsonProperty("mainNetTurnover")
            private Long mainNetTurnover;
        }
    }
}