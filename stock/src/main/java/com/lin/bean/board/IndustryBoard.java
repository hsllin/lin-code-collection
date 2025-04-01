package com.lin.bean.board;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class IndustryBoard {

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
            private Integer f2;
            private Integer f3;
            private Integer f4;
            private Integer f8;
            private String f12;
            private Integer f13;
            private String f14;
            private Long f20;
            private Integer f104;
            private Integer f105;
            private String f128;
            private String f140;
            private Integer f141;
            private Integer f136;
            private Integer f152;
            private String f207;
            private String f208;
            private Integer f209;
            private Integer f222;
        }
    }
}
