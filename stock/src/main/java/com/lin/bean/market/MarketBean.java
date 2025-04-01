package com.lin.bean.market;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class MarketBean {

    private Integer code;
    private DataBean data;
    private String msg;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private Integer tradedate;
        private StockBean stock;
        private Double temperature;
        private Integer aUp;
        private Integer aDown;
        private Integer bUp;
        private Integer bDown;
        private Double totalMoney;
        private MoneyBean moneyBean;

        @NoArgsConstructor
        @Data
        public static class StockBean {
            private Integer total;
            private Integer stopped;
            private Integer zt;
            private Integer dt;
            private Integer up5p;
            private Integer down5p;
            private Integer zero;
            private Integer up;
            private Integer down;
            private Integer limit1;
            private Integer limit2;
            private Integer limit3;
            private Integer limit4;
            private Integer limit5;
            private Integer brokeLimitNum;
            private List<BucketsBean> buckets;

            @NoArgsConstructor
            @Data
            public static class BucketsBean {
                private Integer stockNum;
                private Integer lookRise;
                private String name;
            }
        }

        @NoArgsConstructor
        @Data
        public static class MoneyBean {
            private String aMoney;
            private String aUp;
            private String aDown;
            private String bMoney;
            private String bUp;
            private String bDown;
            private String totalMoney;
            private String totalOriginalMoney;

        }
    }
}
