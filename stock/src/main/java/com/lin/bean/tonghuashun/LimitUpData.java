package com.lin.bean.tonghuashun;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-29 11:18
 */
@Data
public class LimitUpData extends TonghuashunBaseBean {
    private String limitUpTime;
    private String limitUpNum;
    private String limitUpHandNum;
    private String limitUpType;
    private String limitUpNumType;

    private String limitUpMoney;
    private String flowMoney;

}