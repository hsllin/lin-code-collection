package com.lin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bean.index.VolumeTrend;
import com.lin.bean.stockknow.StockConcept;

import java.util.List;

public interface StockConceptMapper extends BaseMapper<StockConcept> {
    List<StockConcept> getAll();
}
