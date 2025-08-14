package com.lin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bean.StockRule;

import java.util.List;

/**
 * 自己维护一些板块
 */
public interface StockRuleService {

    List<StockRule> getStockRuleList(String keyword);

    boolean addOrEditStockRule(StockRule stockRule);

    boolean deleteStockRule(Integer id);

    boolean deleteStockRuleByContent(String content);

}
