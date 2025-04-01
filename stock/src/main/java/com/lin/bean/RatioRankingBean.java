package com.lin.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class RatioRankingBean {

    private Integer rc;
    private Integer rt;
    private Long svr;
    private Integer lt;
    private Integer full;
    private String dlmkts;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private Integer total;
        private List<DiffBean> diff;

        @NoArgsConstructor
        @Data
        public static class DiffBean {
            private Double f1;
            private Double f2;
            private Double f3;
            private Double f4;
            private Double f5;
            private Double f6;
            private Double f7;
            private Double f8;
            private Double f9;
            private Double f10;
            private String f12;
            private Double f13;
            private String f14;
            private Double f15;
            private Double f16;
            private Double f17;
            private Double f18;
            private Double f23;
            private Double f152;
        }
    }
}
