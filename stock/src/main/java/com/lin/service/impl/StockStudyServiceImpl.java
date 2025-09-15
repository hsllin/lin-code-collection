package com.lin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.LianBanBean;
import com.lin.bean.stockknow.Stock;
import com.lin.bean.stockknow.StockConcept;
import com.lin.bean.stockstudy.StockStudy;
import com.lin.bean.stockstudy.StockToday;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.mapper.StockMapper;
import com.lin.mapper.StockStudyMapper;
import com.lin.service.IncreaseAndDecreaseService;
import com.lin.service.LianBanChiService;
import com.lin.service.StockStudyService;
import com.lin.service.StockTodayService;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import com.lin.util.WencaiUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.*;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-30 16:05
 */
@Service
public class StockStudyServiceImpl extends ServiceImpl<StockStudyMapper, StockStudy> implements StockStudyService {
    @Autowired
    LianBanChiService lianBanChiService;
    @Autowired
    IncreaseAndDecreaseService increaseAndDecreaseService;
    @Autowired
    StockTodayService stockTodayService;

    public static void main(String[] args) throws ScriptException, IOException, InterruptedException {
        getStockFromTonghuaShun();
    }

    @Override
    public Page<StockStudy> getStockList(String keyword, String type, Integer startIndex, Integer pageSize) {
        //需要先往今天的股票池插入涨幅最大的几个票
        List<StockToday> todayList = stockTodayService.getAllStockList();
        List<String> codeList = new ArrayList<>();
        for (StockToday today : todayList) {
            codeList.add(today.getCode());
        }
        QueryWrapper<StockStudy> wrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like("name", keyword).or().like("code", keyword)
                    .or().like("concepts", keyword)
                    .or().like("industry", keyword);
        }
        if (StringUtils.isNotBlank(type)) {
            if ("1".equals(type) || "0".equals(type)) {
                wrapper.eq("study_status", type);
            } else {
                wrapper.in("code", codeList);
            }
        }
        wrapper.orderByAsc("study_status");
        wrapper.orderByDesc("update_date");
        Page<StockStudy> page = new Page<>(startIndex, pageSize);
        Page<StockStudy> list = getBaseMapper().selectPage(page, wrapper);
        return list;
    }

    @Override
    public List<StockStudy> getAllStockList() {
        return getBaseMapper().selectList(new QueryWrapper<StockStudy>());
    }

    @Override
    public boolean addOrEditStock(StockStudy Stock) throws ScriptException, IOException {
        return saveOrUpdate(Stock);
    }

    @Override
    public boolean addOrEditStudyStock(String code) throws ScriptException, IOException {

        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        param.put("question", code + "；公司主营业务；行业分类；所属概念；公司概况；盈亏情况；市值；流通市值");
        param.put("query", code + "；公司主营业务；行业分类；所属概念；公司概况；盈亏情况；市值；流通市值");
        param.put("page", "1");
        param.put("perpage", "100");
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
            StockStudy bean = new StockStudy();
            pe = null == txt.get("市盈率(pe)" ) ? 0 : Double.valueOf(txt.get("市盈率(pe)") + "");
            bean.setCode(code);
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setMainBusiness(txt.get("主营产品名称") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setCompanyIntroduce(txt.get("公司简介") + "");
            bean.setMarketCap(txt.get("总市值" ) + "");
            bean.setMarketValue(txt.get("a股市值(不含限售股)" ) + "");
            bean.setPe(txt.get("市盈率(pe)" ) + "");
            bean.setProfitLoss(pe > 0 ? "1" : "0");
            bean.setCompanyLocation(txt.get("办公地址") + "");
            bean.setRate(CommonUtils.formatPrice(Double.valueOf(txt.get("最新涨跌幅") + "")));

            saveOrUpdate(bean);
        }
        return true;
    }


    @Override
    public boolean deleteStock(String id) {
        return false;
    }

    @Override
    public boolean changeStudyStatus(String code) {
        UpdateWrapper<StockStudy> wrapper = new UpdateWrapper<>();
        wrapper.eq("code", code)
                .set("study_status", "1");
        return update(wrapper);
    }

    @Override
    public boolean sysStockData() throws ScriptException, IOException, InterruptedException {
        List<StockStudy> list = getStockFromTonghuaShun();
        return saveOrUpdateBatch(list);
    }

    @Override
    public boolean sysTodayStudyData() {
        String date = DateUtils.getDate(DateFormatEnum.DATE);
        //先清掉
        stockTodayService.deleteStock(date);
        List<StockStudy> todayStockList = new ArrayList<>();
        List<IncreaseRankData> todayDataList = new ArrayList<>();
        try {
            todayDataList = increaseAndDecreaseService.getLianBanData();
            todayDataList.addAll(increaseAndDecreaseService.getTop100Data());
            todayDataList.addAll(increaseAndDecreaseService.getIncreaseData());

            for (IncreaseRankData bean : todayDataList) {
                StockStudy stock = new StockStudy();
                stock.setCode(bean.getCode());
                stock.setName(bean.getName());
                stock.setPrice(bean.getPrice());
                stock.setRate(bean.getRate());
                //更新价格和涨跌幅到基础表
                addOrEditStock(stock);
                todayStockList.add(stock);
            }
            //更新今日学习的股票代码
            for (IncreaseRankData bean : todayDataList) {
                System.out.println(bean.getCode());
                StockToday stockToday = new StockToday();
                stockToday.setCode(bean.getCode());
                stockTodayService.addOrEditStock(stockToday);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Object> getStudyNum() {
        return getBaseMapper().getStudyNum();
    }

    /**
     * 获取同花顺一字涨停
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<StockStudy> getStockFromTonghuaShun() throws ScriptException, IOException, InterruptedException {

        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"个股排行;_&_公司主营业务;_&_行业分类;_&_所属概念;_&_公司概况;_&_盈亏情况;_&_市值;_&_流通市值\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":10,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"主营产品名称\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"主营产品名称\",\"valueType\":\"_主营产品名称\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":8,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":6,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"公司简介\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"公司简介\",\"valueType\":\"_公司简介\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"总市值\",\"indexProperties\":[\"nodate 1\",\"交易日期 20250530\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250530\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"总市值\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"nodate 1\",\"交易日期 20250530\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250530\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"a股市值(不含限售股)\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0}]";
        param.put("query", "个股排行；公司主营业务；行业分类；所属概念；公司概况；盈亏情况；市值；流通市值");
        param.put("query_type", "stock");
        param.put("urp_sort_index", "最新涨跌幅");
        param.put("page", "0");
        param.put("perpage", "50");
        param.put("condition", condition);
        param.put("logid", "ba73693752239c10fc91bdd58653d7ba");
        param.put("ret", "json_all");
        param.put("sessionid", "ba73693752239c10fc91bdd58653d7ba");
        param.put("iwc_token", "0ac9a0e617485925343203375");
        param.put("user_id", "Ths_iwencai_Xuangu_ta7wv6968l1v5l2l8orrl4ohcg77279g");
        param.put("uuids[0]", "18369");
        param.put("comp_id", "6836372");
        param.put("business_cat", "soniu");
        param.put("uuid", "24087");


        Map<String, Object> result = WencaiUtils.getData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);

        // Access /data/answer/txt using JSON Pointer
//        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        JsonNode txtNode = jsonNode.at("/answer/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        String date = "[" + CommonUtils.getTradeDay(0) + "]";

        if (txtMap != null && txtMap.size() > 0) {
            for (int i = 0; i < 200; i++) {
                param.put("page", String.valueOf(Integer.parseInt(param.get("page") + "") + 1));
                result = WencaiUtils.getData(url, param);
                jsonNode = mapper.convertValue(result, JsonNode.class);
                txtNode = jsonNode.at("/answer/components").get(0).at("/data/datas");
                if (txtNode != null) {
                    txtMap.addAll(mapper.convertValue(txtNode, List.class));
                } else {
                    break;
                }
                Thread.sleep(5000);
            }

        }
        List<StockStudy> list = new ArrayList<>();
        Double pe = 0.0;
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            StockStudy bean = new StockStudy();
            pe = Double.valueOf(txt.get("市盈率(pe)" + date) + "");
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setMainBusiness(txt.get("主营产品名称") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setCompanyIntroduce(txt.get("公司简介") + "");
            bean.setMarketCap(txt.get("总市值" + date) + "");
            bean.setMarketValue(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setPe(txt.get("市盈率(pe)" + date) + "");
            bean.setProfitLoss(pe > 0 ? "1" : "0");
            bean.setCompanyLocation(txt.get("办公地址") + "");
            list.add(bean);
        }
        System.out.println(list.size());
        return list;
    }

}