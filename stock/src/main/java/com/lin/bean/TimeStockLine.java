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
 * @create 2025-07-09 11:35
 */
@NoArgsConstructor
@Data
public class TimeStockLine {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("date")
        private Integer date;
        @JsonProperty("minute")
        private Integer minute;
        @JsonProperty("last_px")
        private Double lastPx;
        @JsonProperty("av_px")
        private Object avPx;
        @JsonProperty("change")
        private Double change;
        @JsonProperty("change_color")
        private Integer changeColor;
        @JsonProperty("amp")
        private Double amp;
        @JsonProperty("preclose_px")
        private Double preclosePx;
        @JsonProperty("open_px")
        private Double openPx;
        @JsonProperty("change_px")
        private Double changePx;
        @JsonProperty("purchases")
        private Object purchases;
        @JsonProperty("sales")
        private Object sales;
        @JsonProperty("business_amount")
        private Integer businessAmount;
        @JsonProperty("business_balance")
        private Long businessBalance;
    }
}