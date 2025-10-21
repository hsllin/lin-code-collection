package com.lin.bean.cailianshe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-16 20:03
 */
@NoArgsConstructor
@Data
public class Telegraph {

    @JsonProperty("errno")
    private Integer errno;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("roll_data")
        private List<RollDataDTO> rollData;
        @JsonProperty("is_vip")
        private Integer isVip;
        @JsonProperty("update_num")
        private Integer updateNum;
        @JsonProperty("banners")
        private Object banners;
        @JsonProperty("dynamic_tag")
        private DynamicTagDTO dynamicTag;

        @NoArgsConstructor
        @Data
        public static class DynamicTagDTO {
            @JsonProperty("id")
            private Integer id;
            @JsonProperty("title")
            private String title;
            @JsonProperty("category")
            private String category;
        }

        @NoArgsConstructor
        @Data
        public static class RollDataDTO {
            @JsonProperty("author_extends")
            private String authorExtends;
            @JsonProperty("assocFastFact")
            private AssocFastFactDTO assocFastFact;
            @JsonProperty("depth_extends")
            private String depthExtends;
            @JsonProperty("deny_comment")
            private Integer denyComment;
            @JsonProperty("level")
            private String level;
            @JsonProperty("reading_num")
            private Integer readingNum;
            @JsonProperty("content")
            private String content;
            @JsonProperty("in_roll")
            private Integer inRoll;
            @JsonProperty("recommend")
            private Integer recommend;
            @JsonProperty("confirmed")
            private Integer confirmed;
            @JsonProperty("jpush")
            private Integer jpush;
            @JsonProperty("img")
            private String img;
            @JsonProperty("user_id")
            private Integer userId;
            @JsonProperty("is_top")
            private Integer isTop;
            @JsonProperty("brief")
            private String brief;
            @JsonProperty("id")
            private Integer id;
            @JsonProperty("ctime")
            private Integer ctime;
            @JsonProperty("type")
            private Integer type;
            @JsonProperty("title")
            private String title;
            @JsonProperty("bold")
            private Integer bold;
            @JsonProperty("sort_score")
            private Integer sortScore;
            @JsonProperty("comment_num")
            private Integer commentNum;
            @JsonProperty("modified_time")
            private Integer modifiedTime;
            @JsonProperty("status")
            private Integer status;
            @JsonProperty("collection")
            private Integer collection;
            @JsonProperty("has_img")
            private Integer hasImg;
            @JsonProperty("category")
            private String category;
            @JsonProperty("shareurl")
            private String shareurl;
            @JsonProperty("share_img")
            private String shareImg;
            @JsonProperty("share_num")
            private Integer shareNum;
            @JsonProperty("sub_titles")
            private List<?> subTitles;
            @JsonProperty("tags")
            private List<?> tags;
            @JsonProperty("imgs")
            private List<?> imgs;
            @JsonProperty("images")
            private List<?> images;
            @JsonProperty("explain_num")
            private Integer explainNum;
            @JsonProperty("stock_list")
            private List<?> stockList;
            @JsonProperty("is_ad")
            private Integer isAd;
            @JsonProperty("ad")
            private AdDTO ad;
            @JsonProperty("subjects")
            private List<SubjectsDTO> subjects;
            @JsonProperty("audio_url")
            private List<String> audioUrl;
            @JsonProperty("author")
            private String author;
            @JsonProperty("plate_list")
            private List<?> plateList;
            @JsonProperty("assocArticleUrl")
            private String assocArticleUrl;
            @JsonProperty("assocVideoTitle")
            private String assocVideoTitle;
            @JsonProperty("assocVideoUrl")
            private String assocVideoUrl;
            @JsonProperty("assocCreditRating")
            private List<?> assocCreditRating;
            @JsonProperty("invest_calendar")
            private InvestCalendarDTO investCalendar;
            @JsonProperty("share_content")
            private String shareContent;
            @JsonProperty("gray_share")
            private Integer grayShare;
            @JsonProperty("comment_recommand")
            private Object commentRecommand;
            @JsonProperty("timeline")
            private Object timeline;

            @NoArgsConstructor
            @Data
            public static class AssocFastFactDTO {
                @JsonProperty("fid")
                private Integer fid;
                @JsonProperty("fast_fact_content")
                private String fastFactContent;
                @JsonProperty("article_id")
                private Integer articleId;
                @JsonProperty("article_title")
                private String articleTitle;
                @JsonProperty("imgs")
                private String imgs;
                @JsonProperty("slogan")
                private String slogan;
            }

            @NoArgsConstructor
            @Data
            public static class AdDTO {
                @JsonProperty("id")
                private Integer id;
                @JsonProperty("title")
                private String title;
                @JsonProperty("img")
                private String img;
                @JsonProperty("url")
                private String url;
                @JsonProperty("monitorUrl")
                private String monitorUrl;
                @JsonProperty("video_url")
                private String videoUrl;
                @JsonProperty("adTag")
                private String adTag;
                @JsonProperty("fullScreen")
                private Integer fullScreen;
                @JsonProperty("type")
                private Integer type;
            }

            @NoArgsConstructor
            @Data
            public static class InvestCalendarDTO {
                @JsonProperty("id")
                private Integer id;
                @JsonProperty("data_id")
                private Integer dataId;
                @JsonProperty("r_id")
                private String rId;
                @JsonProperty("type")
                private Integer type;
                @JsonProperty("calendar_time")
                private String calendarTime;
                @JsonProperty("setting_time")
                private String settingTime;
                @JsonProperty("event")
                private Object event;
                @JsonProperty("economic")
                private Object economic;
                @JsonProperty("short_latents")
                private Object shortLatents;
            }

            @NoArgsConstructor
            @Data
            public static class SubjectsDTO {
                @JsonProperty("article_id")
                private Integer articleId;
                @JsonProperty("subject_id")
                private Integer subjectId;
                @JsonProperty("subject_name")
                private String subjectName;
                @JsonProperty("subject_img")
                private String subjectImg;
                @JsonProperty("subject_description")
                private String subjectDescription;
                @JsonProperty("category_id")
                private Integer categoryId;
                @JsonProperty("attention_num")
                private Integer attentionNum;
                @JsonProperty("is_attention")
                private Boolean isAttention;
                @JsonProperty("is_reporter_subject")
                private Boolean isReporterSubject;
                @JsonProperty("plate_id")
                private Integer plateId;
                @JsonProperty("channel")
                private String channel;
            }
        }
    }
}