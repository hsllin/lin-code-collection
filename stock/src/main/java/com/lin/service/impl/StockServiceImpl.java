package com.lin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.stockknow.Stock;
import com.lin.bean.stockknow.StockConcept;
import com.lin.mapper.StockConceptMapper;
import com.lin.mapper.StockMapper;
import com.lin.service.StockConceptService;
import com.lin.service.StockService;
import org.springframework.stereotype.Service;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-04-29 16:04
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    @Override
    public boolean addOrEditStock(Stock stock) {
        return saveOrUpdate(stock);
    }
}