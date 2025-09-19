package com.lin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bean.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统菜单Mapper接口
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    
    /**
     * 查询所有启用的菜单，按排序字段排序
     */
    @Select("SELECT * FROM sys_menu WHERE status = 1 ORDER BY sort_order ASC, id ASC")
    List<SysMenu> selectAllEnabledMenus();
    
    /**
     * 根据父菜单ID查询子菜单
     */
    @Select("SELECT * FROM sys_menu WHERE parent_id = #{parentId} AND status = 1 ORDER BY sort_order ASC, id ASC")
    List<SysMenu> selectMenusByParentId(Long parentId);
    
    /**
     * 查询顶级菜单（父菜单ID为0）
     */
    @Select("SELECT * FROM sys_menu WHERE parent_id = 0 AND status = 1 ORDER BY sort_order ASC, id ASC")
    List<SysMenu> selectTopLevelMenus();
}
