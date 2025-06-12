package com.lin.bean.risefall;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-19 18:05
 */
@NoArgsConstructor
@Data
public class StockIntroduce {
    private String code;
    private String name;
    private String industry;
    private String introduce;
}