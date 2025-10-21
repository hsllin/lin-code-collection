package com.lin.bean;

import lombok.Data;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 市场情绪
 * @create 2025-10-18 11:25
 */
@Data
public class MarketSentiment {
    private Integer totalNum;
    private String totalMoney;

    private Integer limitUpNum;
    private Integer limitDownNum;

    private Integer totalUpNum;
    private Integer totalDownNum;

    private String shangzhengUpNum;
    private String shangzhengDownNum;

    private String shenzhenUpNum;
    private String shenzhengDownNum;

    private Integer fiveUpNum;
    private Integer fiveDownNum;
    private double zhaBanRate;

    private Integer zhaBanNum;

    private Integer yesterDayTwoLimitUpNum;

    private Integer yesterDayLimitUpNum;

    private Double yesterDayTwoLimitUpRate;

    private Double yesterDayLimitUpRate;

    private Double temperature;


    private Double shanZhengRate;
    private Double shenZhengRate;
    private Double chuangyeBanRate;
    private Double huShenRate;
    private Double zhongZheng500Rate;
    private Double daoQiongsiRate;
    private Double riJing225Rate;
    private Double hengShengRate;
    private Double hengShengKeJiRate;

    private double shanZhengCurrent;
    private Integer lianBanNum;

}