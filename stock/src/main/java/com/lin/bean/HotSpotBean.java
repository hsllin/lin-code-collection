package com.lin.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class HotSpotBean {

    private String code;
    private DataBean data;
    private String message;
    private String req_trace;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private List<FastNewsListBean> fastNewsList;
        private Integer index;
        private Integer size;
        private String sortEnd;
        private Integer total;

        @NoArgsConstructor
        @Data
        public static class FastNewsListBean {
            private String code;
            private List<?> image;
            private Integer pinglun_Num;
            private String realSort;
            private Integer share;
            private String showTime;
            private List<String> stockList;
            private String summary;
            private String title;
            private Integer titleColor;
            private String url;
        }
    }
}
