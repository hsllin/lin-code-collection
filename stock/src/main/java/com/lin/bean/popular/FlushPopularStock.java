package com.lin.bean.popular;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-04-27 14:33
 */
@NoArgsConstructor
@Data
public class FlushPopularStock {

    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("status_msg")
    private String statusMsg;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("stock_list")
        private List<StockListDTO> stockList;

        @NoArgsConstructor
        @Data
        public static class StockListDTO {
            @JsonProperty("market")
            private Integer market;
            @JsonProperty("code")
            private String code;
            @JsonProperty("rate")
            private String rate;
            @JsonProperty("rise_and_fall")
            private Double riseAndFall;
            @JsonProperty("name")
            private String name;
            @JsonProperty("analyse")
            private String analyse;
            @JsonProperty("hot_rank_chg")
            private Integer hotRankChg;
            @JsonProperty("topic")
            private TopicDTO topic;
            @JsonProperty("tag")
            private TagDTO tag;
            @JsonProperty("order")
            private Integer order;
            @JsonProperty("analyse_title")
            private String analyseTitle;

            @NoArgsConstructor
            @Data
            public static class TopicDTO {
                @JsonProperty("topic_code")
                private String topicCode;
                @JsonProperty("title")
                private String title;
                @JsonProperty("ios_jump_url")
                private String iosJumpUrl;
                @JsonProperty("android_jump_url")
                private String androidJumpUrl;
            }

            @NoArgsConstructor
            @Data
            public static class TagDTO {
                @JsonProperty("concept_tag")
                private List<String> conceptTag;
                @JsonProperty("popularity_tag")
                private String popularityTag;
            }
        }
    }
}