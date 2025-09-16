package com.lin.controller;

import com.lin.annotation.DecryptRequest;
import com.lin.annotation.EncryptResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.stockknow.StockBoard;
import com.lin.service.StockBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 股票板块
 */
@Controller
public class StockBoardController {
    @Autowired
    StockBoardService BoardService;

    @RequestMapping("/getStockBoardList")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Page<StockBoard>> getStockBoardList(HttpServletRequest request, @RequestParam(value = "current", defaultValue = "1") Integer current,
                                                              @RequestParam(value = "size", defaultValue = "10") Integer size, Model model) {
        String keyword = request.getParameter("keyword");
        Page<StockBoard> list = BoardService.getStockBoardList(keyword, current, size);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addOrEditStockBoardData")
    @ResponseBody
    @DecryptRequest
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> addOrEditStockKnownData(HttpServletRequest request, @RequestBody StockBoard stockBoard) {
        boolean result = BoardService.addOrEditStockBoard(stockBoard);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteStockBoard")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> deleteStockBoard(HttpServletRequest request, String id) {
        boolean result = BoardService.deleteStockBoard(id);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/updateBoardData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> updateBoardData(HttpServletRequest request) {
        boolean result = BoardService.updateBoardData();
        return ResponseEntity.ok(result);
    }

}
