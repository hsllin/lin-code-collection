package com.lin.bean.rps;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-15 15:29
 */
@NoArgsConstructor
@Data
public class RpsUserInfo implements Serializable {

    @JsonProperty("token")
    private String token;
    @JsonProperty("user")
    private UserDTO user;

    @NoArgsConstructor
    @Data
    public static class UserDTO {
        @JsonProperty("sid")
        private Integer sid;
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("user_name")
        private String userName;
        @JsonProperty("role")
        private Integer role;
        @JsonProperty("expired_date")
        private String expiredDate;
        @JsonProperty("remain_days")
        private Integer remainDays;
        @JsonProperty("max_custom_stock")
        private Integer maxCustomStock;
        @JsonProperty("try_out_flg")
        private Integer tryOutFlg;
    }
}