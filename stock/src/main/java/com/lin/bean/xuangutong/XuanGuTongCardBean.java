package com.lin.bean.xuangutong;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-06-16 17:18
 */
@NoArgsConstructor
@Data
public class XuanGuTongCardBean {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("type")
        private String type;
        @JsonProperty("items")
        private List<ItemsDTO> items;
        @JsonProperty("updated_at")
        private Integer updatedAt;
        @JsonProperty("order")
        private Integer order;

        @NoArgsConstructor
        @Data
        public static class ItemsDTO {
            @JsonProperty("plate_id")
            private String plateId;
            @JsonProperty("plate_name")
            private String plateName;
            @JsonProperty("description")
            private String description;
            @JsonProperty("stocks")
            private List<StocksDTO> stocks;
            private Double plateRate;

            @NoArgsConstructor
            @Data
            public static class StocksDTO {
                @JsonProperty("name")
                private String name;
                @JsonProperty("symbol")
                private String symbol;
                private String price;
                private String rate;
            }
        }
    }
}