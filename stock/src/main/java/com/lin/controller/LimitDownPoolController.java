package com.lin.controller;

import com.lin.bean.lianban.LimitDownPoolResultBean;
import com.lin.service.LimitDownPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 连板池相关
 */
@Controller
public class LimitDownPoolController {
    @Autowired
    LimitDownPoolService limitDownPoolService;

    @RequestMapping("/getLimitDownPoolList")
    public ResponseEntity<List<LimitDownPoolResultBean>> list(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        List<LimitDownPoolResultBean> list = limitDownPoolService.getLimitDownPoolList(orderFiled, orderBy);

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/downloadLimitDownData")
    public ResponseEntity<Boolean> downloadLimitUpData(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        limitDownPoolService.downloadLimitDownData();

        return ResponseEntity.ok(true);
    }


}
