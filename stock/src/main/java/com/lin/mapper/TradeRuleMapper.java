package com.lin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bean.TradeRule;
import org.apache.ibatis.annotations.Select;

public interface TradeRuleMapper extends BaseMapper<TradeRule> {
    @Select("SELECT MAX(id) FROM trade_rules")
    Integer selectMaxId();
}
