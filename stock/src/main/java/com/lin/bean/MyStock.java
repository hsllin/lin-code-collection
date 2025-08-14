package com.lin.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-30 15:54
 */
@Data
@TableName("my_stock")
public class MyStock {
    @TableId
    private String code;
    private String name;
    private String concepts;
    private String industry;
    private String mainBusiness;
    private String companyIntroduce;
    private Date study_time;
    private String studyStatus;
    private String marketCap;
    private String marketValue;
    private String profitLoss;
    private String price;
    private String companyLocation;
    private String pe;
    private String description;
    private double rate;
    private double turnoverRate;
    private String volality;
    private String tradingVolume;
    private String minPrice;
    private String maxPrice;
    private String quantityRatio;

    @TableField(fill = FieldFill.INSERT)  // 仅插入时自动填充
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时均填充
    private LocalDateTime updateDate;

}