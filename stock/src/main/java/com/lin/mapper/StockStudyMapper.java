package com.lin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bean.stockknow.StockMarket;
import com.lin.bean.stockstudy.StockStudy;

import java.util.Map;

public interface StockStudyMapper extends BaseMapper<StockStudy> {
//    List<StockConcept> getAll();

    Map<String, Object> getStudyNum();
}
