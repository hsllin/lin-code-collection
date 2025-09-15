package com.lin.service;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.tonghuashun.AutionTradingBean;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.bean.tonghuashun.LimitUpData;
import com.lin.util.BeanFieldConverterUtil;
import com.lin.util.CommonUtils;
import com.lin.util.PhoUtil;
import com.lin.util.WencaiUtils;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 同花顺涨跌幅
 * @create 2025-05-29 11:04
 */
@Service
@Slf4j
public class IncreaseAndDecreaseService {
    private static final List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    );

    public static void main(String[] args) throws ScriptException, IOException {
//        getIncreaseData();
//        getDecreaseData();
//        test();
//        getOneWordData();
//        getYesterdayLimitUpData();
//        getAutionTradingData();
//        getHotConcept();
//        getDragonToGreenData();
//        getDragonToGreenData();
//        getIncreaseYesterdayData();
//        getIncreaseConcept("asc");
        getNewHighData();
    }

    public static List<IncreaseRankData> getIncreaseData() throws ScriptException, IOException, InterruptedException {

        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "涨幅榜");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"从大到小排名\",\"source\":\"text2sql\",\"score\":0,\"ciChunk\":\"涨幅榜\",\"createBy\":\"preCache\",\"node_type\":\"op\",\"chunkedResult\":\"涨幅榜\",\"children\":[],\"opName\":\"sort\",\"uiText\":\"涨跌幅从大到小排名\",\"sonSize\":1,\"opPropertyMap\":{}},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 20250606\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250606\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0}]";

        param.put("question", "个股涨幅榜显示行业和概念;涨停原因");
        param.put("query", "个股涨幅榜显示行业和概念;涨停原因");
        param.put("urp_sort_way", "desc");
        param.put("urp_sort_index", "涨跌幅:前复权[20250606]");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
//        param.put("urp_use_sort", "1");

