package com.lin.bean.lianban;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-12 17:56
 */
@NoArgsConstructor
@Data
public class ZhaBanBean {

    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("status_msg")
    private String statusMsg;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("page")
        private PageDTO page;
        @JsonProperty("info")
        private List<InfoDTO> info;
        @JsonProperty("limit_up_count")
        private LimitUpCountDTO limitUpCount;
        @JsonProperty("limit_down_count")
        private LimitDownCountDTO limitDownCount;
        @JsonProperty("date")
        private String date;
        @JsonProperty("msg")
        private Object msg;
        @JsonProperty("trade_status")
        private TradeStatusDTO tradeStatus;

        @NoArgsConstructor
        @Data
        public static class PageDTO {
            @JsonProperty("limit")
            private Integer limit;
            @JsonProperty("total")
            private Integer total;
            @JsonProperty("count")
            private Integer count;
            @JsonProperty("page")
            private Integer page;
        }

        @NoArgsConstructor
        @Data
        public static class LimitUpCountDTO {
            @JsonProperty("today")
            private TodayDTO today;
            @JsonProperty("yesterday")
            private YesterdayDTO yesterday;

            @NoArgsConstructor
            @Data
            public static class TodayDTO {
                @JsonProperty("num")
                private Integer num;
                @JsonProperty("history_num")
                private Integer historyNum;
                @JsonProperty("rate")
                private Double rate;
                @JsonProperty("open_num")
                private Integer openNum;
            }

            @NoArgsConstructor
            @Data
            public static class YesterdayDTO {
                @JsonProperty("num")
                private Integer num;
                @JsonProperty("history_num")
                private Integer historyNum;
                @JsonProperty("rate")
                private Double rate;
                @JsonProperty("open_num")
                private Integer openNum;
            }
        }

        @NoArgsConstructor
        @Data
        public static class LimitDownCountDTO {
            @JsonProperty("today")
            private TodayDTO today;
            @JsonProperty("yesterday")
            private YesterdayDTO yesterday;

            @NoArgsConstructor
            @Data
            public static class TodayDTO {
                @JsonProperty("num")
                private Integer num;
                @JsonProperty("history_num")
                private Integer historyNum;
                @JsonProperty("rate")
                private Double rate;
                @JsonProperty("open_num")
                private Integer openNum;
            }

            @NoArgsConstructor
            @Data
            public static class YesterdayDTO {
                @JsonProperty("num")
                private Integer num;
                @JsonProperty("history_num")
                private Integer historyNum;
                @JsonProperty("rate")
                private Double rate;
                @JsonProperty("open_num")
                private Integer openNum;
            }
        }

        @NoArgsConstructor
        @Data
        public static class TradeStatusDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;
            @JsonProperty("start_time")
            private String startTime;
            @JsonProperty("end_time")
            private String endTime;
        }

        @NoArgsConstructor
        @Data
        public static class InfoDTO {
            @JsonProperty("open_num")
            private Integer openNum;
            @JsonProperty("code")
            private String code;
            @JsonProperty("is_new")
            private Integer isNew;
            @JsonProperty("limit_up_suc_rate")
            private Object limitUpSucRate;
            @JsonProperty("currency_value")
            private Double currencyValue;
            @JsonProperty("market_id")
            private Integer marketId;
            @JsonProperty("is_again_limit")
            private Integer isAgainLimit;
            @JsonProperty("change_rate")
            private Double changeRate;
            @JsonProperty("turnover_rate")
            private Double turnoverRate;
            @JsonProperty("name")
            private String name;
            @JsonProperty("high_days_value")
            private Object highDaysValue;
            @JsonProperty("change_tag")
            private String changeTag;
            @JsonProperty("rise_rate")
            private Double riseRate;
            @JsonProperty("turnover")
            private Double turnover;
            @JsonProperty("market_type")
            private String marketType;
            @JsonProperty("latest")
            private Double latest;
            @JsonProperty("time_preview")
            private List<Double> timePreview;
        }
    }
}