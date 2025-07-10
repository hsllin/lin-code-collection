package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.TimeStock;
import com.lin.bean.TimeStockLine;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财联社分时冲击概念
 */
@Service
public class TimeStockService {
    public static void main(String[] args) {
        getTimeStockList("2025-07-09");
    }

    public static List<TimeStock.DataDTO> getTimeStockList(String date) {
        String url = "https://www.cls.cn/v3/transaction/anchor";

        Map<String, Object> param = new HashMap<>();
        param.put("app", "CailianpressWeb");
        param.put("os", "web");
        param.put("cdate", date);
        param.put("sv", "8.4.6");
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        TimeStock bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), TimeStock.class);

        List<TimeStock.DataDTO> allList = bean.getData();
        return allList;
    }
    public static List<TimeStockLine.DataDTO> getTimeStockLine(String date) {
        String url = "https://x-quote.cls.cn/quote/index/tline";

        Map<String, Object> param = new HashMap<>();
        param.put("app", "CailianpressWeb");
        param.put("os", "web");
        param.put("cdate", date);
        param.put("sv", "8.4.6");

        String result = HttpUtil.get(url, param);
        TimeStockLine bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), TimeStockLine.class);

        List<TimeStockLine.DataDTO> allList = bean.getData();
        return allList;
    }


}
