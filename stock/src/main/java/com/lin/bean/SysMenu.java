package com.lin.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统菜单实体类
 */
@TableName("sys_menu")
public class SysMenu {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 父菜单ID，0表示顶级菜单
     */
    private Long parentId;
    
    /**
     * 菜单名称
     */
    private String menuName;
    
    /**
     * 菜单图标
     */
    private String menuIcon;
    
    /**
     * 菜单链接地址
     */
    private String menuUrl;
    
    /**
     * 菜单类型：1-目录，2-菜单，3-按钮
     */
    private Integer menuType;
    
    /**
     * 显示顺序
     */
    private Integer sortOrder;
    
    /**
     * 菜单状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 子菜单列表（非数据库字段）
     */
    private List<SysMenu> children;
    
    // 构造函数
    public SysMenu() {}
    
    public SysMenu(Long parentId, String menuName, String menuIcon, String menuUrl, Integer menuType, Integer sortOrder) {
        this.parentId = parentId;
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.menuUrl = menuUrl;
        this.menuType = menuType;
        this.sortOrder = sortOrder;
        this.status = 1;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    
    public String getMenuIcon() {
        return menuIcon;
    }
    
    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }
    
    public String getMenuUrl() {
        return menuUrl;
    }
    
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }
    
    public Integer getMenuType() {
        return menuType;
    }
    
    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public List<SysMenu> getChildren() {
        return children;
    }
    
    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }
    
    @Override
    public String toString() {
        return "SysMenu{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", menuName='" + menuName + '\'' +
                ", menuIcon='" + menuIcon + '\'' +
                ", menuUrl='" + menuUrl + '\'' +
                ", menuType=" + menuType +
                ", sortOrder=" + sortOrder +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
