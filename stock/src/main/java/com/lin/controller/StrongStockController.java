package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.bean.lianban.LianBanNew;
import com.lin.bean.strongstock.StrongStockBean;
import com.lin.service.StrongStockService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 最像风口相关
 */
@Controller
public class StrongStockController {
    @Autowired
    StrongStockService strongStockService;

    @RequestMapping("/getStrongStockList")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<StrongStockBean.DataBean>> list(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        List<StrongStockBean.DataBean> list = strongStockService.getStrongStockList(date);

        return ResponseEntity.ok(list);
    }

    @RequestMapping("/downloadStrongStockData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> downloadLimitUpData(HttpServletRequest request, Model model) {
        String dateIndex = request.getParameter("dateIndex");
        String date = CommonUtils.getTradeDay(Integer.valueOf(dateIndex));
        strongStockService.downloadStrongSrockData(date);

        return ResponseEntity.ok(true);
    }

    @RequestMapping("/dealLianBanData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<LianBanNew>> dealLianBanData(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        List<LianBanNew> list = strongStockService.dealLianBanData(date);

        return ResponseEntity.ok(list);
    }

}
