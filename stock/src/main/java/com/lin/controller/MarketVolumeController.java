package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.bean.market.MarketBean;
import com.lin.bean.market.MarketTimeLineBean;
import com.lin.bean.market.OneWordLimitBean;
import com.lin.service.MarketVolumeTemperatureService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 最像风口相关
 */
@Controller
public class MarketVolumeController {
    @Autowired
    MarketVolumeTemperatureService marketVolumeTemperatureService;

    @RequestMapping("/getMarketVolume")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<MarketBean> list(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        MarketBean bean = marketVolumeTemperatureService.getMarketList(date);

        return ResponseEntity.ok(bean);
    }

    @RequestMapping("/getMarketTimeLine")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<MarketTimeLineBean> getMarketTimeLine(HttpServletRequest request, Model model) {
        String dateIndex = request.getParameter("dateIndex");
        String date = CommonUtils.getTradeDay(Integer.valueOf(dateIndex));
        MarketTimeLineBean bean = marketVolumeTemperatureService.getMarketTimeLine(date);

        return ResponseEntity.ok(bean);
    }

    @RequestMapping("/getMarketYzzt")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<OneWordLimitBean> getStrongStockList(HttpServletRequest request, Model model) {
        String dateIndex = request.getParameter("dateIndex");
        String date = CommonUtils.getTradeDay(Integer.valueOf(dateIndex));
        OneWordLimitBean bean = marketVolumeTemperatureService.getStrongStockList(date);

        return ResponseEntity.ok(bean);
    }

}
