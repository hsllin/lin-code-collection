package com.lin.service.impl;

import java.util.Date;
import java.time.LocalDateTime;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.MyStock;
import com.lin.bean.StockDocument;
import com.lin.bean.stockknow.StockBoard;
import com.lin.bean.stockknow.StockConcept;
import com.lin.bean.stockstudy.StockStudy;
import com.lin.mapper.MyStockMapper;
import com.lin.mapper.StockBoardMapper;
import com.lin.service.MyStockService;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import com.lin.util.WencaiUtils;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-08-14 9:14
 */
@Service
public class MyStockServiceImpl extends ServiceImpl<MyStockMapper, MyStock> implements MyStockService {
    @Override
    public List<MyStock> getMyStockList() {
        QueryWrapper<MyStock> wrapper = new QueryWrapper();
        wrapper.orderByDesc("update_date");
        List<MyStock> list = getBaseMapper().selectList(wrapper);
        return list;
    }

    @Override
    public boolean addOrEditMyStock(String code) throws ScriptException, IOException {

        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        param.put("question", code + "；公司主营业务；行业分类；所属概念；公司概况；盈亏情况；市值；流通市值");
        param.put("query", code + "；公司主营业务；行业分类；所属概念；公司概况；盈亏情况；市值；流通市值");
        param.put("page", "1");
        param.put("perpage", "1");
        param.put("logid", "643581ab0bb14d48c494302861a14574");
        param.put("ret", "json_all");
        param.put("sessionid", "73d1bfa13378b3058147034fd2415994");
        param.put("iwc_token", "0ac98cc517485028307987860");
        param.put("uuids[0]", "24087");
        param.put("comp_id", "6836372");
        param.put("user_id", "Ths_iwencai_Xuangu_ta7wv6968l1v5l2l8orrl4ohcg77279g");
        param.put("comp_id", "6836372");
        param.put("business_cat", "soniu");
        param.put("uuid", "24087");


        Map<String, Object> result = WencaiUtils.getData("", param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
//        JsonNode txtNode = jsonNode.at("/answer/components").get(0).at("/data/datas");
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);

        Double pe = 0.0;
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            MyStock bean = new MyStock();
            pe = null == txt.get("市盈率(pe)") ? 0 : Double.valueOf(txt.get("市盈率(pe)") + "");
            bean.setCode(code);
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setMainBusiness(txt.get("主营产品名称") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setCompanyIntroduce(txt.get("公司简介") + "");
            bean.setMarketCap(txt.get("总市值") + "");
            bean.setMarketValue(txt.get("a股市值(不含限售股)") + "");
            bean.setPe(txt.get("市盈率(pe)") + "");
            bean.setProfitLoss(pe > 0 ? "1" : "0");
            bean.setCompanyLocation(txt.get("办公地址") + "");
            bean.setRate(CommonUtils.formatPrice(Double.valueOf(txt.get("最新涨跌幅") + "")));

            saveOrUpdate(bean);
        }
        return true;
    }

    @Override
    public boolean deleteStock(String code) {
        QueryWrapper<MyStock> wrapper = new QueryWrapper();
        wrapper.eq("code", code);
        return getBaseMapper().delete(wrapper) > 0;
    }

    @Override
    public boolean sysStockData() throws ScriptException, IOException, InterruptedException {
        String url = "https://np-tjxg-g.eastmoney.com/api/smart-tag/stock/v3/pw/search-code";
        Map<String, Object> param = new HashMap<>();
        param.put("dxInfo", new ArrayList<>());
        param.put("extraCondition", "");
        param.put("fingerprint", "3bcfaace7971f4f85f2f3f7288d3e22c");
        param.put("gids", new ArrayList<>());
//        param.put("keyWord", "002806");
        param.put("matchWord", "");
        param.put("needCorrect", "true");
        param.put("ownSelectAll", false);
        param.put("pageNo", "1");
        param.put("pageSize", "100");
        param.put("removedConditionIdList", new ArrayList<>());
        param.put("requestId", "w9dqHyCVS3cgNjejy0VtkKLgkMsVvm4G1755156746823");
        param.put("shareToGuba", "false");
        List<MyStock> stockList = getMyStockList();
        String keyword="";
        for (MyStock stock : stockList) {
            keyword += stock.getCode() + ";";
        }
        param.put("keyWord", keyword);
        param.put("timestamp", System.currentTimeMillis());
        param.put("xcId", "xc0be2a8f6163300390b");

        String result = HttpUtil.createPost(url).contentType("application/json").body(JSONUtil.toJsonStr(param)).execute().body();
        Map map = JSONUtil.toBean(JSONUtil.toJsonStr(result), Map.class);
        Map dataMap = (Map) map.get("data");
        Map resultMap = (Map) dataMap.get("result");
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("dataList");
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> data = dataList.get(i);
            MyStock bean = new MyStock();
            bean.setCode(data.get("SECURITY_CODE") + "");
            bean.setStudyStatus("");
            bean.setMarketCap("");
            bean.setMarketValue(data.get("CIRCULATION_MARKET_VALUE<140>") + "");
            bean.setProfitLoss("");
            bean.setPrice(data.get("NEWEST_PRICE") + "");
            bean.setCompanyLocation("");
            bean.setPe(data.get("PE_DYNAMIC") + "");
            bean.setDescription("");
            bean.setRate(Double.valueOf(data.get("CHG") + ""));
            bean.setTurnoverRate(CommonUtils.formatStringPrice(data.get("TURNOVER_RATE") + ""));
            bean.setVolality(data.get("CIRCULATION_MARKET_VALUE<140>") + "");
            bean.setTradingVolume(data.get("TRADING_VOLUMES") + "");

            bean.setMinPrice(data.get("BOTTOM_PRICE<140>") + "");
            bean.setMaxPrice(data.get("PEAK_PRICE<140>") + "");
            bean.setQuantityRatio(data.get("QRR") + "");
            saveOrUpdate(bean);

        }
        return true;
    }

}