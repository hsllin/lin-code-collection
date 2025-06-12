package com.lin.bean.risefall;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-19 17:20
 */
@NoArgsConstructor
@Data
public class DayRiseBean {

    @JsonProperty("5")
    private String a;
    @JsonAlias("199112")
    private String b;
    @JsonAlias("1968584")
    private String c;
    @JsonAlias("10")
    private String d;
    @JsonAlias("199112")
    private String e;
    @JsonAlias("13")
    private String f;
}