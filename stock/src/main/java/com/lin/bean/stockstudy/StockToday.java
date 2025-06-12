package com.lin.bean.stockstudy;

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
 * @create 2025-06-03 14:39
 */
@Data
@TableName("stock_today")
public class StockToday {
    @TableId("code")
    private String code;
    @TableField(fill = FieldFill.INSERT)  // 仅插入时自动填充
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时均填充
    private LocalDateTime updateDate;
}