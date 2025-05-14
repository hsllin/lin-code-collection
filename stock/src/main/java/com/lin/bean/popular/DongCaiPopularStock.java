package com.lin.bean.popular;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-04-27 17:44
 */
@NoArgsConstructor
@Data
public class DongCaiPopularStock {

    @JsonProperty("rc")
    private Integer rc;
    @JsonProperty("rt")
    private Integer rt;
    @JsonProperty("svr")
    private Integer svr;
    @JsonProperty("lt")
    private Integer lt;
    @JsonProperty("full")
    private Integer full;
    @JsonProperty("dlmkts")
    private String dlmkts;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("diff")
        private List<DiffDTO> diff;

        @NoArgsConstructor
        @Data
        public static class DiffDTO {
            @JsonProperty("f1")
            private Integer f1;
            @JsonProperty("f2")
            private Double f2;
            @JsonProperty("f3")
            private Double f3;
            @JsonProperty("f4")
            private Double f4;
            @JsonProperty("f12")
            private String f12;
            @JsonProperty("f13")
            private Integer f13;
            @JsonProperty("f14")
            private String f14;
            @JsonProperty("f15")
            private Double f15;
            @JsonProperty("f16")
            private Double f16;
            @JsonProperty("f152")
            private Integer f152;
        }
    }
}