package com.lin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.stockknow.StockMarketConcept;
import com.lin.mapper.StockMarketConceptMapper;
import com.lin.service.StockMarketConceptService;
import org.springframework.stereotype.Service;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-04-29 16:15
 */
@Service
public class StockMarketConceptServiceImpl extends ServiceImpl<StockMarketConceptMapper, StockMarketConcept> implements StockMarketConceptService {
    @Override
    public boolean addOrEditStock(StockMarketConcept bean) {
        return saveOrUpdate(bean);
    }
}