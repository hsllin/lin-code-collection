package com.lin.bean.index;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class GloabalIndex {

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
            private Integer f1;
            private Double f2;
            private Double f3;
            private Integer f4;
            private String f12;
            private Integer f13;
            private String f14;
            private Integer f152;
        }
    }
}
