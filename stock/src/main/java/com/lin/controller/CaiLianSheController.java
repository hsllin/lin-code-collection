package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.cailianshe.CaiLianSheLimitUp;
import com.lin.bean.cailianshe.Telegraph;
import com.lin.bean.taoguba.TaoGuBaHotStock;
import com.lin.bean.taoguba.TaoGuBaHotStockPrice;
import com.lin.service.CaiLianSheService;
import com.lin.service.TaoGuBaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-14 16:56
 */
@Controller
public class CaiLianSheController {
    @Autowired
    CaiLianSheService caiLianSheService;

    @Cacheable(key = "getTelegraph:default", type = Cacheable.CacheType.STOCK_DATA)
    @EncryptResponse(encryptAll = true)
    @RequestMapping("/getTelegraph")
    public ResponseEntity<Telegraph> getTelegraph(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        Telegraph list = caiLianSheService.getTelegraph();
        return ResponseEntity.ok(list);
    }

    @Cacheable(key = "getCaiLianSheLimitUp:default", type = Cacheable.CacheType.STOCK_DATA)
    @EncryptResponse(encryptAll = true)
    @RequestMapping("/getCaiLianSheLimitUp")
    public ResponseEntity<CaiLianSheLimitUp> getCaiLianSheLimitUp(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        CaiLianSheLimitUp list = caiLianSheService.getCaiLianSheLimitUp();
        return ResponseEntity.ok(list);
    }

}