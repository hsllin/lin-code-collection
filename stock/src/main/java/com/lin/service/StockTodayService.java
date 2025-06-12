package com.lin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.stockknow.Stock;
import com.lin.bean.stockstudy.StockStudy;
import com.lin.bean.stockstudy.StockToday;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

/**
 * 个股学习
 */
public interface StockTodayService {

    List<StockToday> getAllStockList();

    boolean addOrEditStock(StockToday Stock);

    boolean deleteStock(String date);

}
