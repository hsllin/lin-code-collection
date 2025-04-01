package com.lin.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class NewsBean {

    private Integer code;
    private String message;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private Integer count;
        private List<ItemsBean> items;
        private String next_cursor;
        private String search_id;

        @NoArgsConstructor
        @Data
        public static class ItemsBean {
            private Object article;
            private Boolean ban_comment;
            private List<String> channels;
            private String content;
            private String content_more;
            private String content_text;
            private List<?> cover_images;
            private Integer display_time;
            private String global_channel_name;
            private String global_more_uri;
            private String highlight_title;
            private Integer id;
            private List<?> images;
            private Boolean is_calendar;
            private Boolean is_favourite;
            private Boolean is_priced;
            private Boolean is_scaling;
            private String reference;
            private Object related_themes;
            private Integer score;
            private List<?> symbols;
            private List<?> tags;
            private String title;
            private String type;
            private String uri;
            private String time;
        }
    }
}
