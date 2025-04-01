package com.lin.bean.lianban;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class LianBanNew {
    private Integer limitNum;
    private List<stock> stockList;

    @Data
    @NoArgsConstructor
    public static class stock {
        private String code;
        private String name;
        private String concept;
        private String price;
        private String firstTime;
    }

}
