package com.lin.bean.lianban;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class LimitUpPoolBean {

    private Integer status_code;
    private DataBean data;
    private String status_msg;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private PageBean page;
        private List<InfoBean> info;
        private LimitUpCountBean limit_up_count;
        private LimitDownCountBean limit_down_count;
        private String date;
        private Object msg;
        private TradeStatusBean trade_status;

        @NoArgsConstructor
        @Data
        public static class PageBean {
            private Integer limit;
            private Integer total;
            private Integer count;
            private Integer page;
        }

        @NoArgsConstructor
        @Data
        public static class LimitUpCountBean {
            private TodayBean today;
            private YesterdayBean yesterday;

            @NoArgsConstructor
            @Data
            public static class TodayBean {
                private Integer num;
                private Integer history_num;
                private Double rate;
                private Integer open_num;
            }

            @NoArgsConstructor
            @Data
            public static class YesterdayBean {
                private Integer num;
                private Integer history_num;
                private Double rate;
                private Integer open_num;
            }
        }

        @NoArgsConstructor
        @Data
        public static class LimitDownCountBean {
            private TodayBean today;
            private YesterdayBean yesterday;

            @NoArgsConstructor
            @Data
            public static class TodayBean {
                private Integer num;
                private Integer history_num;
                private Double rate;
                private Integer open_num;
            }

            @NoArgsConstructor
            @Data
            public static class YesterdayBean {
                private Integer num;
                private Integer history_num;
                private Double rate;
                private Integer open_num;
            }
        }

        @NoArgsConstructor
        @Data
        public static class TradeStatusBean {
            private String id;
            private String name;
            private String start_time;
            private String end_time;
        }

        @NoArgsConstructor
        @Data
        public static class InfoBean {
            private Object open_num;
            private Long first_limit_up_time;
            private Long last_limit_up_time;
            private String code;
            private String limit_up_type;
            private Double order_volume;
            private Integer is_new;
            private Double limit_up_suc_rate;
            private Double currency_value;
            private Integer market_id;
            private Integer is_again_limit;
            private Double change_rate;
            private Double turnover_rate;
            private String reason_type;
            private Double order_amount;
            private String high_days;
            private String name;
            private Integer high_days_value;
            private String change_tag;
            private String market_type;
            private Double latest;
//            private List<Double> time_preview;
        }
    }
}
