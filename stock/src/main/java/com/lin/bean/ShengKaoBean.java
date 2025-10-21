package com.lin.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-21 17:10
 */
@NoArgsConstructor
@Data
public class ShengKaoBean {

    @JsonProperty("rows")
    private List<RowsDTO> rows;
    @JsonProperty("total")
    private Integer total;

    @NoArgsConstructor
    @Data
    public static class RowsDTO {
        @JsonProperty("aab004")
        private String aab004;
        @JsonProperty("aab019")
        private Integer aab019;
        @JsonProperty("aab119")
        private Integer aab119;
        @JsonProperty("aab120")
        private Integer aab120;
        @JsonProperty("aae036")
        private Long aae036;
        @JsonProperty("bab301")
        private String bab301;
        @JsonProperty("bfe301")
        private String bfe301;
        @JsonProperty("bfe3a4")
        private String bfe3a4;
        @JsonProperty("id")
        private IdDTO id;

        @NoArgsConstructor
        @Data
        public static class IdDTO {
            @JsonProperty("bfa001")
            private String bfa001;
            @JsonProperty("bfz315")
            private String bfz315;
        }
    }
}