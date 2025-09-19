package com.lin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.bean.SysMenu;
import com.lin.mapper.SysMenuMapper;
import com.lin.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统菜单服务实现类
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
    
    @Autowired
    private SysMenuMapper sysMenuMapper;
    
    @Override
    public List<SysMenu> getAllEnabledMenus() {
        return sysMenuMapper.selectAllEnabledMenus();
    }
    
    @Override
    public List<SysMenu> getMenuTree() {
        // 获取所有启用的菜单
        List<SysMenu> allMenus = getAllEnabledMenus();
        
        // 构建树形结构
        return buildMenuTree(allMenus, 0L);
    }
    
    @Override
    public SysMenu getMenuById(Long id) {
        return sysMenuMapper.selectById(id);
    }
    
    @Override
    public boolean saveMenu(SysMenu menu) {
        return sysMenuMapper.insert(menu) > 0;
    }
    
    @Override
    public boolean updateMenu(SysMenu menu) {
        return sysMenuMapper.updateById(menu) > 0;
    }
    
    @Override
    public boolean deleteMenu(Long id) {
        return sysMenuMapper.deleteById(id) > 0;
    }
    
    @Override
    public boolean deleteMenus(List<Long> ids) {
        return sysMenuMapper.deleteBatchIds(ids) > 0;
    }
    
    /**
     * 构建菜单树
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> allMenus, Long parentId) {
        List<SysMenu> result = new ArrayList<>();
        
        for (SysMenu menu : allMenus) {
            if (parentId.equals(menu.getParentId())) {
                // 递归查找子菜单
                List<SysMenu> children = buildMenuTree(allMenus, menu.getId());
                menu.setChildren(children);
                result.add(menu);
            }
        }
        
        return result;
    }
}
