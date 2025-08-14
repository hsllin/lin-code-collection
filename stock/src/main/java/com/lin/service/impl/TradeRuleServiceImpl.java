package com.lin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.TradeRule;
import com.lin.mapper.TradeRuleMapper;
import com.lin.service.TradeRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-08-04 14:01
 */
@Service
public class TradeRuleServiceImpl extends ServiceImpl<TradeRuleMapper, TradeRule> implements TradeRuleService {
    @Override
    public List<TradeRule> getTradeRuleList(String keyword) {
        QueryWrapper<TradeRule> wrapper = new QueryWrapper();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like("content", keyword.trim());
        }
        wrapper.orderByAsc("sort_order");
        List<TradeRule> list = getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrEditTradeRule(TradeRule bean) {
        // 如果是新增（ID为null），让数据库自动生成ID
        if (bean.getId() == null) {
            Integer id = generateId();
            bean.setId(id);
            bean.setSortOrder(id);
            return save(bean);
        } else {
            // 如果是更新，使用ID更新
            return updateById(bean);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTradeRule(Integer id) {
        return getBaseMapper().deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTradeRuleByContent(String content) {
        QueryWrapper<TradeRule> wrapper = new QueryWrapper<>();
        wrapper.eq("content", content);
        return getBaseMapper().delete(wrapper) > 0;
    }

    private Integer generateId() {
        Integer id = getBaseMapper().selectMaxId();
        if (id == null) {
            return 1;
        }
        return id + 1;
    }

}