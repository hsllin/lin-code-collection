package com.lin.bean.market;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class MarketTimeLineBean {

    private Integer code;
    private DataBean data;
    private String msg;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private List<TimelineBean> timeline;
        private Integer td;
        private Integer tm;

        @NoArgsConstructor
        @Data
        public static class TimelineBean {
            private Integer tm;
            private Integer idx;
            private Integer zt;
            private Integer dt;
        }
    }
}
