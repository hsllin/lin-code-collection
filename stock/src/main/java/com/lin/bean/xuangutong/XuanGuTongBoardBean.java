package com.lin.bean.xuangutong;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-06-14 16:35
 */
@NoArgsConstructor
@Data
public class XuanGuTongBoardBean {

    @JsonProperty("bottom_n_stocks")
    private BottomNStocksDTO bottomNStocks;
    @JsonProperty("core_avg_pcp")
    private Double coreAvgPcp;
    @JsonProperty("core_avg_pcp_rank")
    private Integer coreAvgPcpRank;
    @JsonProperty("core_avg_pcp_rank_change")
    private Integer coreAvgPcpRankChange;
    @JsonProperty("fall_count")
    private Integer fallCount;
    @JsonProperty("fund_flow")
    private Double fundFlow;
    @JsonProperty("limit_up_count")
    private Integer limitUpCount;
    @JsonProperty("plate_id")
    private Integer plateId;
    @JsonProperty("plate_name")
    private String plateName;
    @JsonProperty("rise_count")
    private Integer riseCount;
    @JsonProperty("stay_count")
    private Integer stayCount;
    @JsonProperty("top_n_stocks")
    private TopNStocksDTO topNStocks;

    @NoArgsConstructor
    @Data
    public static class BottomNStocksDTO {
        @JsonProperty("items")
        private List<ItemsDTO> items;

        @NoArgsConstructor
        @Data
        public static class ItemsDTO {
            @JsonProperty("symbol")
            private String symbol;
            @JsonProperty("change_percent")
            private Double changePercent;
            @JsonProperty("stock_chi_name")
            private String stockChiName;
            @JsonProperty("price_change")
            private Double priceChange;
        }
    }

    @NoArgsConstructor
    @Data
    public static class TopNStocksDTO {
        @JsonProperty("items")
        private List<ItemsDTO> items;

        @NoArgsConstructor
        @Data
        public static class ItemsDTO {
            @JsonProperty("change_percent")
            private Double changePercent;
            @JsonProperty("price_change")
            private Double priceChange;
            @JsonProperty("stock_chi_name")
            private String stockChiName;
            @JsonProperty("symbol")
            private String symbol;
        }
    }
}