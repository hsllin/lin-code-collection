package com.lin.bean.lianban;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class LimitDownBean {

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
            private String code;
            private Integer is_new;
            private Double currency_value;
            private Integer market_id;
            private Integer is_again_limit;
            private Double change_rate;
            private Long last_limit_down_time;
            private Double turnover_rate;
            private Long first_limit_down_time;
            private String name;
            private Object high_days_value;
            private String change_tag;
            private String market_type;
            private Double latest;
            private List<Double> time_preview;
        }
    }
}
