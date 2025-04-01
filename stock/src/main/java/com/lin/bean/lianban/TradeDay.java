package com.lin.bean.lianban;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class TradeDay {

    private Integer status_code;
    private DataBean data;
    private String status_msg;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private Integer code;
        private String msg;
        private List<String> next_dates;
        private List<String> prev_dates;
        private Boolean trade_day;
    }
}
