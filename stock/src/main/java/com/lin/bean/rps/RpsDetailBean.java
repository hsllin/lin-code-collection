package com.lin.bean.rps;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-15 15:03
 */
@NoArgsConstructor
@Data
public class RpsDetailBean {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("name")
    private String name;
    @JsonProperty("close")
    private Double close;
    @JsonProperty("day_rate")
    private Integer dayRate;
    @JsonProperty("rps10")
    private Double rps10;
    @JsonProperty("rps20")
    private Double rps20;
    @JsonProperty("rps50")
    private Double rps50;
    @JsonProperty("rps120")
    private Double rps120;
    @JsonProperty("rps250")
    private Double rps250;
    @JsonProperty("ts_code")
    private String tsCode;
    @JsonProperty("t_market_cap")
    private Integer tMarketCap;
    @JsonProperty("turnover_value")
    private Integer turnoverValue;
    @JsonProperty("industry")
    private String industry;
    @JsonProperty("is_fond")
    private String isFond;
}