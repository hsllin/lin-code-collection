package com.lin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bean.StockRule;
import org.apache.ibatis.annotations.Select;

public interface StockRuleMapper extends BaseMapper<StockRule> {
    @Select("SELECT MAX(id) FROM stock_rules")
    Integer selectMaxId();
}
