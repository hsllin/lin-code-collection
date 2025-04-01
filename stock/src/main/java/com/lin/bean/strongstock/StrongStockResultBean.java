package com.lin.bean.strongstock;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class StrongStockResultBean {

    private Integer status_code;
    private List<DataBean> data;
    private String status_msg;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private String code;
        private String name;
        private Double change;
        private Integer limit_up_num;
        private Integer continuous_plate_num;
        private String high;
        private Integer high_num;
        private Integer days;
        private List<StockListBean> stock_list;

        @NoArgsConstructor
        @Data
        public static class StockListBean {
            private String first_limit_up_time;
            private String code;
            private String last_limit_up_time;
            private Integer is_new;
            private String concept;
            private Integer market_id;
            private Integer is_st;
            private Double change_rate;
            private String reason_info;
            private String high;
            private String reason_type;
            private Integer continue_num;
            private String name;
            private Integer high_days;
            private String change_tag;
            private String market_type;
            private Double latest;
        }
    }
}