//        param.put("urp_sort_way", "desc");
//        param.put("date_range[0]", "20250606");
//        param.put("date_range[1]", "20250606");
//
//        param.put("urp_use_sort", "涨跌幅:前复权"+tradeDay);
//        param.put("page", "1");
//        param.put("perpage", "100");
//        param.put("condition", condition);
//        param.put("logid", "409f71a78b7387d96bb0ce921466fdcb");
//        param.put("ret", "json_all");
//        param.put("sessionid", "409f71a78b7387d96bb0ce921466fdcb");
//        param.put("iwc_token", "0ac98cc517491758839608245");
//        param.put("uuids[0]", "24087");
//        param.put("comp_id", "6836372");
//        param.put("user_id", "721659935");
//        param.put("business_cat", "soniu");
//        param.put("uuid", "24087");


        Map<String, Object> result = WencaiUtils.getData("", param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
//        JsonNode txtNode = jsonNode.at("/answer/components").get(0).at("/data/datas");
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setIncreaseReason(txt.get("涨停原因类别" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("涨跌幅:前复权" + date) + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            list.add(bean);
        }
        return list;
    }

    public static List<IncreaseRankData> getDecreaseData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
//        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"个股跌幅榜显示行业和概念_&_跌停原因\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":7,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"涨跌幅小于0并且涨跌幅从小到大排名并且所属同花顺行业并且所属概念\",\"sonSize\":5,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"<0\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"0\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅小于0\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从小到大排名\",\"uiText\":\"涨跌幅从小到大排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"跌停原因类型\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"跌停原因\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"跌停原因类型\",\"valueType\":\"_跌停原因类别\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "个股跌幅榜显示行业和概念;跌停原因");
        param.put("question", "个股跌幅榜显示行业和概念;跌停原因");
        param.put("urp_use_sort", "涨跌幅:前复权" + date);
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
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


        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setIncreaseReason(txt.get("跌停原因类型" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("涨跌幅:前复权" + date) + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取同花顺一字涨停
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getOneWordData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "一字涨停；涨停原因；所属行业和概念；大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
//        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"个股跌幅榜显示行业和概念_&_跌停原因\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":7,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"涨跌幅小于0并且涨跌幅从小到大排名并且所属同花顺行业并且所属概念\",\"sonSize\":5,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"<0\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"0\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅小于0\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从小到大排名\",\"uiText\":\"涨跌幅从小到大排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"跌停原因类型\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"跌停原因\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"跌停原因类型\",\"valueType\":\"_跌停原因类别\",\"sonSize\":0}]";
//        condition = condition.replace("{{date}}", tradeDay);
//        param.put("query", "一字涨停；涨停原因；所属行业和概念");
//        param.put("urp_use_sort", "涨跌幅:前复权" + date);
//        param.put("page", "1");
//        param.put("perpage", "100");


        Map<String, Object> result = WencaiUtils.getRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);

        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setIncreaseReason(txt.get("涨停原因类别" + date) + "");
            bean.setIncreaseType(txt.get("涨停类型" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            list.add(bean);
        }
        Iterator<IncreaseRankData> allstockBeanIterator = list.iterator();
        while (allstockBeanIterator.hasNext()) {
            IncreaseRankData allstockBean = allstockBeanIterator.next();
            if (allstockBean.getCode().startsWith("8") || allstockBean.getCode().startsWith("3") || allstockBean.getCode().startsWith("68") || allstockBean.getName().contains("ST")) {
                allstockBeanIterator.remove();
            }

        }
        return list;
    }

    /**
     * 获取同花顺一字涨停
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getTLimitupData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "t字涨停；涨停原因；所属行业和概念；大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);


        Map<String, Object> result = WencaiUtils.getRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);

        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setIncreaseReason(txt.get("涨停原因类别" + date) + "");
            bean.setIncreaseType(txt.get("涨停类型" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            list.add(bean);
        }
        Iterator<IncreaseRankData> allstockBeanIterator = list.iterator();
        while (allstockBeanIterator.hasNext()) {
            IncreaseRankData allstockBean = allstockBeanIterator.next();
            if (allstockBean.getCode().startsWith("8") || allstockBean.getCode().startsWith("3") || allstockBean.getCode().startsWith("68") || allstockBean.getName().contains("ST")) {
                allstockBeanIterator.remove();
            }

        }
        return list;
    }


    /**
     * 获取同花顺一字涨停
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getLeaderData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "一字涨停；涨停原因；所属行业和概念；大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
//        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"个股跌幅榜显示行业和概念_&_跌停原因\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":7,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"涨跌幅小于0并且涨跌幅从小到大排名并且所属同花顺行业并且所属概念\",\"sonSize\":5,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"<0\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"0\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅小于0\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从小到大排名\",\"uiText\":\"涨跌幅从小到大排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"跌停原因类型\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"跌停原因\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"跌停原因类型\",\"valueType\":\"_跌停原因类别\",\"sonSize\":0}]";
//        condition = condition.replace("{{date}}", tradeDay);
//        param.put("query", "一字涨停；涨停原因；所属行业和概念");
//        param.put("urp_use_sort", "涨跌幅:前复权" + date);
//        param.put("page", "1");
//        param.put("perpage", "100");


        Map<String, Object> result = WencaiUtils.getRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);

        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setIncreaseReason(txt.get("涨停原因类别" + date) + "");
            bean.setIncreaseType(txt.get("涨停类型" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            list.add(bean);


        }
        return list;
    }

    public static List<IncreaseRankData> getTrendData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
//        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"5日均线大于10日均线 _&_且 _&_10日均线大于20日均线 _&_且 _&_近20日涨幅大于25% _&_且 _&_近10日没有跌幅大于5%的_&_阴线 _&_且 _&_今日收盘价接近5日均线 _&_且 _&_非st股,_&_行业概念\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":31,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"-\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"(0\",\"uiText\":\"5日的均线>10日的均线\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"nodate 1\",\"n日 5日\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"5日\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"nodate 1\",\"n日 10日\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"10日\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":27,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"-\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"(0\",\"uiText\":\"10日的均线>20日的均线\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"nodate 1\",\"n日 10日\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"10日\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"nodate 1\",\"n日 20日\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"20日\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":23,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"近20日\",\"ci\":false,\"indexName\":\"区间涨跌幅:前复权\",\"indexProperties\":[\"起始交易日期 20250620\",\"截止交易日期 {{date}}\",\"(0.25\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"(\":\"0.25\",\"起始交易日期\":\"20250620\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"uiText\":\"近20日的区间涨跌幅>25%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":21,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"取反\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"近10日的区间涨跌幅<=-5%取反\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"近10日\",\"ci\":false,\"indexName\":\"区间涨跌幅:前复权\",\"indexProperties\":[\"起始交易日期 20250704\",\"截止交易日期 {{date}}\",\"<=-0.05\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"<=\":\"-0.05\",\"起始交易日期\":\"20250704\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":18,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"阴线\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"阴线\",\"valueType\":\"\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":16,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"今日的收盘价在今日的5日的均线左右\",\"sonSize\":10,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"-\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\">0\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"收盘价:不复权\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"*\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"n日 5日\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"5日\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"score\":0,\"node_type\":\"value\",\"ci\":false,\"sonSize\":0,\"source\":\"new_parser\",\"type\":\"number\",\"value\":0.97},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"-\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"<0\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"收盘价:不复权\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"*\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"n日 5日\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"5日\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"score\":0,\"node_type\":\"value\",\"ci\":false,\"sonSize\":0,\"source\":\"new_parser\",\"type\":\"number\",\"value\":1.03},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "5日均线大于10日均线 且 10日均线大于20日均线 且 近20日涨幅大于25% 且 近10日没有跌幅大于5%的阴线 且 今日收盘价接近5日均线 且 非ST股，行业概念");
        param.put("question", "5日均线大于10日均线 且 10日均线大于20日均线 且 近20日涨幅大于25% 且 近10日没有跌幅大于5%的阴线 且 今日收盘价接近5日均线 且 非ST股，行业概念");
//        param.put("urp_use_sort", "涨跌幅:前复权" + date);
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("ret", "json_all");
        param.put("sessionid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("iwc_token", "0ac9f00417490487071945389");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setIncreaseReason(txt.get("跌停原因类型" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setMinPrice(txt.get("最低价:不复权" + date) + "");
            bean.setMostPrice(txt.get("最高价:不复权" + date) + "");
            bean.setRangeRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setFivePrice(txt.get("5日均线" + date) + "");
            bean.setTenPrice(txt.get("10日均线" + date) + "");
            bean.setTwentyPrice(txt.get("20日均线" + date) + "");
            list.add(bean);
        }
        Iterator<IncreaseRankData> allstockBeanIterator = list.iterator();
        while (allstockBeanIterator.hasNext()) {
            IncreaseRankData allstockBean = allstockBeanIterator.next();
            if (allstockBean.getCode().startsWith("8") || allstockBean.getCode().startsWith("3") || allstockBean.getCode().startsWith("68") || allstockBean.getName().contains("ST")) {
                allstockBeanIterator.remove();
            }

        }
        return list;
    }

    public static List<IncreaseRankData> getTop100Data() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String today = "[" + DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue()) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
//        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"热点个股前100_&_所属行业概念_&_所属行业_&_大单净量\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":9,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"score\":0,\"ciChunk\":\"热点个股前100\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"个股热度排名小于等于100并且个股热度从大到小排名\",\"sonSize\":3,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"个股热度排名\",\"indexProperties\":[\"<=100\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"<=\":\"100\",\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"NATURAL_DAILY\",\"score\":0,\"ciChunk\":\"热点个股前100\",\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"uiText\":\"个股热度排名小于等于100\",\"valueType\":\"_整型数值\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"热点个股前100\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从大到小排名\",\"uiText\":\"个股热度从大到小排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"个股热度\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"NATURAL_DAILY\",\"score\":0,\"ciChunk\":\"热点个股前100\",\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"uiText\":\"个股热度\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"\",\"source\":\"text2sql\",\"score\":0,\"ciChunk\":\"所属行业概念\",\"createBy\":\"preCache\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"uiText\":\"所属同花顺行业并且所属概念\",\"sonSize\":2,\"opPropertyMap\":{}},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"所属行业概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"所属行业概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"所属行业\",\"createBy\":\"cache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"dde大单净量\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"大单净量\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"dde大单净量\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "热点个股前100；所属行业概念；所属行业;大单净量");
        param.put("question", "热点个股前100；所属行业概念；所属行业;大单净量");
        param.put("urp_use_sort", "个股热度" + date);
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("ret", "json_all");
        param.put("sessionid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("iwc_token", "0ac9c90317490919776873954");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        try {
            for (int i = 0; i < txtMap.size(); i++) {
                Map<String, Object> txt = txtMap.get(i);
                IncreaseRankData bean = new IncreaseRankData();
                bean.setCode(txt.get("code") + "");
                bean.setName(txt.get("股票简称") + "");
                bean.setPrice(txt.get("最新价") + "");
                bean.setIncreaseReason(txt.get("跌停原因类型" + date) + "");
                bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
                bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
                bean.setCicuration(txt.get("流通a股" + date) + "");
                bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
                bean.setConcepts(txt.get("所属概念") + "");
                bean.setIndustry(txt.get("所属同花顺行业") + "");
                Double heat = Double.valueOf(txt.get("个股热度" + today) + "") / 10000;
                bean.setHeat(CommonUtils.formatPrice(heat));
                list.add(bean);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public static List<IncreaseRankData> getLianBanData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String today = "[" + DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue()) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
//        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"今日连板池_&_非st\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"连续涨停天数\",\"indexProperties\":[\"(=2\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(=\":\"2\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"今日连板池\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的连续涨停天数大于等于2\",\"valueType\":\"_整型数值(天)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "今日连板池；非St");
        param.put("question", "今日连板池；非St");
        param.put("urp_use_sort", "个股热度" + date);
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("ret", "json_all");
        param.put("sessionid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("iwc_token", "0ac9c90317490919776873954");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            list.add(bean);
        }
        return list;
    }

    public static void downloadTrendData() {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\今日趋势.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<IncreaseRankData> list = getTrendData();
            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void downloadOneWordData() {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\今日一字.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<IncreaseRankData> list = getOneWordData();
            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }


    public static void generateTrendDataPhoto() {
        List<IncreaseRankData> list = new ArrayList<>();
        try {
            list = getTop100Data();
        } catch (Exception e) {

        }
        for (int i = 0; i < list.size(); i++) {
            IncreaseRankData bean = list.get(i);
//            bean.setRank(String.valueOf(i + 1));
            bean.setCode(list.get(i).getName() + "\n" + list.get(i).getCode());
            bean.setName("");
            bean.setConcepts("");
            bean.setMoneyInNum("");
            bean.setCicuration("");
            bean.setIncreaseReason("");
            bean.setIndustry("");
        }
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\今日热门.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();

            List<IncreaseRankData> list1 = list.subList(0, 20);
            List<String[]> result1 = BeanFieldConverterUtil.convertBeansToFieldArrays(list1);
            PhoUtil.createTableImage(result1, "D:\\1stock\\todayHotStock1.png");
            if (list.size() > 40) {
                List<IncreaseRankData> list2 = list.subList(20, 40);
                List<String[]> result2 = BeanFieldConverterUtil.convertBeansToFieldArrays(list2);
                PhoUtil.createTableImage(result2, "D:\\1stock\\todayHotStock2.png");
            }
            if (list.size() >= 60) {
                List<IncreaseRankData> list3 = list.subList(40, 60);
                List<String[]> result3 = BeanFieldConverterUtil.convertBeansToFieldArrays(list3);
                PhoUtil.createTableImage(result3, "D:\\1stock\\todayHotStock3.png");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * 获取上日涨停数据
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<LimitUpData> getYesterdayLimitUpData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(1) + "]";
        String today = "[" + DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue()) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
//        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(1);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"上日涨停_&_涨停封单量从大到小排列_&_非st的股票\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"上个交易日\",\"ci\":false,\"indexName\":\"涨停\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"上日涨停\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"上个交易日的涨停\",\"valueType\":\"_是否\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"涨停封单量从大到小排列\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从大到小排名\",\"uiText\":\"涨停封单量从大到小排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"上个交易日\",\"ci\":false,\"indexName\":\"涨停封单量\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"涨停封单量从大到小排列\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨停封单量\",\"valueType\":\"_浮点型数值(股)\",\"sonSize\":0},{\"dateText\":\"上个交易日\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st的股票\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "上日涨停，涨停封单量从大到小排列，非st的股票");
        param.put("question", "上日涨停，涨停封单量从大到小排列，非st的股票");
        param.put("urp_sort_index", "涨停封单量" + date);
        param.put("urp_sort_way", "desc");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("ret", "json_all");
        param.put("sessionid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("iwc_token", "0ac9c90317490919776873954");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<LimitUpData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            LimitUpData bean = new LimitUpData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setLimitUpTime(txt.get("首次涨停时间" + date) + "");
            bean.setLimitUpNumType(txt.get("几天几板" + date) + "");
            bean.setLimitUpNum(txt.get("连续涨停天数" + date) + "");
            bean.setLimitUpHandNum(txt.get("涨停封单量" + date) + "");
            bean.setLimitUpType(txt.get("涨停类型" + date) + "");
            bean.setConcepts(txt.get("涨停原因类别" + date) + "");
            bean.setFlowMoney(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setLimitUpMoney(txt.get("涨停封单额" + date) + "");
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取上日涨停数据
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<LimitUpData> getTodayLimitUpData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String today = "[" + DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue()) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
//        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"今日涨停_&_涨停封单量从大到小排列_&_非st的股票\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"涨停\",\"indexProperties\":[\"交易日期 20250701\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250701\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"今日涨停\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的涨停\",\"valueType\":\"_是否\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"涨停封单量从大到小排列\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从大到小排名\",\"uiText\":\"涨停封单量从大到小排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"涨停封单量\",\"indexProperties\":[\"交易日期 20250701\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250701\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"涨停封单量从大到小排列\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨停封单量\",\"valueType\":\"_浮点型数值(股)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st的股票\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "今日涨停，涨停封单量从大到小排列，非st的股票");
        param.put("question", "今日涨停，涨停封单量从大到小排列，非st的股票");
        param.put("urp_sort_index", "涨停封单量" + date);
        param.put("urp_sort_way", "desc");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("ret", "json_all");
        param.put("sessionid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("iwc_token", "0ac9c90317490919776873954");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<LimitUpData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            LimitUpData bean = new LimitUpData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setLimitUpTime(txt.get("首次涨停时间" + date) + "");
            bean.setLimitUpNumType(txt.get("几天几板" + date) + "");
            bean.setLimitUpNum(txt.get("连续涨停天数" + date) + "");
            bean.setLimitUpHandNum(txt.get("涨停封单量" + date) + "");
            bean.setLimitUpType(txt.get("涨停类型" + date) + "");
            bean.setConcepts(txt.get("涨停原因类别" + date) + "");
            bean.setFlowMoney(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setLimitUpMoney(txt.get("涨停封单额" + date) + "");
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取上日涨停数据
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<AutionTradingBean> getAutionTradingData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String today = "[" + DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue()) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
//        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"集合竞价未匹配量排行,_&_非st,_&_非退市,_&_涨停原因,_&_流通市值>35亿<200亿\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":11,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从大到小排名\",\"uiText\":\"2025年07月03日竞价未匹配量从大到小排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"竞价未匹配量\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(股)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":8,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st,退\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st,退\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":6,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st,退\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st,退\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含退\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨停原因类别\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨停原因类别\",\"valueType\":\"_涨停原因类别\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"a股市值(不含限售股)>35亿元且a股市值(不含限售股)<200亿元\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\",\"(3500000000\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"3500000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\",\"<20000000000\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"20000000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "集合竞价未匹配量排行，非st，非退市，涨停原因,流通市值>35亿<200亿");
        param.put("question", "集合竞价未匹配量排行，非st，非退市，涨停原因,流通市值>35亿<200亿");
        param.put("urp_sort_index", "竞价未匹配量" + date);
        param.put("urp_sort_way", "desc");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("ret", "json_all");
        param.put("sessionid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("iwc_token", "0ac9c90317490919776873954");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<AutionTradingBean> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            AutionTradingBean bean = new AutionTradingBean();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setType(txt.get("竞价异动说明" + date) + "");
            bean.setRatingType(txt.get("集合竞价评级" + date) + "");
            bean.setBuyNum(txt.get("竞价量" + date) + "");
            bean.setBuyMoney(String.valueOf(txt.get("竞价金额" + date)));
            bean.setUnMatchedNum(txt.get("竞价未匹配量" + date) + "");
            bean.setUnMatchedMoney(String.valueOf(txt.get("竞价未匹配金额" + date)));
            bean.setConcepts(txt.get("涨停原因类别" + date) + "");
            bean.setFlowMoney(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");

            list.add(bean);
        }
        return list;
    }

    /**
     * 获取上日涨停数据
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<AutionTradingBean> getAutionNoMatchedData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String today = "[" + DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue()) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
//        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"集合竞价未匹配量>10000手_&_竞价涨幅>0%_&_非st_&_非退市_&_非新股_&_流通市值大于35亿小于200亿_&_涨停原因\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":9,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"竞价未匹配量\",\"indexProperties\":[\"(10000\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"10000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"集合竞价未匹配量>10000\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"竞价未匹配量大于10000\",\"valueType\":\"_浮点型数值(股)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"竞价涨幅\",\"indexProperties\":[\"(0\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"0\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"竞价涨幅>0%\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"竞价涨幅大于0%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含退\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"退\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非退市\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含退\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"上市交易日天数\",\"indexProperties\":[\"(5\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"5\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"createBy\":\"user_define_index\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"上市交易日天数大于5\",\"valueType\":\"_整型数值(天)\",\"sonSize\":0},{\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"\",\"source\":\"text2sql\",\"score\":0,\"ciChunk\":\"流通市值大于35亿小于200亿\",\"createBy\":\"ner_con\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"uiText\":\"a股市值(不含限售股)大于35亿并且a股市值(不含限售股)小于200亿\",\"sonSize\":2,\"opPropertyMap\":{}},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"(3500000000\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"3500000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"流通市值大于35亿小于200亿\",\"createBy\":\"ner_con\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"a股市值(不含限售股)大于35亿\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"<20000000000\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"20000000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"流通市值大于35亿小于200亿\",\"createBy\":\"ner_con\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"a股市值(不含限售股)小于200亿\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"涨停原因类别\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"涨停原因\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨停原因类别\",\"valueType\":\"_涨停原因类别\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "集合竞价未匹配量＞10000手，竞价涨幅>0%，非st，非退市，非新股，流通市值大于35亿小于200亿，涨停原因");
        param.put("question", "集合竞价未匹配量＞10000手，竞价涨幅>0%，非st，非退市，非新股，流通市值大于35亿小于200亿，涨停原因");
        param.put("urp_sort_index", "竞价未匹配量" + date);
        param.put("urp_sort_way", "desc");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("ret", "json_all");
        param.put("sessionid", "35374a74aaede40c7022fd57b648a0e3");
        param.put("iwc_token", "0ac9c90317490919776873954");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<AutionTradingBean> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            AutionTradingBean bean = new AutionTradingBean();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setType(txt.get("竞价异动说明" + date) + "");
            bean.setRatingType(txt.get("集合竞价评级" + date) + "");
            bean.setBuyNum(txt.get("竞价量" + date) + "");
            bean.setBuyMoney(String.valueOf(txt.get("竞价金额" + date)));
            bean.setUnMatchedNum(txt.get("竞价未匹配量" + date) + "");
            bean.setUnMatchedMoney(String.valueOf(txt.get("竞价未匹配金额" + date)));
            bean.setConcepts(txt.get("涨停原因类别" + date) + "");
            bean.setFlowMoney(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取同花顺一字涨停
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static Map<String, List<IncreaseRankData>> getHotConcept() throws ScriptException, IOException {
        Map<String, List<IncreaseRankData>> resultMap = new HashMap<>();
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "概念热度");
        param.put("page", "1");
        param.put("perpage", "50");
        String tradeDay = CommonUtils.getTradeDay(0);
//        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"个股跌幅榜显示行业和概念_&_跌停原因\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":7,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"涨跌幅小于0并且涨跌幅从小到大排名并且所属同花顺行业并且所属概念\",\"sonSize\":5,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"<0\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"0\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅小于0\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从小到大排名\",\"uiText\":\"涨跌幅从小到大排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股跌幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"跌停原因类型\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"跌停原因\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"跌停原因类型\",\"valueType\":\"_跌停原因类别\",\"sonSize\":0}]";
//        condition = condition.replace("{{date}}", tradeDay);
//        param.put("query", "一字涨停；涨停原因；所属行业和概念");
//        param.put("urp_use_sort", "涨跌幅:前复权" + date);
//        param.put("page", "1");
//        param.put("perpage", "100");


        Map<String, Object> result = WencaiUtils.getConceptRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
        JsonNode industryNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(2).at("/tab_list").get(0).at("/list").get(1).at("/data/datas");
        JsonNode conceptNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(2).at("/tab_list").get(1).at("/list").get(1).at("/data/datas");
        List<Map<String, Object>> industryMap = mapper.convertValue(industryNode, List.class);
        List<Map<String, Object>> conceptMap = mapper.convertValue(conceptNode, List.class);

        List<IncreaseRankData> industryList = new ArrayList<>();
        List<IncreaseRankData> conceptList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String, Object> txt = industryMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("指数@领涨股简称") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("指数@涨跌幅:前复权") + ""));
            bean.setConcepts(txt.get("指数简称") + "");
            industryList.add(bean);
        }
        for (int i = 0; i < 6; i++) {
            Map<String, Object> txt = conceptMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("指数@领涨股简称") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("指数@涨跌幅:前复权") + ""));
            bean.setConcepts(txt.get("指数简称") + "");
            conceptList.add(bean);
        }
        resultMap.put("industry", industryList);
        resultMap.put("concepts", conceptList);
        return resultMap;
    }

    public static List<IncreaseRankData> getTradeData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"成交额超过10亿排行_&_非st_&_行业概念\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":8,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"score\":0,\"ciChunk\":\"成交额超过10亿排行\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"成交额大于10亿并且成交额从大到小排名\",\"sonSize\":3,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"成交额\",\"indexProperties\":[\"(1000000000\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"1000000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"成交额超过10亿排行\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"成交额大于10亿\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"成交额超过10亿排行\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从大到小排名\",\"uiText\":\"成交额从大到小排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"成交额\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"成交额超过10亿排行\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"成交额\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"\",\"source\":\"text2sql\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"uiText\":\"所属同花顺行业并且所属概念\",\"sonSize\":2,\"opPropertyMap\":{}},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "成交额超过10亿排行，非st，行业概念");
        param.put("question", "成交额超过10亿排行，非st，行业概念");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("ret", "json_all");
        param.put("sessionid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("iwc_token", "0ac9f00417490487071945389");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            list.add(bean);
        }
        return list;
    }

    /**
     * 弱转强
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getWeakToStrongData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"近10日涨幅大于20%_&_近3日有阴线_&_今日为阳线_&_今日涨幅大于3%_&_今日放量_&_非st股_&_流通市值小于200亿大于35亿_&_涨跌幅_&_行业概念\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":13,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"近10日\",\"ci\":true,\"indexName\":\"区间涨跌幅:前复权\",\"indexProperties\":[\"(0.2\",\"起始交易日期 20250704\",\"截止交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"(\":\"0.2\",\"起始交易日期\":\"20250704\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"近10日涨幅大于20%\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"uiText\":\"近10日的涨跌幅大于20%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"近3日\",\"ci\":false,\"indexName\":\"阴线\",\"indexProperties\":[\"出现次数 >=1\",\"起始交易日期 20250715\",\"截止交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"tech\",\"indexPropertiesMap\":{\"出现次数\":\">=1\",\"起始交易日期\":\"20250715\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"近3日有阴线\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"近3日的阴线出现次数大于等于1\",\"valueType\":\"\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"阳线\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"今日为阳线\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的阳线\",\"valueType\":\"\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"(0.03\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"0.03\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"今日涨幅大于3%\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的涨跌幅大于3%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"放量\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"今日放量\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的放量\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st股\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"\",\"source\":\"text2sql\",\"score\":0,\"ciChunk\":\"流通市值小于200亿大于35亿\",\"createBy\":\"ner_con\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"uiText\":\"a股市值(不含限售股)小于200亿并且a股市值(不含限售股)大于35亿\",\"sonSize\":2,\"opPropertyMap\":{}},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"<20000000000\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"20000000000\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"流通市值小于200亿大于35亿\",\"createBy\":\"ner_con\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"a股市值(不含限售股)小于200亿\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"(3500000000\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"3500000000\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"流通市值小于200亿大于35亿\",\"createBy\":\"ner_con\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"a股市值(不含限售股)大于35亿\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"涨跌幅\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"\",\"source\":\"text2sql\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"uiText\":\"所属同花顺行业并且所属概念\",\"sonSize\":2,\"opPropertyMap\":{}},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "近10日涨幅大于20% 且 近3日有阴线 且 今日为阳线 且 今日涨幅大于3% 且 今日放量 且 非ST股 且 流通市值小于200亿大于35亿，涨跌幅，行业概念");
        param.put("question", "近10日涨幅大于20% 且 近3日有阴线 且 今日为阳线 且 今日涨幅大于3% 且 今日放量 且 非ST股 且 流通市值小于200亿大于35亿，涨跌幅，行业概念");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("ret", "json_all");
        param.put("sessionid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("iwc_token", "0ac9f00417490487071945389");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("涨跌幅:前复权" + date) + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            list.add(bean);
        }
        return list;
    }

    /**
     * 龙头首阴
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getDragonToGreenData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
//        String tradeDay = CommonUtils.getTradeDay(0);
//        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"近10日涨幅大于20%_&_近3日有阴线_&_今日为阳线_&_今日涨幅大于1%_&_今日放量_&_非st股_&_流通市值小于200亿大于35亿_&_涨跌幅_&_行业概念\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":13,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"近10日\",\"ci\":true,\"indexName\":\"区间涨跌幅:前复权\",\"indexProperties\":[\"(0.2\",\"起始交易日期 20250704\",\"截止交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"(\":\"0.2\",\"起始交易日期\":\"20250704\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"近10日涨幅大于20%\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"uiText\":\"近10日的涨跌幅大于20%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"近3日\",\"ci\":false,\"indexName\":\"阴线\",\"indexProperties\":[\"出现次数 >=1\",\"起始交易日期 20250715\",\"截止交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"tech\",\"indexPropertiesMap\":{\"出现次数\":\">=1\",\"起始交易日期\":\"20250715\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"近3日有阴线\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"近3日的阴线出现次数大于等于1\",\"valueType\":\"\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"阳线\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"今日为阳线\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的阳线\",\"valueType\":\"\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"(0.01\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"0.01\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"今日涨幅大于1%\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的涨跌幅大于1%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"放量\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"今日放量\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的放量\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st股\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"\",\"source\":\"text2sql\",\"score\":0,\"ciChunk\":\"流通市值小于200亿大于35亿\",\"createBy\":\"ner_con\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"uiText\":\"a股市值(不含限售股)小于200亿并且a股市值(不含限售股)大于35亿\",\"sonSize\":2,\"opPropertyMap\":{}},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"<20000000000\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"20000000000\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"流通市值小于200亿大于35亿\",\"createBy\":\"ner_con\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"a股市值(不含限售股)小于200亿\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"(3500000000\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"3500000000\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"流通市值小于200亿大于35亿\",\"createBy\":\"ner_con\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"a股市值(不含限售股)大于35亿\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"涨跌幅\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"\",\"source\":\"text2sql\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"uiText\":\"所属同花顺行业并且所属概念\",\"sonSize\":2,\"opPropertyMap\":{}},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":true,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"行业概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0}]";
//        condition = condition.replace("{{date}}", tradeDay);
//        param.put("query", "近10日涨幅大于20% 且 近3日有阴线 且 今日为阳线 且 今日涨幅大于1% 且 今日放量 且 非ST股 且 流通市值小于200亿大于35亿，涨跌幅，行业概念");
//        param.put("question", "近10日涨幅大于20% 且 近3日有阴线 且 今日为阳线 且 今日涨幅大于1% 且 今日放量 且 非ST股 且 流通市值小于200亿大于35亿，涨跌幅，行业概念");
//        param.put("page", "1");
//        param.put("perpage", "100");
//        param.put("condition", condition);
//        param.put("logid", "0f22ab71785c210b94f61de3b8267d9c");
//        param.put("ret", "json_all");
//        param.put("sessionid", "0f22ab71785c210b94f61de3b8267d9c");
//        param.put("iwc_token", "0ac9f00417490487071945389");
//        param.put("uuids[0]", "24087");
//        param.put("comp_id", "6836372");
//        param.put("user_id", "Ths_iwencai_Xuangu_ta7wv6968l1v5l2l8orrl4ohcg77279g");
//        param.put("comp_id", "6836372");
//        param.put("business_cat", "soniu");
//        param.put("uuid", "24087");
        param.put("question", "近10日涨停次数大于等于2 且 昨日涨停 且 今日为阴线 且 非ST股 且 流通市值小于200亿大于35亿涨跌幅，行业概念");


        Map<String, Object> result = WencaiUtils.getRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
//        JsonNode txtNode = jsonNode.at("/answer/components").get(0).at("/data/datas");
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("涨跌幅:前复权" + date) + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            list.add(bean);
        }
        return list;
    }

    /**
     * 弱转强
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getIncreaseYesterdayData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"近5日有过涨停 _&_且 _&_今日实时价格大于昨日最高价 _&_且 _&_非st股,_&_市值大于35亿,_&_行业概念,_&_且_&_连续涨停<1,_&_最新价格\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":16,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"近5日\",\"ci\":false,\"indexName\":\"涨停次数\",\"indexProperties\":[\"起始交易日期 20250716\",\"截止交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"起始交易日期\":\"20250716\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"uiText\":\"近5日的涨停次数\",\"valueType\":\"_整型数值(次|个)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":14,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"-\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"(0\",\"uiText\":\"今日的收盘价>昨日的最高价\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"收盘价:不复权\",\"indexProperties\":[\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"dateText\":\"昨日\",\"ci\":false,\"indexName\":\"最高价:不复权\",\"indexProperties\":[\"交易日期 20250721\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250721\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":10,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":8,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"总市值\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\",\"(3500000000\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"3500000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"总市值>35亿元\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":6,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"连续涨停天数\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\",\"<1\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"1\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"连续涨停天数<1日\",\"valueType\":\"_整型数值(天)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"收盘价:不复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"收盘价\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "近5日有过涨停 且 今日实时价格大于昨日最高价 且 非ST股，市值大于35亿，行业概念，且连续涨停<1，最新价格");
        param.put("question", "近5日有过涨停 且 今日实时价格大于昨日最高价 且 非ST股，市值大于35亿，行业概念，且连续涨停<1，最新价格");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("ret", "json_all");
        param.put("sessionid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("iwc_token", "0ac9f00417490487071945389");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("收盘价:不复权" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取最近有过涨停高开1.5%的票
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getStrongMorningData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String morningDate = "[" + CommonUtils.getTradeDay(0) + " 09:25]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"早上开盘涨幅大于等于1.5%_&_近10日有过涨停_&_非st_&_市值大于等于35亿_&_行业和概念_&_成交额\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":8,\"source\":\"text2sql\"},{\"dateText\":\"9点25分\",\"ci\":false,\"indexName\":\"分时涨跌幅:前复权\",\"indexProperties\":[\"(=0.015\",\"交易日期 {{date}}\",\"交易时间 09:25\",\"区间偏移 [0,0]\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"区间偏移\":\"[0,0]\",\"(=\":\"0.015\",\"交易时间\":\"09:25\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"早上开盘涨幅大于等于1.5%\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"开盘的涨跌幅大于等于1.5%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"近10日\",\"ci\":true,\"indexName\":\"涨停次数\",\"indexProperties\":[\"起始交易日期 20250722\",\"截止交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"起始交易日期\":\"20250722\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"近10日有过涨停\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"uiText\":\"近10日的涨停次数\",\"valueType\":\"_整型数值(次|个)\",\"sonSize\":0},{\"dateText\":\"近10日\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"总市值\",\"indexProperties\":[\"(=3500000000\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(=\":\"3500000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"市值大于等于35亿\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"总市值大于等于35亿\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"行业和概念\",\"createBy\":\"preCache\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":true,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"所属同花顺行业并且所属概念\",\"sonSize\":2,\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"行业和概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"行业和概念\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"成交额\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"成交额\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"成交额\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "早上开盘涨幅大于等于1.5%，且近10日有过涨停，非st，市值大于等于35亿,行业和概念，成交额");
        param.put("question", "早上开盘涨幅大于等于1.5%，且近10日有过涨停，非st，市值大于等于35亿,行业和概念，成交额");
        param.put("urp_sort_index", "最新涨跌幅");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("ret", "json_all");
        param.put("sessionid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("iwc_token", "0ac9f00417490487071945389");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            bean.setMorningRate(CommonUtils.formatStringPrice(txt.get("分时涨跌幅:前复权" + morningDate) + ""));
            list.add(bean);
        }
        return list;
    }

    /**
     * 创60日新高
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getNewHighData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "股价创60日新高，近十日有过涨停，行业概念");
        param.put("page", "1");
        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);


        Map<String, Object> result = WencaiUtils.getRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);

        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setIncreaseReason(txt.get("涨停原因类别" + date) + "");
            bean.setIncreaseType(txt.get("涨停类型" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            list.add(bean);
        }
        Iterator<IncreaseRankData> allstockBeanIterator = list.iterator();
        while (allstockBeanIterator.hasNext()) {
            IncreaseRankData allstockBean = allstockBeanIterator.next();
            if (allstockBean.getCode().startsWith("8") || allstockBean.getCode().startsWith("3") || allstockBean.getCode().startsWith("68") || allstockBean.getName().contains("ST")) {
                allstockBeanIterator.remove();
            }

        }
        return list;
    }

    /**
     * 获取同花顺涨幅板块
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getIncreaseConcept(String sort) throws ScriptException, IOException {
        Map<String, List<IncreaseRankData>> resultMap = new HashMap<>();
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "涨停家数占比前100的指数，领涨股简称");
        param.put("query", "涨停家数占比前100的指数，领涨股简称");
        param.put("urp_sort_way", sort);
        param.put("page", "1");
        param.put("perpage", "50");
        param.put("urp_sort_index", "指数@涨跌幅:前复权" + date);

        String tradeDay = CommonUtils.getTradeDay(0);
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"涨停家数占比前100的指数,_&_领涨股简称\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":5,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从大到小排名前100\",\"uiText\":\"2025年07月24日指数涨停家数占比从大到小排名前100\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"指数@涨停家数占比\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_a指领域\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"指数@领涨股简称\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_a指领域\",\"uiText\":\"领涨股简称\",\"valueType\":\"_领涨股简称\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"指数简称\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_a指领域\",\"uiText\":\"指数简称\",\"valueType\":\"_指数简称\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("condition", condition);
//        param.put("query", "一字涨停；涨停原因；所属行业和概念");
//        param.put("urp_use_sort", "涨跌幅:前复权" + date);
//        param.put("page", "1");
//        param.put("perpage", "100");


        Map<String, Object> result = WencaiUtils.getConceptData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
//        JsonNode industryNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        JsonNode industryNode = jsonNode.at("/answer/components").get(0).at("/data/datas");
        List<Map<String, Object>> industryMap = mapper.convertValue(industryNode, List.class);

        List<IncreaseRankData> industryList = new ArrayList<>();
        List<IncreaseRankData> conceptList = new ArrayList<>();
        for (int i = 0; i < industryMap.size(); i++) {
            Map<String, Object> txt = industryMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("指数@领涨股简称" + date) + "");
            System.out.println(CommonUtils.formatStringPrice(txt.get("指数@涨跌幅:前复权" + date) + ""));
            bean.setRate(CommonUtils.formatStringPrice(txt.get("指数@涨跌幅:前复权" + date) + ""));
            bean.setConcepts(txt.get("指数简称") + "");
            industryList.add(bean);
        }
        return industryList;
    }

    /**
     * 反包大阳线
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getStockPackagingData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "反包大阳线，行业概念,dde大单净量");
        param.put("page", "1");
        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);


        Map<String, Object> result = WencaiUtils.getRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);

        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            list.add(bean);
        }
        Iterator<IncreaseRankData> allstockBeanIterator = list.iterator();
        while (allstockBeanIterator.hasNext()) {
            IncreaseRankData allstockBean = allstockBeanIterator.next();
            if (allstockBean.getCode().startsWith("8") || allstockBean.getCode().startsWith("3") || allstockBean.getCode().startsWith("68") || allstockBean.getName().contains("ST")) {
                allstockBeanIterator.remove();
            }

        }
        return list;
    }

    /**
     * 低吸上涨趋势中正常回调的个股
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getStockBuyLowNormal() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "低吸上涨趋势中正常回调的个股，行业概念,dde大单净量");
        param.put("page", "1");
        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);


        Map<String, Object> result = WencaiUtils.getRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅" ) + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            list.add(bean);
        }
        Iterator<IncreaseRankData> allstockBeanIterator = list.iterator();
        while (allstockBeanIterator.hasNext()) {
            IncreaseRankData allstockBean = allstockBeanIterator.next();
            if (allstockBean.getCode().startsWith("8") || allstockBean.getCode().startsWith("3") || allstockBean.getCode().startsWith("68") || allstockBean.getName().contains("ST")) {
                allstockBeanIterator.remove();
            }

        }
        return list;
    }

    /**
     * 低吸热门强势妖股
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getStockBuyLowStrong() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/customized/chart/get-robot-data";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "低吸热门强势妖股，行业概念,dde大单净量");
        param.put("page", "1");
        param.put("perpage", "100");
        String tradeDay = CommonUtils.getTradeDay(0);


        Map<String, Object> result = WencaiUtils.getRootData(url, param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);

        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setIncreaseReason(txt.get("涨停原因类别" + date) + "");
            bean.setIncreaseType(txt.get("涨停类型" + date) + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));
            bean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            list.add(bean);
        }
        Iterator<IncreaseRankData> allstockBeanIterator = list.iterator();
        while (allstockBeanIterator.hasNext()) {
            IncreaseRankData allstockBean = allstockBeanIterator.next();
            if (allstockBean.getCode().startsWith("8") || allstockBean.getCode().startsWith("3") || allstockBean.getCode().startsWith("68") || allstockBean.getName().contains("ST")) {
                allstockBeanIterator.remove();
            }

        }
        return list;
    }

    /**
     * 获取同花顺放量2倍以上涨幅大于3.5%的票
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getStockIncreaseVolumeData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String morningDate = "[" + CommonUtils.getTradeDay(0) + " 09:25]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition ="[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"成交量放大2倍以上_&_非st_&_涨幅大于4.5%_&_市值大于35亿_&_阳线_&_所属板块和概念_&_非创业板_&_非北交所_&_非科创板_&_dde大单净量_&_涨跌幅\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":13,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"倍量\",\"indexProperties\":[\"(=2\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(=\":\"2\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"成交量放大2倍以上\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"倍量大于等于2倍\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非st\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"(0.035\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"0.035\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"涨幅大于4.5%\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅大于4.5%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"总市值\",\"indexProperties\":[\"(3500000000\",\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"3500000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"市值大于35亿\",\"createBy\":\"ner_con\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"总市值大于35亿\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"阳线\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"阳线\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"阳线\",\"valueType\":\"\",\"sonSize\":0},{\"score\":0,\"ciChunk\":\"所属板块和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"股票市场类型并且所属概念\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票市场类型\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"所属板块和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票市场类型\",\"valueType\":\"_股票市场类型\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"所属板块和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"上市板块\",\"indexProperties\":[\"不包含创业板\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"创业板\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非创业板\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"上市板块不包含创业板\",\"valueType\":\"_上市板块\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"股票市场类型\",\"indexProperties\":[\"不包含北证a股\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"北证a股\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非北交所\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票市场类型不包含北交所\",\"valueType\":\"_股票市场类型\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"上市板块\",\"indexProperties\":[\"不包含科创板\"],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"科创板\"},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"非科创板\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"上市板块不包含科创板\",\"valueType\":\"_上市板块\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"dde大单净量\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"dde大单净量\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"dde大单净量\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"涨跌幅\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "成交量放大2倍以上，非st，涨幅大于4.5%，市值大于35亿，阳线，所属板块和概念，非创业板，非北交所，非科创板，dde大单净量，涨跌幅");
        param.put("question", "成交量放大2倍以上，非st，涨幅大于4.5%，市值大于35亿，阳线，所属板块和概念，非创业板，非北交所，非科创板，dde大单净量，涨跌幅");
        param.put("urp_sort_index", "最新涨跌幅");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("ret", "json_all");
        param.put("sessionid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("iwc_token", "0ac9f00417490487071945389");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("涨跌幅:前复权"+date) + ""));
            bean.setMoneyInNum(txt.get("dde大单净量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            bean.setMorningRate(CommonUtils.formatStringPrice(txt.get("分时涨跌幅:前复权" + morningDate) + ""));
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取同花顺放量2倍以上涨幅大于3.5%的票
     *
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public static List<IncreaseRankData> getStockDayangLineData() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String morningDate = "[" + CommonUtils.getTradeDay(0) + " 09:25]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        String tradeDay = CommonUtils.getTradeDay(0);
        String condition ="[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"带上下影线的阳线,_&_5日有过涨停,_&_市值大于35亿,_&_所属板块和概念,_&_非st股,_&_非创业板,_&_非北交所,_&_非科创板,_&_dde大单净量,_&_涨跌幅\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":24,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"上影线且下影线\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"上影线\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"下影线\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":20,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"阳线\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"阳线\",\"valueType\":\"\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":18,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"5日\",\"ci\":false,\"indexName\":\"涨停次数\",\"indexProperties\":[\"起始交易日期 20250904\",\"截止交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"起始交易日期\":\"20250904\",\"截止交易日期\":\"{{date}}\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"+区间\",\"domain\":\"abs_股票领域\",\"uiText\":\"5日的涨停次数\",\"valueType\":\"_整型数值(次|个)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":16,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"总市值\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\",\"(3500000000\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"3500000000\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"总市值>35亿元\",\"valueType\":\"_浮点型数值(元|港元|美元|英镑)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":14,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票市场类型\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票市场类型\",\"valueType\":\"_股票市场类型\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":12,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":10,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":8,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"上市板块\",\"indexProperties\":[\"不包含创业板\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"创业板\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"上市板块不包含创业板\",\"valueType\":\"_上市板块\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":6,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票市场类型\",\"indexProperties\":[\"不包含北证a股\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"北证a股\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票市场类型不包含北交所\",\"valueType\":\"_股票市场类型\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"上市板块\",\"indexProperties\":[\"不包含科创板\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"科创板\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"上市板块不包含科创板\",\"valueType\":\"_上市板块\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"dde大单净量\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"dde大单净量\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "带上下影线的阳线，5日有过涨停，市值大于35亿，所属板块和概念，非ST股，非创业板，非北交所，非科创板，dde大单净量，涨跌幅");
        param.put("question", "带上下影线的阳线，5日有过涨停，市值大于35亿，所属板块和概念，非ST股，非创业板，非北交所，非科创板，dde大单净量，涨跌幅");
        param.put("urp_sort_index", "最新涨跌幅");
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("ret", "json_all");
        param.put("sessionid", "0f22ab71785c210b94f61de3b8267d9c");
        param.put("iwc_token", "0ac9f00417490487071945389");
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
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        List<IncreaseRankData> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            IncreaseRankData bean = new IncreaseRankData();
            bean.setCode(txt.get("code") + "");
            bean.setName(txt.get("股票简称") + "");
            bean.setPrice(txt.get("最新价") + "");
            bean.setRate(CommonUtils.formatStringPrice(txt.get("涨跌幅:前复权"+date) + ""));
            bean.setMoneyInNum(txt.get("dde大单净量" + date) + "");
            bean.setCicuration(txt.get("流通a股" + date) + "");
            bean.setNetInFlow(txt.get("最新dde大单净额") + "");
            bean.setConcepts(txt.get("所属概念") + "");
            bean.setIndustry(txt.get("所属同花顺行业") + "");
            bean.setVolality(txt.get("a股市值(不含限售股)" + date) + "");
            bean.setTradeMoney(txt.get("成交额" + date) + "");
            bean.setMorningRate(CommonUtils.formatStringPrice(txt.get("分时涨跌幅:前复权" + morningDate) + ""));
            list.add(bean);
        }
        return list;
    }

    public static void downloadWeakToStrongData() {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\弱转强.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<IncreaseRankData> list = getWeakToStrongData();
            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void downDragonFirstGreenData() {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\龙头首阴.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<IncreaseRankData> list = getDragonToGreenData();
            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void downStockPackagingData() {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\反包大阳线.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<IncreaseRankData> list = getStockPackagingData();
            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void downloadStockBuyLowStrong() {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\低吸妖股.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<IncreaseRankData> list = getStockBuyLowStrong();
            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void downStockBuyLowNormalData() {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\低吸正常回调.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<IncreaseRankData> list = getStockBuyLowNormal();
            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public static void downStockIncreaseVolumeData() {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\放量2倍.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<IncreaseRankData> list = getStockIncreaseVolumeData();
            for (IncreaseRankData dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getPrice());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}