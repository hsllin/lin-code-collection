package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.bean.HotSpotBean;
import com.lin.service.HotSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HotSpotController {
    @Autowired
    HotSpotService hotSpotService;

    @RequestMapping("/getHotSpotNews")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<HotSpotBean.DataBean.FastNewsListBean>> list(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<HotSpotBean.DataBean.FastNewsListBean> list = hotSpotService.getNews();

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(list);
    }
}
