package com.lin.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.stockknow.StockConcept;
import com.lin.bean.stockknow.StockMarket;
import com.lin.mapper.StockConceptMapper;
import com.lin.mapper.StockMarketMapper;
import com.lin.service.StockMarketService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-14 17:22
 */
@Service
public class StockMarketServiceImpl extends ServiceImpl<StockMarketMapper, StockMarket> implements StockMarketService {
    @Override
    public Page<StockMarket> getStockMarketList(String keyword, Integer startIndex, Integer pageSize) {
        return null;
    }

    @Override
    public List<StockMarket> getAllStockMarketList() {
        return List.of();
    }

    @Override
    public boolean addOrEditStockMarket(StockMarket bean) {
        if (bean.getCode() == null) {
            bean.setCode(IdUtil.fastSimpleUUID());
        }
        UpdateWrapper<StockMarket> updateWrapper = new UpdateWrapper<StockMarket>().eq("code", bean.getCode()).eq("name", bean.getName());
        //todo:存储行业板块和概念板块
        return saveOrUpdate(bean, updateWrapper);
    }

    @Override
    public boolean deleteStockMarket(String id) {
        return false;
    }
}