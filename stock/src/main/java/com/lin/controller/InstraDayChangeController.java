package com.lin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.lin.bean.intradaychange.BuyChangeBean;
import com.lin.bean.intradaychange.IntradayChange;
import com.lin.service.IntraDayChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InstraDayChangeController {
    @Autowired
    private IntraDayChangeService intraDayChangeService;

    @RequestMapping("/intraDayChange")
    public ResponseEntity<List<IntradayChange.DataBean.AllstockBean>> list(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<IntradayChange.DataBean.AllstockBean> stockList = new ArrayList<>();
        if (type.equals("1")) {
            stockList = intraDayChangeService.getIntradayChangeList(IntraDayChangeService.INCREASE);
        } else {
            stockList = intraDayChangeService.getIntradayChangeList(IntraDayChangeService.DECREASE);
        }

        CollectionUtil.reverse(stockList);
        return ResponseEntity.ok(stockList);
    }

    @RequestMapping("/buyChange")
    public ResponseEntity<List<BuyChangeBean.DataDTO.AllstockDTO>> buyChange(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<BuyChangeBean.DataDTO.AllstockDTO> stockList = new ArrayList<>();
        if (type.equals("1")) {
            stockList = intraDayChangeService.getBuyChangeList(IntraDayChangeService.BUY);
        } else {
            stockList = intraDayChangeService.getBuyChangeList(IntraDayChangeService.SELL);
        }

        CollectionUtil.reverse(stockList);
        return ResponseEntity.ok(stockList);
    }

}
