package com.lin.bean;

import java.text.SimpleDateFormat;

public enum DateFormatEnum {

    /**
     * 日期-时分秒
     */
    DEFAULT("yyyy-MM-dd HH:mm:ss"),

    /**
     * 日期
     */
    DATE("yyyy-MM-dd"),
    /**
     * 日期-时分
     */
    DATE_HOUR_MIN("yyyy-MM-dd HH:mm"),


    /**
     * 时分秒
     */
    HOUR_MIN_SECORD("HH:mm:ss"),

    /**
     * 时分
     */
    HOUR_MIN("HH:mm"),

    DATE_WITH_OUT_LINE("yyyyMMdd")
    ;


    DateFormatEnum(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public static SimpleDateFormat getSimpleDateFormat(DateFormatEnum dateFormatEnum) {
        return new SimpleDateFormat(dateFormatEnum.getValue());
    }
}
