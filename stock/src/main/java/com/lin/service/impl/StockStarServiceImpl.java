package com.lin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.StockRule;
import com.lin.bean.StockStar;
import com.lin.mapper.StockRuleMapper;
import com.lin.mapper.StockStarMapper;
import com.lin.service.StockStarService;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-09-25 13:49
 */
public class StockStarServiceImpl extends ServiceImpl<StockStarMapper, StockStar> implements StockStarService {
    @Override
    public Integer getStarCount() {
        QueryWrapper<StockStar> queryWrapper = new QueryWrapper<>();
        List<StockStar> list = getBaseMapper().selectList(queryWrapper);
        return list.get(0).getStar();
    }

    @Override
    public boolean updateStarCount() {
        return false;
    }
}