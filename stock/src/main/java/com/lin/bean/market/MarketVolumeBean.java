package com.lin.bean.market;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class MarketVolumeBean {

    private Integer rc;
    private Integer rt;
    private Integer svr;
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
            private Integer f1;
            private Double f2;
            private Double f3;
            private Double f4;
            private Double f6;
            private String f12;
            private Integer f13;
            private Integer f104;
            private Integer f105;
            private Integer f106;
        }
    }
}
