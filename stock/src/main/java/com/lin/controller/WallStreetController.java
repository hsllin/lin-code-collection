package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.bean.NewsBean;
import com.lin.service.WallStreetNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class WallStreetController {
    @Autowired
    WallStreetNewsService wallStreetNewsService;

    @RequestMapping("/getWallStreetNews")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<NewsBean.DataBean.ItemsBean>> list(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<NewsBean.DataBean.ItemsBean> list = wallStreetNewsService.getNews();

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(list);
    }
}
