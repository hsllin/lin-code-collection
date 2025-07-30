package com.lin.bean.pluginking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-07-30 11:31
 */
@NoArgsConstructor
@Data
public class LiveStreamingBean {

    @JsonProperty("JHJJYD")
    private List<String> jhjjyd;
    @JsonProperty("List")
    private List<ListDTO> list;
    @JsonProperty("Notice")
    private String notice;
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("Time")
    private Integer time;
    @JsonProperty("date")
    private String date;
    @JsonProperty("ttag")
    private Double ttag;
    @JsonProperty("errcode")
    private String errcode;

    @NoArgsConstructor
    @Data
    public static class ListDTO {
        @JsonProperty("ID")
        private String id;
        @JsonProperty("UID")
        private String uid;
        @JsonProperty("Time")
        private Integer time;
        @JsonProperty("Comment")
        private String comment;
        @JsonProperty("Type")
        private String type;
        @JsonProperty("PlateCode")
        private String plateCode;
        @JsonProperty("PlateName")
        private String plateName;
        @JsonProperty("PlateJE")
        private String plateJE;
        @JsonProperty("PlateZDF")
        private String plateZDF;
        @JsonProperty("ThemeInfo")
        private List<?> themeInfo;
        @JsonProperty("Interpretation")
        private String interpretation;
        @JsonProperty("IsChart")
        private String isChart;
        @JsonProperty("UserName")
        private String userName;
        @JsonProperty("Image")
        private String image;
        @JsonProperty("Stock")
        private List<List<String>> stock;
        @JsonProperty("DisStock")
        private List<List<String>> disStock;
        @JsonProperty("styleIndex")
        private List<?> styleIndex;
        @JsonProperty("BoomReason")
        private String boomReason;
    }
}