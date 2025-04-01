package com.lin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bean.index.VolumeTrend;

import java.util.List;

public interface VolumeTrendService extends IService<VolumeTrend> {
    public List<VolumeTrend> getVolumeTrendData();

    public boolean saveOrUpdateData();
}
