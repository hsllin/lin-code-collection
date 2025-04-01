package com.lin.bean.tigerdragon;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class LhbBean {

    private Integer code;
    private DataBean data;
    private String msg;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private Integer total;
        private List<LhbStocksBean> lhbStocks;

        @NoArgsConstructor
        @Data
        public static class LhbStocksBean {
            private String endDate;
            private Integer market;
            private Integer stockId;
            private String stockCode;
            private String stockName;
            private Integer infoClassCode;
            private String infoClassName;
            private Double netValueTotal;
            private Double buyValueTotal;
            private Double sellValueTotal;
            private Double closeValue;
            private Double value;
            private Double chg;
            private Double volume;
            private LhbBranchBean lhbBranch;

            @NoArgsConstructor
            @Data
            public static class LhbBranchBean {
                private List<BuyBranchesBean> buyBranches;
                private List<SellBranchesBean> sellBranches;
                private Double buyValueTotal;
                private Double buyRatioTotal;
                private Double sellValueTotal;
                private Double sellRatioTotal;
                private Double netValueTotal;

                @NoArgsConstructor
                @Data
                public static class BuyBranchesBean {
                    private Integer branchCode;
                    private String branchName;
                    private Double buyValue;
                    private Double buyRatio;
                    private Double sellValue;
                    private Double sellRatio;
                    private Double netValue;
                }

                @NoArgsConstructor
                @Data
                public static class SellBranchesBean {
                    private Integer branchCode;
                    private String branchName;
                    private Double buyValue;
                    private Double buyRatio;
                    private Double sellValue;
                    private Double sellRatio;
                    private Double netValue;
                }
            }
        }
    }
}
