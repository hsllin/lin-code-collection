package com.lin.service;

import com.lin.bean.TradeRule;

import java.util.List;

/**
 * 自己维护一些板块
 */
public interface TradeRuleService {

    List<TradeRule> getTradeRuleList(String keyword);

    boolean addOrEditTradeRule(TradeRule TradeRule);

    boolean deleteTradeRule(Integer id);

    boolean deleteTradeRuleByContent(String content);

}
