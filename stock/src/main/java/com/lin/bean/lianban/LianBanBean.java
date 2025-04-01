package com.lin.bean.lianban;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class LianBanBean {

    private Integer status_code;
    private List<DataBean> data;
    private String status_msg;
    private String date;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private Integer height;
        private List<CodeListBean> code_list;
        private Integer number;

        @NoArgsConstructor
        @Data
        public static class CodeListBean {
            private String code;
            private String name;
            private Integer market_id;
            private Integer continue_num;
        }
    }
}
