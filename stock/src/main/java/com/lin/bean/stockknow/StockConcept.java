package com.lin.bean.stockknow;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
public class StockConcept {
    private String id;
    private String name;
    private String description;
    @TableField(fill = FieldFill.INSERT)  // 仅插入时自动填充
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时均填充
    private LocalDateTime updateDate;

    public StockConcept(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}