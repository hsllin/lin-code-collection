package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.bean.lianban.LimitDownPoolResultBean;
import com.lin.service.LimitDownPoolService;
import com.lin.service.SysnConceptStockService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 股票了解
 */
@Controller
public class StockMarketConceptBoardController {
    @Autowired
    LimitDownPoolService limitDownPoolService;
    @Autowired
    SysnConceptStockService sysnConceptStockService;

    @RequestMapping("/getStockKnownList")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<LimitDownPoolResultBean>> list(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        List<LimitDownPoolResultBean> list = limitDownPoolService.getLimitDownPoolList(date, orderFiled, orderBy);

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/addOrEditStockKnownData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> addOrEditStockKnownData(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        limitDownPoolService.downloadLimitDownData(date);

        return ResponseEntity.ok(true);
    }

    @RequestMapping("/deleteStockKnownData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> deleteStockKnownData(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        limitDownPoolService.downloadLimitDownData(date);

        return ResponseEntity.ok(true);
    }

    @RequestMapping("/sysnConceptData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> sysnConceptData(HttpServletRequest request, Model model) {
        sysnConceptStockService.sysnData();
        return ResponseEntity.ok(true);
    }


}
