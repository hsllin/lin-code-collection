package com.lin.controller;

import com.lin.bean.lianban.LimitUpPoolResultBean;
import com.lin.service.LimitUpPoolService;
import com.lin.util.CommonUtils;
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
public class LimitUpPoolController {
    @Autowired
    LimitUpPoolService limitUpPoolService;

    @RequestMapping("/getLimitUpPoolList")
    public ResponseEntity<List<LimitUpPoolResultBean>> list(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        List<LimitUpPoolResultBean> list = limitUpPoolService.getLimitUpPoolList(date,orderFiled, orderBy);

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/downloadLimitUpData")
    public ResponseEntity<Boolean> downloadLimitUpData(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        limitUpPoolService.downloadLimitUpData(date);

        return ResponseEntity.ok(true);
    }


}
