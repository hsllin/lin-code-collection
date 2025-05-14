package com.lin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.stockknow.StockBoard;

/**
 * 自己维护一些板块
 */
public interface StockBoardService {

    Page<StockBoard> getStockBoardList(String keyword, Integer startIndex, Integer pageSize);

    boolean addOrEditStockBoard(StockBoard stockBoard);

    boolean deleteStockBoard(String id);

    boolean updateBoardData();
}
