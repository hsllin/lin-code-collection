package com.lin.controller;

import com.lin.bean.index.VolumeTrend;
import com.lin.service.VolumeTrendService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 成交量趋势
 */
@Controller
public class VolumeTrendController {
    @Autowired
    VolumeTrendService volumeTrendService;

    @RequestMapping("/getVolumeTrendData")
    public ResponseEntity<List<VolumeTrend>> getVolumeTrendData(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        List<VolumeTrend> list = volumeTrendService.getVolumeTrendData();

        return ResponseEntity.ok(list);
    }


    @RequestMapping("/updateVolumeTrendData")
    public ResponseEntity<Boolean> updateVolumeTrendData(HttpServletRequest request, Model model) {
        Boolean result = volumeTrendService.saveOrUpdateData();

        return ResponseEntity.ok(result);
    }

}
