package com.lin.bean.stockknow;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-04-21 16:28
 */
@Data
@TableName("stock_market")
public class StockMarket {
    @TableId
    private String code;
    private String name;
    private String description;
    private String mainBusiness;
    private String location;
    private String profitLoss;
    private String delistingRisk;
    private String volume;
    @TableField(fill = FieldFill.INSERT)  // 仅插入时自动填充
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时均填充
    private LocalDateTime updateDate;
}