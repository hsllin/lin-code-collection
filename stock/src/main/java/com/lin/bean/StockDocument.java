package com.lin.bean;

import java.util.List;

@lombok.NoArgsConstructor
@lombok.Data
public class StockDocument {

    private DataBean data;
    private String error;
    private Integer success;

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class DataBean {
        private List<ListBean> list;
        private Integer page_index;
        private Integer page_size;
        private Integer total_hits;

        @lombok.NoArgsConstructor
        @lombok.Data
        public static class ListBean {
            private String art_code;
            private List<CodesBean> codes;
            private List<ColumnsBean> columns;
            private String display_time;
            private String eiTime;
            private String language;
            private String listing_state;
            private String notice_date;
            private String product_code;
            private String sort_date;
            private String source_type;
            private String title;
            private String title_ch;
            private String title_en;

            @lombok.NoArgsConstructor
            @lombok.Data
            public static class CodesBean {
                private String ann_type;
                private String inner_code;
                private String market_code;
                private String short_name;
                private String stock_code;
            }

            @lombok.NoArgsConstructor
            @lombok.Data
            public static class ColumnsBean {
                private String column_code;
                private String column_name;
            }
        }
    }
}
