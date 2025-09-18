package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.DecryptRequest;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.TradeRule;
import com.lin.service.TradeRuleService;
import com.lin.service.TradeRuleService;
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
public class TradeRuleManageController {
    @Autowired
    TradeRuleService tradeRuleService;

    @Autowired
    private com.lin.mapper.TradeRuleMapper TradeRuleMapper;

    @Cacheable(key = "tradeRuleList:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getTradeRuleList")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<TradeRule>> getTradeRuleList(HttpServletRequest request, Model model) {
        String keyword = request.getParameter("keyword");
        List<TradeRule> list = tradeRuleService.getTradeRuleList(keyword);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addOrEditTradeRuleData")
    @ResponseBody
    @DecryptRequest
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> addOrEditTradeKnownData(HttpServletRequest request, @RequestBody TradeRule TradeRule) {
        boolean result = tradeRuleService.addOrEditTradeRule(TradeRule);
        return ResponseEntity.ok(result);
    }

    @Cacheable(key = "deleteTradeRule:#{#id}", type = Cacheable.CacheType.DEFAULT, ttl = 300)
    @RequestMapping("/deleteTradeRule")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> deleteTradeRule(HttpServletRequest request, @RequestParam("id") Integer id) {
        boolean result = tradeRuleService.deleteTradeRule(id);
        return ResponseEntity.ok(result);
    }

    @Cacheable(key = "deleteTradeRuleByContent:#{#content}", type = Cacheable.CacheType.DEFAULT, ttl = 300)
    @RequestMapping("/deleteTradeRuleByContent")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> deleteTradeRuleByContent(HttpServletRequest request, @RequestParam("content") String content) {
        boolean result = tradeRuleService.deleteTradeRuleByContent(content);
        return ResponseEntity.ok(result);
    }

    // 测试方法，用于调试参数类型问题
    @Cacheable(key = "testDeleteTradeRule:#{#id}", type = Cacheable.CacheType.DEFAULT, ttl = 300)
    @RequestMapping("/testDeleteTradeRule")
    public ResponseEntity<String> testDeleteTradeRule(HttpServletRequest request, @RequestParam("id") String id) {
        try {
            Integer intId = Integer.parseInt(id);
            boolean result = tradeRuleService.deleteTradeRule(intId);
            return ResponseEntity.ok("删除结果: " + result);
        } catch (Exception e) {
            return ResponseEntity.ok("错误: " + e.getMessage());
        }
    }
}
