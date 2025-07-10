package com.lin.bean.tonghuashun;

import lombok.Data;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-29 11:18
 */
@Data
public class AutionTradingBean extends TonghuashunBaseBean {
    private String unMatchedNum;
    private String unMatchedMoney;
    private String type;
    private String ratingType;
    private String buyNum;

    private String buyMoney;
    private String flowMoney;

}