package com.lin.bean.intradaychange;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class IntradayChange {

    private Integer rc;
    private Integer rt;
    private Integer svr;
    private Integer lt;
    private Integer full;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private Integer tc;
        private List<AllstockBean> allstock;

        @NoArgsConstructor
        @Data
        public static class AllstockBean {
            private Integer tm;
            private String c;
            private Integer m;
            private String n;
            private Integer t;
            private String i;
            private Integer count=1;
            private String time;
            private String industry;
            private double percent;
        }
    }
}
