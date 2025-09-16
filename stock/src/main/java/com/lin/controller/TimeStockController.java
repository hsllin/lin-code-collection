package com.lin.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.TimeStock;
import com.lin.bean.TimeStockLine;
import com.lin.bean.stockknow.StockConcept;
import com.lin.service.TimeStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 股票概念
 */
@Controller
public class TimeStockController {
    @Autowired
    TimeStockService timeStockService;

    @RequestMapping("/getTimeStockList")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<TimeStock.DataDTO>> getTimeStockList(HttpServletRequest request, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                                    @RequestParam(value = "size", defaultValue = "10") Integer size, Model model) {
        String date = DateUtil.format(new Date(), "yyyy-MM-dd");
//        date="2025-09-12";
        List<TimeStock.DataDTO> list = timeStockService.getTimeStockList(date);
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/getTimeStockLine")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<TimeStockLine.DataDTO>> getTimeStockLine(HttpServletRequest request, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                                        @RequestParam(value = "size", defaultValue = "10") Integer size, Model model) {
        String date = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<TimeStockLine.DataDTO> list = timeStockService.getTimeStockLine(date);
        return ResponseEntity.ok(list);
    }


}
