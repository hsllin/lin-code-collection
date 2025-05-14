package com.lin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.StockDocument;
import com.lin.bean.stockknow.StockBoard;
import com.lin.mapper.StockBoardMapper;
import com.lin.service.StockBoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockBoardServiceImpl extends ServiceImpl<StockBoardMapper, StockBoard> implements StockBoardService {
    @Override
    public Page<StockBoard> getStockBoardList(String keyword, Integer startIndex, Integer pageSize) {
        QueryWrapper<StockBoard> wrapper = new QueryWrapper();
        wrapper.orderByDesc("update_date");
        Page<StockBoard> page = new Page<>(startIndex, pageSize);
        Page<StockBoard> list = getBaseMapper().selectPage(page, wrapper);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrEditStockBoard(StockBoard bean) {
        if (bean.getId() == null) {
            bean.setId(IdUtil.fastSimpleUUID());
        }
        UpdateWrapper<StockBoard> updateWrapper = new UpdateWrapper<StockBoard>()
                .eq("id", bean.getId())
                .eq("name", bean.getName());
        return saveOrUpdate(bean, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStockBoard(String id) {
        return getBaseMapper().deleteById(id) > 0;
    }

    public boolean updateBoardData() {
        String boardurl = "http://push2.eastmoney.com/api/qt/clist/get?pn=1&pz=10000&fs=m:90+t:2";

        HttpClient client = HttpClient.newHttpClient();
        String result = HttpUtil.get(boardurl);
        Map<String, Object> boardMap = JSONUtil.toBean(JSONUtil.toJsonStr(result), Map.class);
        Map<String, Object> datamap = (Map) boardMap.get("data");
        Map<String, Object> difMap = (Map) datamap.get("diff");
        List<StockBoard> stockBoardList = new ArrayList<StockBoard>();
        for (Map.Entry<String, Object> entry : difMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            Map<String, Object> stockMap = (Map<String, Object>) entry.getValue();
            StockBoard stockBoard = new StockBoard();
            stockBoard.setId(stockMap.get("f12").toString());
            stockBoard.setName(stockMap.get("f14").toString());
            stockBoard.setDescription(stockMap.get("f14").toString());
            stockBoardList.add(stockBoard);
        }
        return saveOrUpdateBatch(stockBoardList);
    }

}
