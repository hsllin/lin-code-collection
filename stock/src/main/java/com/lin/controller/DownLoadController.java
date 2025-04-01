package com.lin.controller;

import com.lin.service.*;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DownLoadController {
    @Autowired
    StrongStockService strongStockService;
    @Autowired
    LimitUpPoolService limitUpPoolService;
    @Autowired
    LimitDownPoolService limitDownPoolService;

    @Autowired
    LianBanChiService lianBanChiService;
    @Autowired
    VolumeTrendService volumeTrendService;


    @RequestMapping("/downloadAllData")
    public ResponseEntity<Boolean> downloadAllData(HttpServletRequest request, Model model) {
        String dateIndex = "0";
        String date = CommonUtils.getTradeDay(Integer.valueOf(dateIndex));

        strongStockService.downloadStrongSrockData(date);
        limitUpPoolService.downloadLimitUpData();
        limitDownPoolService.downloadLimitDownData();
        lianBanChiService.downloadData(date);
        volumeTrendService.saveOrUpdateData();
        return ResponseEntity.ok(true);
    }
}
