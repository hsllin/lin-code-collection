package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.pluginking.LiveStreamingBean;
import com.lin.bean.tonghuashun.BigOrderBean;
import com.lin.service.BigOrderService;
import com.lin.service.PluginKingService;
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
 * @Description 获取插件王里面的信息
 * @create 2025-05-12 16:29
 */
@Controller
public class PluginKingController {
    @Autowired
    PluginKingService pluginKingService;

    @Cacheable(key = "liveStreamingData:default", type = Cacheable.CacheType.HOT_DATA)
    @RequestMapping("/getLiveStreamingData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getLiveStreamingData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String data = pluginKingService.getLiveStreamingData();
        return ResponseEntity.ok(data);
    }

    @Cacheable(key = "featuredBoards:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getFeaturedBoards")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getFeaturedBoards(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String data = pluginKingService.getFeaturedBoards();
        return ResponseEntity.ok(data);
    }

    @Cacheable(key = "featuredBoardsData:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getFeaturedBoardsData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getFeaturedBoardsData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String boardId = request.getParameter("boardId");
        String data = pluginKingService.getFeaturedBoardsData(boardId);
        return ResponseEntity.ok(data);
    }

    @Cacheable(key = "rangeIncreaseBoardData:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getRangeIncreaseBoardData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getRangeIncreaseBoardData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String days = request.getParameter("days");
        String sort = request.getParameter("sort");
        String data = pluginKingService.getRangeIncreaseBoardData(days,sort);
        return ResponseEntity.ok(data);
    }

    @Cacheable(key = "rangeIncreaseStockData:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getRangeIncreaseStockData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getRangeIncreaseStockData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String days = request.getParameter("days");
        String sort = request.getParameter("sort");
        String data = pluginKingService.getRangeIncreaseStockData(days,sort);
        return ResponseEntity.ok(data);
    }

    @Cacheable(key = "stockThemeChance:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getStockThemeChance")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getStockThemeChance(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String days = request.getParameter("days");
        String sort = request.getParameter("sort");
        String data = pluginKingService.getStockThemeChance();
        return ResponseEntity.ok(data);
    }

    @Cacheable(key = "limitUpAnalyze:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getLimitUpAnalyze")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getLimitUpAnalyze(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String days = request.getParameter("days");
        String sort = request.getParameter("sort");
        String data = pluginKingService.getLimitUpAnalyze(date);
        return ResponseEntity.ok(data);
    }

    @Cacheable(key = "hotTheme:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getHotTheme")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getHotTheme(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String days = request.getParameter("days");
        String sort = request.getParameter("sort");
        String data = pluginKingService.getHotTheme();
        return ResponseEntity.ok(data);
    }

}