package com.lin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.stockknow.Stock;
import com.lin.bean.stockstudy.StockStudy;
import com.lin.bean.tonghuashun.BigOrderBean;
import com.lin.service.BigOrderService;
import com.lin.service.StockStudyService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 获取同花顺个股学习
 * @create 2025-05-12 16:29
 */
@Controller
public class StockStudyController {
    @Autowired
    StockStudyService studyService;

    @RequestMapping("/sysStockData")
    public ResponseEntity<Boolean> sysStockData(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
//        String date = CommonUtils.getTradeDay(0);
        boolean list = studyService.sysStockData();
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/getStudyStockList")
    public ResponseEntity<Page<StockStudy>> getTodayStudyStockList(HttpServletRequest request, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                                   @RequestParam(value = "size", defaultValue = "10") Integer size, Model model) throws ScriptException, IOException, InterruptedException {
        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");
        Page<StockStudy> list = studyService.getStockList(keyword, type, current, size);
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/sysTodayStudyData")
    public ResponseEntity<Boolean> sysTodayStudyData(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");
        boolean result = studyService.sysTodayStudyData();
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/changeStudyStatus")
    public ResponseEntity<Boolean> changeStudyStatus(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String code = request.getParameter("code");
        boolean result = studyService.changeStudyStatus(code);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/getStudyNum")
    public ResponseEntity<Map<String, Object>> getStudyNum(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        Map<String, Object> result = studyService.getStudyNum();
        return ResponseEntity.ok(result);
    }
    @RequestMapping("/addOrEditStudyStock")
    public ResponseEntity<Boolean> addOrEditStudyStock(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String code = request.getParameter("code");
        boolean result = studyService.addOrEditStudyStock(code);
        return ResponseEntity.ok(result);
    }


}