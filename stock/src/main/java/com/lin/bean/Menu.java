package com.lin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Menu {
    private String title;
    private String icon;
    private List<SubMenu> subMenus;
    private boolean expanded;

    // 子菜单类
    @AllArgsConstructor
    @Data
    public static class SubMenu {
        private String title;
        private String path;
        private boolean active;

        // 构造方法 + Getter/Setter
    }
}
