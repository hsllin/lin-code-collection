package com.lin.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.lianban.LimitDownPoolResultBean;
import com.lin.bean.stockknow.StockConcept;
import com.lin.service.LimitDownPoolService;
import com.lin.service.StockConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 股票概念
 */
@Controller
public class StockConceptController {
    @Autowired
    StockConceptService conceptService;

    @RequestMapping("/getStockConceptList")
    public ResponseEntity<Page<StockConcept>> getStockConceptList(HttpServletRequest request, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size, Model model) {
        String keyword = request.getParameter("keyword");
        Page<StockConcept> list = conceptService.getStockConceptList(keyword, current, size);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addOrEditStockConceptData")
    @ResponseBody
    public ResponseEntity<Boolean> addOrEditStockKnownData(HttpServletRequest request, @RequestBody StockConcept stockConcept) {
        boolean result = conceptService.addOrEditStockConcept(stockConcept);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteStockConcept")
    public ResponseEntity<Boolean> deleteStockConcept(HttpServletRequest request, String id) {
        boolean result = conceptService.deleteStockConcept(id);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/sysTonghuaShunConcept")
    public ResponseEntity<Boolean> sysTonghuaShunConcept(HttpServletRequest request) {
        boolean result = conceptService.sysTonghuaShunConcept();
        return ResponseEntity.ok(result);
    }


}
