package com.lin.service;

import com.lin.bean.SysMenu;

import java.util.List;

/**
 * 系统菜单服务接口
 */
public interface SysMenuService {
    
    /**
     * 获取所有启用的菜单
     */
    List<SysMenu> getAllEnabledMenus();
    
    /**
     * 获取树形结构的菜单列表
     */
    List<SysMenu> getMenuTree();
    
    /**
     * 根据ID获取菜单
     */
    SysMenu getMenuById(Long id);
    
    /**
     * 保存菜单
     */
    boolean saveMenu(SysMenu menu);
    
    /**
     * 更新菜单
     */
    boolean updateMenu(SysMenu menu);
    
    /**
     * 删除菜单
     */
    boolean deleteMenu(Long id);
    
    /**
     * 批量删除菜单
     */
    boolean deleteMenus(List<Long> ids);
}
