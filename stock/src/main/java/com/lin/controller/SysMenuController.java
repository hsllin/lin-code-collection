package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.SysMenu;
import com.lin.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统菜单控制器
 */
@Controller
public class SysMenuController {
    
    @Autowired
    private SysMenuService sysMenuService;
    
    /**
     * 获取菜单树形结构（用于前端渲染）
     */
    @Cacheable(key = "menuTree:default", type = Cacheable.CacheType.DEFAULT, ttl = 3600)
    @RequestMapping("/api/menu/tree")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<SysMenu>> getMenuTree(HttpServletRequest request, Model model) {
        List<SysMenu> menuTree = sysMenuService.getMenuTree();
        return ResponseEntity.ok(menuTree);
    }
    
    /**
     * 获取所有菜单列表
     */
    @Cacheable(key = "menuList:default", type = Cacheable.CacheType.DEFAULT, ttl = 1800)
    @RequestMapping("/api/menu/list")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<SysMenu>> getAllMenus(HttpServletRequest request, Model model) {
        List<SysMenu> menus = sysMenuService.getAllEnabledMenus();
        return ResponseEntity.ok(menus);
    }
    
    /**
     * 根据ID获取菜单详情
     */
    @Cacheable(key = "menuDetail:#{#id}", type = Cacheable.CacheType.DEFAULT, ttl = 1800)
    @RequestMapping("/api/menu/{id}")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<SysMenu> getMenuById(@PathVariable Long id, HttpServletRequest request, Model model) {
        SysMenu menu = sysMenuService.getMenuById(id);
        if (menu != null) {
            return ResponseEntity.ok(menu);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 保存菜单
     */
    @RequestMapping(value = "/api/menu/save", method = RequestMethod.POST)
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> saveMenu(@RequestBody SysMenu menu, HttpServletRequest request, Model model) {
        boolean result = sysMenuService.saveMenu(menu);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 更新菜单
     */
    @RequestMapping(value = "/api/menu/update", method = RequestMethod.PUT)
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> updateMenu(@RequestBody SysMenu menu, HttpServletRequest request, Model model) {
        boolean result = sysMenuService.updateMenu(menu);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 删除菜单
     */
    @RequestMapping(value = "/api/menu/{id}", method = RequestMethod.DELETE)
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> deleteMenu(@PathVariable Long id, HttpServletRequest request, Model model) {
        boolean result = sysMenuService.deleteMenu(id);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 菜单管理页面
     */
    @RequestMapping("/menuManage")
    public String menuManagePage(HttpServletRequest request, Model model) {
        return "menuManage";
    }
}
