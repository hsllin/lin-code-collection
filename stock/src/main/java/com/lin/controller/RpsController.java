package com.lin.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.Mode;
import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.intradaychange.BuyChangeBean;
import com.lin.bean.intradaychange.IntradayChange;
import com.lin.bean.rps.RpsBean;
import com.lin.bean.rps.RpsDetailBean;
import com.lin.service.IntraDayChangeService;
import com.lin.service.RpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RpsController {
    @Autowired
    private RpsService rpsService;

    @Cacheable(key = "rpsData:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getRpsData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<RpsBean>> getRpsData(HttpServletRequest request, Model model) {
        List<RpsBean> rpsBeanArrayList = new ArrayList<>();
        rpsBeanArrayList = rpsService.getRpsData();
        CollectionUtil.reverse(rpsBeanArrayList);
        return ResponseEntity.ok(rpsBeanArrayList);
    }

    @RequestMapping("/rpsDataDetailIndex")
    public ModelAndView rpsDataDetailIndex(HttpServletRequest request, ModelAndView modelAndView) {
        String blockName = request.getParameter("blockName");
        modelAndView.addObject("blockName", blockName);
        modelAndView.setViewName("rpsDetail");
        return modelAndView;
    }

    @Cacheable(key = "rpsDataDetail:#{#request.getParameter('blockName')}", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getRpsDataDetail")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<RpsDetailBean>> buyChange(HttpServletRequest request, ModelAndView modelAndView) {
        String blockName = request.getParameter("blockName");
        List<RpsDetailBean> stockList = new ArrayList<>();
        stockList = rpsService.getRpsDataDetail(blockName);
        CollectionUtil.reverse(stockList);
        return ResponseEntity.ok(stockList);
    }


}
