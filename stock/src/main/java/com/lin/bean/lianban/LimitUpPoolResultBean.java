package com.lin.bean.lianban;

import lombok.Data;

@Data
public class LimitUpPoolResultBean {
    private String code;
    private String name;
    private Double percent;
    private Double price;
    private String reason;
    private String firstTime;
    private String endTime;
    private String upType;
    private String boardNum;

    private String upMoney;
    private Double changeRate;
    private String totalPrice;

    @Override
    public String toString() {
        return "LimitUpPoolResultBean{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", percent=" + percent +
                ", price=" + price +
                ", reason='" + reason + '\'' +
                ", firstTime='" + firstTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", upType='" + upType + '\'' +
                ", boardNum='" + boardNum + '\'' +
                ", upMoney='" + upMoney + '\'' +
                ", changeRate=" + changeRate +
                ", totalPrice='" + totalPrice + '\'' +
                '}';
    }
}
