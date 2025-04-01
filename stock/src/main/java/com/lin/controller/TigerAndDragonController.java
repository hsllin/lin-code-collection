package com.lin.controller;

import com.lin.bean.tigerdragon.LhbBean;
import com.lin.service.TigerAndDragonService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 龙虎榜
 */
@Controller
public class TigerAndDragonController {
    @Autowired
    TigerAndDragonService tigerAndDragonService;

    @RequestMapping("/getTigerAndDragonData")
    public ResponseEntity<LhbBean> getTigerAndDragonData(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        LhbBean bean = tigerAndDragonService.getTigerAndDragonData();

        return ResponseEntity.ok(bean);
    }

}
