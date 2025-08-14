package com.lin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.StockRule;
import com.lin.mapper.StockRuleMapper;
import com.lin.service.StockRuleService;
import com.lin.util.CommonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-08-04 14:01
 */
@Service
public class StockRuleServiceImpl extends ServiceImpl<StockRuleMapper, StockRule> implements StockRuleService {
    @Override
    public List<StockRule> getStockRuleList(String keyword) {
        QueryWrapper<StockRule> wrapper = new QueryWrapper();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like("content", keyword.trim());
        }
        wrapper.orderByDesc("sort_order");
        List<StockRule> list = getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrEditStockRule(StockRule bean) {
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
    public boolean deleteStockRule(Integer id) {
        return getBaseMapper().deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStockRuleByContent(String content) {
        QueryWrapper<StockRule> wrapper = new QueryWrapper<>();
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