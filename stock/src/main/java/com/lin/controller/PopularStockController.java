package com.lin.controller;

import com.lin.bean.lianban.LianBanNew;
import com.lin.bean.popular.DongCaiPopularStock;
import com.lin.bean.popular.FlushPopularStock;
import com.lin.bean.popular.IndustryPopularStock;
import com.lin.service.PopularStockService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 同花顺和东方财富热门股票
 */
@Controller
public class PopularStockController {
    @Autowired
    PopularStockService popularStockService;

    /**获取同花顺热门股票和飙升
     * @param request
     * @param model
     * @return
     */
    //
    @RequestMapping("/getPopularStockList")
    public ResponseEntity<List<FlushPopularStock.DataDTO.StockListDTO>> getPopularStockList(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<FlushPopularStock.DataDTO.StockListDTO> list = PopularStockService.getPopularStockList(type);
        return ResponseEntity.ok(list);
    }

    /**获取同花顺热门板块和概念
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getPopularConceptList")
    public ResponseEntity<List<IndustryPopularStock.DataDTO.PlateListDTO>> getPopularConceptList(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<IndustryPopularStock.DataDTO.PlateListDTO> list = PopularStockService.getPopularConceptList(type);
        return ResponseEntity.ok(list);
    }

    /**获取东方财富热门股票
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getDongCaiPopularStocktList")
    public ResponseEntity<List< DongCaiPopularStock.DataDTO.DiffDTO>> getDongCaiPopularStocktList(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        List<DongCaiPopularStock.DataDTO.DiffDTO> list = PopularStockService.getDongCaiPopularStocktList(type);
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/downLoadHotBoardAndConceptData")
    public ResponseEntity<Boolean> downLoadHotBoardAndConceptData(HttpServletRequest request, Model model) {
        String orderFiled = request.getParameter("orderFiled");
        String orderBy = request.getParameter("orderBy");
        String date = CommonUtils.getTradeDay(0);
        popularStockService.downLoadHotBoardAndConceptData();

        return ResponseEntity.ok(true);
    }



}
