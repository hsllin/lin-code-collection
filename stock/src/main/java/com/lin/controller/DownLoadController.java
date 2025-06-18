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

    @Autowired
    ZhaBanChiService zhaBanChiService;

    @Autowired
    IncreaseAndDecreaseService increaseAndDecreaseService;
    @Autowired
    PopularStockService popularStockService;


    @RequestMapping("/downloadAllData")
    public ResponseEntity<Boolean> downloadAllData(HttpServletRequest request, Model model) {
        String dateIndex = "0";
        String date = CommonUtils.getTradeDay(Integer.valueOf(dateIndex));
//        date="20250523";
        strongStockService.downloadStrongSrockData(date);
        limitUpPoolService.downloadLimitUpData(date);
        limitDownPoolService.downloadLimitDownData(date);
        lianBanChiService.downloadData(date);
        volumeTrendService.saveOrUpdateData();
        zhaBanChiService.downloadData(date);
        increaseAndDecreaseService.downloadTrendData();
        increaseAndDecreaseService.generateTrendDataPhoto();
        popularStockService.downLoadHotBoardAndConceptData();
        return ResponseEntity.ok(true);
    }
}
