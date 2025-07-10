package com.lin.bean.jfzt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-06-25 22:10
 */
@NoArgsConstructor
@Data
public class InflowAndOutFlowBean {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("currentTime")
    private Long currentTime;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("message")
    private String message;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("list")
        private List<ListDTO> list;
        @JsonProperty("total")
        private Integer total;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @JsonProperty("large_grp_pure_in")
            private Integer largeGrpPureIn;
            @JsonProperty("last_price")
            private Integer lastPrice;
            @JsonProperty("little_grp_pure_in")
            private Long littleGrpPureIn;
            @JsonProperty("medium_grp_pure_in")
            private Long mediumGrpPureIn;
            @JsonProperty("name")
            private String name;
            @JsonProperty("prodCode")
            private String prodCode;
            @JsonProperty("prodType")
            private String prodType;
            @JsonProperty("pure_in")
            private Long pureIn;
            @JsonProperty("px_change_ratio")
            private Integer pxChangeRatio;
            @JsonProperty("super_grp_pure_in")
            private Long superGrpPureIn;
        }
    }
}