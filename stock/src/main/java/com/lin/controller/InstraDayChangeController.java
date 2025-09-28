package com.lin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
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
    @EncryptResponse(encryptAll = true)
//    @Cacheable(key = "intraDayChange:default", type = Cacheable.CacheType.STOCK_DATA)
    public ResponseEntity<List<IntradayChange.DataBean.AllstockBean>> list(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        String checkedData = request.getParameter("checkedData");
        List<IntradayChange.DataBean.AllstockBean> stockList = new ArrayList<>();
        if (type.equals("1")) {
            stockList = intraDayChangeService.getIntradayChangeList(IntraDayChangeService.INCREASE, checkedData);
        } else {
            stockList = intraDayChangeService.getIntradayChangeList(IntraDayChangeService.DECREASE, checkedData);
        }

        CollectionUtil.reverse(stockList);
        return ResponseEntity.ok(stockList);
    }

    @RequestMapping("/buyChange")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<BuyChangeBean.DataDTO.AllstockDTO>> buyChange(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        String checkedData = request.getParameter("checkedData");
        List<BuyChangeBean.DataDTO.AllstockDTO> stockList = new ArrayList<>();
        if (type.equals("1")) {
            stockList = intraDayChangeService.getBuyChangeList(IntraDayChangeService.BUY, checkedData);
        } else {
            stockList = intraDayChangeService.getBuyChangeList(IntraDayChangeService.SELL, checkedData);
        }

        CollectionUtil.reverse(stockList);
        return ResponseEntity.ok(stockList);
    }

    @RequestMapping("/sixtyChange")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<IntradayChange.DataBean.AllstockBean>> sixtyChange(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<IntradayChange.DataBean.AllstockBean> stockList = new ArrayList<>();
        if (type.equals("1")) {
            stockList = intraDayChangeService.getSixtyDayList(IntraDayChangeService.SIXTY_DAY);
        }
        CollectionUtil.reverse(stockList);
        return ResponseEntity.ok(stockList);
    }

    @RequestMapping("/openLimitDown")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<IntradayChange.DataBean.AllstockBean>> openLimitDown(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<IntradayChange.DataBean.AllstockBean> stockList = new ArrayList<>();
        stockList = intraDayChangeService.openLimitDown(IntraDayChangeService.OPEN_LIMIT_DOWN);
        CollectionUtil.reverse(stockList);
        return ResponseEntity.ok(stockList);
    }

    @RequestMapping("/cleanIncreaseChangeCached")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> cleanIncreaseChangeCached(HttpServletRequest request, Model model) {
        boolean result = intraDayChangeService.cleanCached();
        return ResponseEntity.ok(result);
    }
    
    /**
     * 清理指定类型的缓存
     */
    @RequestMapping("/cleanCachedByType")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> cleanCachedByType(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean result = intraDayChangeService.cleanCachedByType(type);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 检查缓存是否存在
     */
    @RequestMapping("/checkCacheExists")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> checkCacheExists(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        String pageIndexStr = request.getParameter("pageIndex");
        
        if (type == null || type.isEmpty() || pageIndexStr == null || pageIndexStr.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        
        try {
            int pageIndex = Integer.parseInt(pageIndexStr);
            boolean exists = intraDayChangeService.isCacheExists(type, pageIndex);
            return ResponseEntity.ok(exists);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
    
    /**
     * 测试缓存清理功能
     */
    @RequestMapping("/testCacheCleanup")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> testCacheCleanup(HttpServletRequest request, Model model) {
        String result = intraDayChangeService.testCacheCleanup();
        return ResponseEntity.ok(result);
    }

}
