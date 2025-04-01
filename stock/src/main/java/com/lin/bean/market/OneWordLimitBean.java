package com.lin.bean.market;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class OneWordLimitBean {

    private Integer code;
    private DataBean data;
    private String msg;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private List<YzztBean> yzzt;
        private List<?> yzdt;

        @NoArgsConstructor
        @Data
        public static class YzztBean {
            private Integer sid;
            private String code;
            private String name;
            private Integer mkt;
        }
    }
}
