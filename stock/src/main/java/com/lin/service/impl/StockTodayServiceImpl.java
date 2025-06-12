package com.lin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.stockstudy.StockStudy;
import com.lin.bean.stockstudy.StockToday;
import com.lin.mapper.StockStudyMapper;
import com.lin.mapper.StockTodayMapper;
import com.lin.service.StockTodayService;
import com.lin.util.CommonUtils;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-06-03 15:20
 */
@Service
public class StockTodayServiceImpl extends ServiceImpl<StockTodayMapper, StockToday> implements StockTodayService {
    @Override
    public List<StockToday> getAllStockList() {
        return getBaseMapper().selectList(new QueryWrapper<StockToday>());
    }

    @Override
    public boolean addOrEditStock(StockToday stock) {
        return saveOrUpdate(stock);
    }

    @Override
    public boolean deleteStock(String date) {
        QueryWrapper<StockToday> queryWrapper = new QueryWrapper<>();
        //删掉前一天的数据
        queryWrapper.le("create_date", date);
        return getBaseMapper().delete(queryWrapper) > 0;
    }
}