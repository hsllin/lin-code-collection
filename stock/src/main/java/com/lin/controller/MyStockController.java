package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.MyStock;
import com.lin.service.MyStockService;
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
 * @Description 自选个股
 * @create 2025-08-14 16:29
 */
@Controller
public class MyStockController {
    @Autowired
    MyStockService myStockService;

    @Cacheable(key = "sysMyStockData:default", type = Cacheable.CacheType.DEFAULT, ttl = 300)
    @RequestMapping("/sysMyStockData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> sysMyStockData(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        boolean list = myStockService.sysStockData();
        return ResponseEntity.ok(list);
    }

    @Cacheable(key = "myStockList:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getMyStockList")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<MyStock>> getTodayMyStockList(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        List<MyStock> list = myStockService.getMyStockList();
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/addOrEditMyStock")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> addOrEditMyStock(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String code = request.getParameter("code");
        boolean result = myStockService.addOrEditMyStock(code);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteMyStock")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> deleteMyStock(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String code = request.getParameter("code");
        boolean result = myStockService.deleteStock(code);
        return ResponseEntity.ok(result);
    }

}