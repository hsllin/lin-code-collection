package com.lin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.stockknow.StockMarket;
import com.lin.service.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 股票录入概念
 */
@Controller
public class StockMarktetController {
    @Autowired
    StockMarketService marketService;

    @RequestMapping("/getStockMarketList")
    public ResponseEntity<Page<StockMarket>> getStockMarketList(HttpServletRequest request, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size, Model model) {
        String keyword = request.getParameter("keyword");
        Page<StockMarket> list = marketService.getStockMarketList(keyword, current, size);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addOrEditStockMarketData")
    @ResponseBody
    public ResponseEntity<Boolean> addOrEditStockKnownData(HttpServletRequest request, @RequestBody StockMarket stockMarket) {
        boolean result = marketService.addOrEditStockMarket(stockMarket);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteStockMarket")
    public ResponseEntity<Boolean> deleteStockMarket(HttpServletRequest request, String id) {
        boolean result = marketService.deleteStockMarket(id);
        return ResponseEntity.ok(result);
    }



}
