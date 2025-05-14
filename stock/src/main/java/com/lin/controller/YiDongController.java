package com.lin.controller;

import com.lin.bean.YiDong;
import com.lin.bean.strongstock.StrongStockBean;
import com.lin.service.StrongStockService;
import com.lin.service.YidongService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 获取同花顺异动信息
 * @create 2025-05-12 16:29
 */
@Controller
public class YiDongController {
    @Autowired
    YidongService yidongService;

    @RequestMapping("/getYidongList")
    public ResponseEntity<List<YiDong>> list(HttpServletRequest request, Model model) {
        List<YiDong> list = yidongService.getYidongList();
        return ResponseEntity.ok(list);
    }
}