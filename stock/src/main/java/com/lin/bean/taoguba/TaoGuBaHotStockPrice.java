package com.lin.bean.taoguba;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-14 16:57
 */
@NoArgsConstructor
@Data
public class TaoGuBaHotStockPrice {

    @JsonProperty("status")
    private Boolean status;
    @JsonProperty("errorCode")
    private Integer errorCode;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("dto")
    private List<DtoDTO> dto;
    @JsonProperty("_t")
    private Long t;

    @NoArgsConstructor
    @Data
    public static class DtoDTO {
        @JsonProperty("name")
        private String name;
        @JsonProperty("code")
        private String code;
        @JsonProperty("fullCode")
        private String fullCode;
        @JsonProperty("openPrice")
        private Double openPrice;
        @JsonProperty("closePrice")
        private Double closePrice;
        @JsonProperty("price")
        private Double price;
        @JsonProperty("highPrice")
        private Double highPrice;
        @JsonProperty("lowPrice")
        private Double lowPrice;
        @JsonProperty("lastDate")
        private String lastDate;
        @JsonProperty("lastTime")
        private String lastTime;
        @JsonProperty("zhangting")
        private Double zhangting;
        @JsonProperty("dieting")
        private Double dieting;
        @JsonProperty("pxChange")
        private Double pxChange;
        @JsonProperty("pxChangeRate")
        private Double pxChangeRate;
        @JsonProperty("buy_1")
        private Double buy1;
        @JsonProperty("sell_1")
        private Double sell1;
        @JsonProperty("volumn")
        private Double volumn;
        @JsonProperty("volumnPrice")
        private Double volumnPrice;
        @JsonProperty("totalValue")
        private Double totalValue;
        @JsonProperty("turnoverRate")
        private Double turnoverRate;
        @JsonProperty("circulationValue")
        private Double circulationValue;
        @JsonProperty("linkingBoard")
        private Object linkingBoard;
        @JsonProperty("status")
        private Object status;
        @JsonProperty("tradingPhaseCode")
        private Object tradingPhaseCode;
    }
}