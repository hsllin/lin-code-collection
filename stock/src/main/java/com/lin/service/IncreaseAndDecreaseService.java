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
import com.taobao.api.internal.toplink.logging.LogUtil;
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
        getAutionTradingData();
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
        String condition = "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"连续2日上涨;_&_今日涨幅>2%;_&_量比>1.2; _&_5日均线>10日均线>20日均线; _&_换手率>5%;_&_流通市值<200亿>30亿; _&_非st股;_&_非停牌_&_所属行业和概念\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":24,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"连续上涨天数\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\",\"=2\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"=\":\"2\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"连续上涨天数=2日\",\"valueType\":\"_整型数值(天)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":22,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"交易日期 {{date}}\",\"(0.02\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"0.02\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的涨跌幅>2%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":20,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"量比\",\"indexProperties\":[\"交易日期 {{date}}\",\"(1.2\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"1.2\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的量比>1.2\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":18,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"今日的5日的均线>今日的10日的均线且今日的10日的均线>今日的20日的均线\",\"sonSize\":6,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"-\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"(0\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"n日 5日\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"5日\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"n日 10日\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"10日\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"-\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"(0\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"n日 10日\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"10日\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"均线\",\"indexProperties\":[\"n日 20日\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"tech\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"n日\":\"20日\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":10,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"换手率\",\"indexProperties\":[\"交易日期 {{date}}\",\"(0.05\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"0.05\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"今日的换手率>5%\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":8,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"今日的a股市值(不含限售股)<200亿元且今日的a股市值(不含限售股)>30亿元\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"交易日期 {{date}}\",\"<20000000000\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"<\":\"20000000000\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"dateText\":\"今日\",\"ci\":false,\"indexName\":\"a股市值(不含限售股)\",\"indexProperties\":[\"交易日期 {{date}}\",\"(3000000000\"],\"dateUnit\":\"日\",\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"(\":\"3000000000\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_浮点型数值(元)\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"股票简称\",\"indexProperties\":[\"不包含st\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"不包含\":\"st\"},\"reportType\":\"null\",\"score\":0,\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"股票简称不包含st\",\"valueType\":\"_股票简称\",\"sonSize\":0},{\"score\":0,\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"2025年06月04日交易状态包含交易>-<新股上市,2025年06月04日交易状态不包含停牌>-<暂停上市\",\"sonSize\":2,\"opPropertyMap\":{},\"source\":\"new_parser\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"交易状态\",\"indexProperties\":[\"交易日期 {{date}}\",\"包含交易>-<新股上市\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"包含\":\"交易>-<新股上市\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_交易状态\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"交易状态\",\"indexProperties\":[\"交易日期 {{date}}\",\"不包含停牌>-<暂停上市\"],\"source\":\"new_parser\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"不包含\":\"停牌>-<暂停上市\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"valueType\":\"_交易状态\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", tradeDay);
        param.put("query", "连续2日上涨；今日涨幅>2%；量比>1.2；  5日均线>10日均线>20日均线；  换手率>5%；流通市值<200亿>30亿；  非ST股；非停牌；所属行业和概念；");
        param.put("question", "连续2日上涨；今日涨幅>2%；量比>1.2；  5日均线>10日均线>20日均线；  换手率>5%；流通市值<200亿>30亿；  非ST股；非停牌；所属行业和概念；");
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
            bean.setRate(CommonUtils.formatStringPrice(txt.get("涨跌幅:前复权" + date) + ""));
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
        } catch (Exception e){
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
        } catch (Exception e) {
            System.out.println(e);
        }
        List<IncreaseRankData> list1 = list.subList(0, 20);
        List<IncreaseRankData> list2 = list.subList(20, 40);
        List<IncreaseRankData> list3 = list.subList(40, 60);
        List<String[]> result1 = BeanFieldConverterUtil.convertBeansToFieldArrays(list1);
        List<String[]> result2 = BeanFieldConverterUtil.convertBeansToFieldArrays(list2);
        List<String[]> result3 = BeanFieldConverterUtil.convertBeansToFieldArrays(list3);

        PhoUtil.createTableImage(result1, "D:\\1stock\\todayHotStock1.png");
        PhoUtil.createTableImage(result2, "D:\\1stock\\todayHotStock2.png");
        PhoUtil.createTableImage(result3, "D:\\1stock\\todayHotStock3.png");
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

}