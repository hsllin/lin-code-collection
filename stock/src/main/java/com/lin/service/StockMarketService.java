package com.lin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.stockknow.StockMarket;

import java.util.List;

/**
 * 自己维护一些概念
 */
public interface StockMarketService {

    Page<StockMarket> getStockMarketList(String keyword, Integer startIndex, Integer pageSize);

    List<StockMarket> getAllStockMarketList();

    boolean addOrEditStockMarket(StockMarket stockMarket);

    boolean deleteStockMarket(String id);


}
