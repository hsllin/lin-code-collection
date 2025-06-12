package com.lin.bean.rps;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-15 14:42
 */
@NoArgsConstructor
@Data
public class RpsBean {

    @JsonProperty("block_name")
    private String blockName;
    @JsonProperty("trade_date")
    private String tradeDate;
    @JsonProperty("turnover_value")
    private Integer turnoverValue;
    @JsonProperty("t_market_cap")
    private Integer tMarketCap;
    @JsonProperty("pct_change")
    private Double pctChange;
    @JsonProperty("pct_change_5")
    private Double pctChange5;
    @JsonProperty("pct_change_10")
    private Double pctChange10;
    @JsonProperty("pct_change_15")
    private Double pctChange15;
    @JsonProperty("pct_change_20")
    private Double pctChange20;
    @JsonProperty("pct_change_50")
    private Double pctChange50;
    @JsonProperty("rps5")
    private Integer rps5;
    @JsonProperty("rps10")
    private Double rps10;
    @JsonProperty("rps15")
    private Double rps15;
    @JsonProperty("rps20")
    private Double rps20;
    @JsonProperty("rps50")
    private Double rps50;
    @JsonProperty("rps5_rank")
    private Integer rps5Rank;
    @JsonProperty("rps10_rank")
    private Integer rps10Rank;
    @JsonProperty("rps15_rank")
    private Integer rps15Rank;
    @JsonProperty("rps20_rank")
    private Integer rps20Rank;
    @JsonProperty("rps50_rank")
    private Integer rps50Rank;
    @JsonProperty("last_update")
    private String lastUpdate;
    @JsonProperty("rps_cnt")
    private Integer rpsCnt;
    @JsonProperty("rps_sum")
    private Double rpsSum;
    @JsonProperty("rps5_rank_b")
    private Integer rps5RankB;
    @JsonProperty("rps10_rank_b")
    private Integer rps10RankB;
    @JsonProperty("rps15_rank_b")
    private Integer rps15RankB;
    @JsonProperty("rps20_rank_b")
    private Integer rps20RankB;
    @JsonProperty("rps50_rank_b")
    private Integer rps50RankB;
}