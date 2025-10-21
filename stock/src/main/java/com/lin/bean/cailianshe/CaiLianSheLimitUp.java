package com.lin.bean.cailianshe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-16 21:33
 */
@NoArgsConstructor
@Data
public class CaiLianSheLimitUp {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("continuous_limit_up")
        private List<ContinuousLimitUpDTO> continuousLimitUp;
        @JsonProperty("plate_stock")
        private List<PlateStockDTO> plateStock;
        @JsonProperty("share")
        private ShareDTO share;

        @NoArgsConstructor
        @Data
        public static class ShareDTO {
            @JsonProperty("title")
            private String title;
            @JsonProperty("descr")
            private String descr;
            @JsonProperty("image")
            private String image;
            @JsonProperty("url")
            private String url;
        }

        @NoArgsConstructor
        @Data
        public static class ContinuousLimitUpDTO {
            @JsonProperty("height")
            private Integer height;
            @JsonProperty("stock_list")
            private List<StockListDTO> stockList;

            @NoArgsConstructor
            @Data
            public static class StockListDTO {
                @JsonProperty("secu_code")
                private String secuCode;
                @JsonProperty("secu_name")
                private String secuName;
            }
        }

        @NoArgsConstructor
        @Data
        public static class PlateStockDTO {
            @JsonProperty("secu_code")
            private String secuCode;
            @JsonProperty("secu_name")
            private String secuName;
            @JsonProperty("change")
            private Double change;
            @JsonProperty("up_reason")
            private String upReason;
            @JsonProperty("plate_stock_up_num")
            private Integer plateStockUpNum;
            @JsonProperty("stock_list")
            private List<StockListDTO> stockList;

            @NoArgsConstructor
            @Data
            public static class StockListDTO {
                @JsonProperty("secu_code")
                private String secuCode;
                @JsonProperty("secu_name")
                private String secuName;
                @JsonProperty("change")
                private Double change;
                @JsonProperty("last_px")
                private Double lastPx;
                @JsonProperty("cmc")
                private Long cmc;
                @JsonProperty("time")
                private String time;
                @JsonProperty("up_num")
                private String upNum;
                @JsonProperty("up_reason")
                private String upReason;
                @JsonProperty("up_tags")
                private List<String> upTags;
            }
        }
    }
}