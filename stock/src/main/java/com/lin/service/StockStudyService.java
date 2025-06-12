package com.lin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.stockknow.Stock;
import com.lin.bean.stockstudy.StockStudy;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 个股学习
 */
public interface StockStudyService {

    Page<StockStudy> getStockList(String keyword, String type, Integer startIndex, Integer pageSize);

    List<StockStudy> getAllStockList();

    boolean addOrEditStock(StockStudy Stock) throws ScriptException, IOException;

    boolean addOrEditStudyStock(String code) throws ScriptException, IOException;

    boolean deleteStock(String id);

    boolean changeStudyStatus(String code);

    boolean sysStockData() throws ScriptException, IOException, InterruptedException;

    boolean sysTodayStudyData();

    Map<String,Object> getStudyNum();
}
