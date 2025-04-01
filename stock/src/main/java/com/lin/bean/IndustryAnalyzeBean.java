package com.lin.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class IndustryAnalyzeBean {

    private String msg;
    private DataBean data;
    private String errCode;
    private Integer serverTime;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private Integer pageNo;
        private Integer pageSize;
        private Object orderBy;
        private Object order;
        private Boolean autoCount;
        private Object map;
        private String params;
        private List<ResultBean> result;
        private Integer totalCount;
        private Integer totalPages;
        private Boolean hasNext;
        private Integer nextPage;
        private Boolean hasPre;
        private Integer prePage;
        private Integer first;

        @NoArgsConstructor
        @Data
        public static class ResultBean {
            private String industry_id;
            private Integer title_red;
            private Integer title_bold;
            private String title;
            private Object author;
            private String imgs;
            private String keyword;
            private String content;
            private Integer is_top;
            private Integer status;
            private Integer sort_no;
            private Integer forward_count;
            private Integer browsers_count;
            private String is_delete;
            private Object delete_time;
            private String create_time;
            private String update_time;
        }
    }
}
