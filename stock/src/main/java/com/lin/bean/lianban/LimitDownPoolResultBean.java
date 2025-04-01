package com.lin.bean.lianban;

import lombok.Data;

@Data
public class LimitDownPoolResultBean {
    private String code;
    private String name;
    private Double percent;
    private Double price;
//    private String reason;
    private String firstTime;
    private String endTime;
//    private String upType;
//    private String boardNum;

//    private String upMoney;
    private Double changeRate;
    private String totalPrice;

}
