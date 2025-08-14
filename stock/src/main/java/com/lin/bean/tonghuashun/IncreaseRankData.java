package com.lin.bean.tonghuashun;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-29 11:18
 */
@Data
@NoArgsConstructor
public class IncreaseRankData extends TonghuashunBaseBean {
    private String tradeMoney;
    private double morningRate;

}