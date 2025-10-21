package com.lin.bean.taoguba;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-14 16:57
 */
@NoArgsConstructor
@Data
public class TaoGuBaHotStock {

    @JsonProperty("status")
    private Boolean status;
    @JsonProperty("errorCode")
    private Integer errorCode;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("dto")
    private List<DtoDTO> dto;
    @JsonProperty("_t")
    private Long t;

    @NoArgsConstructor
    @Data
    public static class DtoDTO {
        @JsonProperty("fullCode")
        private String fullCode;
        @JsonProperty("stockName")
        private String stockName;
        @JsonProperty("ranking")
        private Integer ranking;
        @JsonProperty("remark")
        private Object remark;
        @JsonProperty("continuenum")
        private Integer continuenum;
        @JsonProperty("stockGn")
        private Object stockGn;
        @JsonProperty("gnList")
        private List<GnListDTO> gnList;
        @JsonProperty("popularValue")
        private Integer popularValue;
        @JsonProperty("rankRate")
        private Object rankRate;
        @JsonProperty("implied")
        private Object implied;
        @JsonProperty("reason")
        private String reason;
        @JsonProperty("linkingBoard")
        private String linkingBoard;

        @NoArgsConstructor
        @Data
        public static class GnListDTO {
            @JsonProperty("ztgnSeq")
            private Integer ztgnSeq;
            @JsonProperty("gnName")
            private String gnName;
        }
    }
}