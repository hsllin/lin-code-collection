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
 * @create 2025-06-16 17:57
 */
@NoArgsConstructor
@Data
public class XuanGuTongStock {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("fields")
        private List<String> fields;
        @JsonProperty("snapshot")
        private SnapshotDTO snapshot;

        @NoArgsConstructor
        @Data
        public static class SnapshotDTO {
            @JsonProperty("000554.SZ")
            private List<String> _$000554Sz123;// FIXME check this code
            @JsonProperty("000625.SZ")
            private List<String> _$000625Sz191;// FIXME check this code
            @JsonProperty("002490.SZ")
            private List<String> _$002490Sz152;// FIXME check this code
            @JsonProperty("002878.SZ")
            private List<String> _$002878Sz174;// FIXME check this code
            @JsonProperty("002887.SZ")
            private List<String> _$002887Sz241;// FIXME check this code
            @JsonProperty("002951.SZ")
            private List<String> _$002951Sz155;// FIXME check this code
        }
    }
}