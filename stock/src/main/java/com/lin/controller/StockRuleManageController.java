package com.lin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.StockRule;
import com.lin.service.StockBoardService;
import com.lin.service.StockRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 股票板块
 */
@Controller
public class StockRuleManageController {
    @Autowired
    StockRuleService RuleService;

    @Autowired
    private com.lin.mapper.StockRuleMapper stockRuleMapper;

    @RequestMapping("/getStockRuleList")
    public ResponseEntity<List<StockRule>> getStockRuleList(HttpServletRequest request, Model model) {
        String keyword = request.getParameter("keyword");
        List<StockRule> list = RuleService.getStockRuleList(keyword);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addOrEditStockRuleData")
    @ResponseBody
    public ResponseEntity<Boolean> addOrEditStockKnownData(HttpServletRequest request, @RequestBody StockRule stockRule) {
        boolean result = RuleService.addOrEditStockRule(stockRule);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteStockRule")
    public ResponseEntity<Boolean> deleteStockRule(HttpServletRequest request, @RequestParam("id") Integer id) {
        boolean result = RuleService.deleteStockRule(id);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteStockRuleByContent")
    public ResponseEntity<Boolean> deleteStockRuleByContent(HttpServletRequest request, @RequestParam("content") String content) {
        boolean result = RuleService.deleteStockRuleByContent(content);
        return ResponseEntity.ok(result);
    }

    // 测试方法，用于调试参数类型问题
    @RequestMapping("/testDeleteStockRule")
    public ResponseEntity<String> testDeleteStockRule(HttpServletRequest request, @RequestParam("id") String id) {
        try {
            Integer intId = Integer.parseInt(id);
            boolean result = RuleService.deleteStockRule(intId);
            return ResponseEntity.ok("删除结果: " + result);
        } catch (Exception e) {
            return ResponseEntity.ok("错误: " + e.getMessage());
        }
    }
}
