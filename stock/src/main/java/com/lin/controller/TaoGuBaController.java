package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.taoguba.TaoGuBaHotStock;
import com.lin.bean.taoguba.TaoGuBaHotStockPrice;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.service.TaoGuBaService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-14 16:56
 */
@Controller
public class TaoGuBaController {
    @Autowired
    TaoGuBaService taoGuBaService;

    @Cacheable(key = "getTaoGuBaHotStock:default", type = Cacheable.CacheType.STOCK_DATA)
    @EncryptResponse(encryptAll = true)
    @RequestMapping("/getTaoGuBaHotStock")
    public ResponseEntity<TaoGuBaHotStock> getTaoGuBaHotStock(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String type = request.getParameter("type");
        TaoGuBaHotStock list = taoGuBaService.getTaoGuBaHotStock(type);
        return ResponseEntity.ok(list);
    }

    @Cacheable(key = "getTaoGuBaStockPrice:default", type = Cacheable.CacheType.STOCK_DATA)
    @EncryptResponse(encryptAll = true)
    @RequestMapping("/getTaoGuBaStockPrice")
    public ResponseEntity<TaoGuBaHotStockPrice> getTaoGuBaStockPrice(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String stocks = request.getParameter("stocks");
        TaoGuBaHotStockPrice list = taoGuBaService.getTaoGuBaStockPrice(stocks);
        return ResponseEntity.ok(list);
    }


}