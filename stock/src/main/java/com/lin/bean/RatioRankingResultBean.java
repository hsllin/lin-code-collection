package com.lin.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class RatioRankingResultBean {

    private List<DataBean> dataBeanList;

    @Data
    @NoArgsConstructor
    public static class DataBean {
        private String code;
        private String name;
        private Double ratio;
        private Double increaseAndDecrease;
        private Double current;
        private Double changeRate;
        private Double amplitude;
        private Double min;
        private Double max;
        private Double total;
        private String sellMoney;
    }
}
