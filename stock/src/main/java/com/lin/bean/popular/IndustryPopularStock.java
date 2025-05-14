package com.lin.bean.popular;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-04-27 16:31
 */
@NoArgsConstructor
@Data
public class IndustryPopularStock {

    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("status_msg")
    private String statusMsg;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("plate_list")
        private List<PlateListDTO> plateList;

        @NoArgsConstructor
        @Data
        public static class PlateListDTO {
            @JsonProperty("code")
            private String code;
            @JsonProperty("rise_and_fall")
            private Double riseAndFall;
            @JsonProperty("etf_rise_and_fall")
            private Double etfRiseAndFall;
            @JsonProperty("hot_rank_chg")
            private Integer hotRankChg;
            @JsonProperty("market_id")
            private Integer marketId;
            @JsonProperty("hot_tag")
            private String hotTag;
            @JsonProperty("etf_product_id")
            private String etfProductId;
            @JsonProperty("rate")
            private String rate;
            @JsonProperty("etf_name")
            private String etfName;
            @JsonProperty("name")
            private String name;
            @JsonProperty("tag")
            private String tag;
            @JsonProperty("etf_market_id")
            private Integer etfMarketId;
            @JsonProperty("order")
            private Integer order;
        }
    }
}