package com.lin.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-21 17:24
 */
@Data
@TableName("guangzhou_civil_servant_recruitment")
public class ShengKaoDataBean {
    private String id;
    private String recruitmentUnit;
    private String recruitmentPosition;
    private String positionCode;
    private String parent_code;
    private String successfulApplicants;
    private String a10;
    private String a20;
    private String a30;
    private String a40;
    private String a50;
    private String a60;
    private String needNum;
    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时均填充
    private LocalDateTime created_at;
    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时均填充
    private LocalDateTime updated_at;

}