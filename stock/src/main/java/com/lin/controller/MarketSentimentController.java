package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.MarketSentiment;
import com.lin.service.MarketSentimentService;
import com.lin.util.CommonUtils;
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
 * @create 2025-10-18 11:30
 */
@Controller
public class MarketSentimentController {
    @Autowired
    MarketSentimentService marketSentimentService;

    @Cacheable(key = "getMarketSentimentData:default", type = Cacheable.CacheType.STOCK_DATA)
    @EncryptResponse(encryptAll = true)
    @RequestMapping("/getMarketSentimentData")
    public ResponseEntity<MarketSentiment> getMarketSentimentData(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String date = CommonUtils.getTradeDay(0);
        MarketSentiment bean = marketSentimentService.getMarketSentiment();
        return ResponseEntity.ok(bean);
    }
}