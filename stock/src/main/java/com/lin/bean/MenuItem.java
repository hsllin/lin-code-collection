package com.lin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuItem {
    private String title;
    private String url;
    private boolean active;

}
