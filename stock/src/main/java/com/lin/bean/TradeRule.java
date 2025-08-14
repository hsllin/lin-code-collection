package com.lin.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-08-04 13:53
 */
@Data
@TableName("trade_rules")
public class TradeRule {
    private Integer id;
    private String content;
    
    @TableField("sort_order")
    private Integer sortOrder;
    
    @TableField("created_at")
    private String createdAt;
    
    @TableField("updated_at")
    private String updatedAt;
}