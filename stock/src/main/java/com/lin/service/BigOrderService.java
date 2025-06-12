package com.lin.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.lin.bean.intradaychange.IntradayChange;
import com.lin.bean.tonghuashun.BigOrderBean;
import com.lin.util.CommonUtils;
import com.lin.util.WencaiUtils;
import com.taobao.api.internal.toplink.logging.LogUtil;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-23 11:34
 */
@Service
public class BigOrderService {
    private static final List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    );

    public static void main(String[] args) throws ScriptException, IOException {
        getBigOrderList();
//        test();
    }

    public static List<BigOrderBean> getBigOrderList() throws ScriptException, IOException {
        String date = "[" + CommonUtils.getTradeDay(0) + "]";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        Map<String, Object> param = new HashMap<>();
        param.put("question", "大单净量");
//        param.put("perpage", "100");
        String condition = "[{\"dateText\":\"\",\"ci\":true,\"indexName\":\"dde大单净量\",\"indexProperties\":[\"nodate 1\",\"交易日期 {{date}}\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"{{date}}\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"大单净量\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"chunkedResult\":\"大单净量\",\"domain\":\"abs_股票领域\",\"uiText\":\"dde大单净量\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0}]";
        condition = condition.replace("{{date}}", CommonUtils.getTradeDay(0));
//        param.put("query", "%E5%A4%A7%E5%8D%95%E5%87%80%E9%87%8F&urp_sort_way=desc&urp_sort_index=dde%E5%A4%A7%E5%8D%95%E5%87%80%E9%87%8F%5B20250523%5D&page=1&perpage=50&addheaderindexes=&condition=%5B%7B%22dateText%22%3A%22%22%2C%22ci%22%3Atrue%2C%22indexName%22%3A%22dde%E5%A4%A7%E5%8D%95%E5%87%80%E9%87%8F%22%2C%22indexProperties%22%3A%5B%22nodate%201%22%2C%22%E4%BA%A4%E6%98%93%E6%97%A5%E6%9C%9F%2020250523%22%5D%2C%22dateUnit%22%3A%22%E6%97%A5%22%2C%22source%22%3A%22text2sql%22%2C%22type%22%3A%22index%22%2C%22indexPropertiesMap%22%3A%7B%22%E4%BA%A4%E6%98%93%E6%97%A5%E6%9C%9F%22%3A%2220250523%22%2C%22nodate%22%3A%221%22%7D%2C%22reportType%22%3A%22TRADE_DAILY%22%2C%22score%22%3A0%2C%22ciChunk%22%3A%22%E5%A4%A7%E5%8D%95%E5%87%80%E9%87%8F%22%2C%22createBy%22%3A%22preCache%22%2C%22node_type%22%3A%22index%22%2C%22dateType%22%3A%22%E4%BA%A4%E6%98%93%E6%97%A5%E6%9C%9F%22%2C%22chunkedResult%22%3A%22%E5%A4%A7%E5%8D%95%E5%87%80%E9%87%8F%22%2C%22domain%22%3A%22abs_%E8%82%A1%E7%A5%A8%E9%A2%86%E5%9F%9F%22%2C%22uiText%22%3A%22dde%E5%A4%A7%E5%8D%95%E5%87%80%E9%87%8F%22%2C%22valueType%22%3A%22_%E6%B5%AE%E7%82%B9%E5%9E%8B%E6%95%B0%E5%80%BC%28%25%29%22%2C%22sonSize%22%3A0%7D%5D&codelist=&indexnamelimit=&logid=a9bc42bba8365280f3baf30b8fc79110&ret=json_all&sessionid=a9bc42bba8365280f3baf30b8fc79110&source=Ths_iwencai_Xuangu&date_range%5B0%5D=20250523&iwc_token=0ac99da717480128552823150&urp_use_sort=1&user_id=721659935&uuids%5B0%5D=24087&query_type=stock&comp_id=6836372&business_cat=soniu&uuid=24087");
        param.put("query", "大单净量");
        param.put("urp_sort_index", "dde大单净量"+date);
        param.put("page", "1");
        param.put("perpage", "100");
        param.put("condition", condition);
        param.put("logid", "83aede70d9b7349b9376f59814c5b2eb");
        param.put("ret", "json_all");
        param.put("sessionid", "73d1bfa13378b3058147034fd2415994");
        param.put("iwc_token", "0ac9c07717484976362134127");
        param.put("comp_id", "6836372");
        param.put("user_id", "Ths_iwencai_Xuangu_ta7wv6968l1v5l2l8orrl4ohcg77279g");
        param.put("uuids[0]", "18369");
        param.put("comp_id", "6910323");
        param.put("business_cat", "soniu");
        param.put("uuid", "18369");


        Map<String, Object> result = WencaiUtils.getData("", param);
        // Convert Map to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);

        // Access /data/answer/txt using JSON Pointer
        JsonNode txtNode = jsonNode.at("/data/answer").get(0).at("/txt").get(0).at("/content/components").get(0).at("/data/datas");
//        JsonNode txtNode = jsonNode.at("/answer/components").get(0).at("/data/datas");
        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);

        List<BigOrderBean> list = new ArrayList<>();
        for (int i = 0; i < txtMap.size(); i++) {
            Map<String, Object> txt = txtMap.get(i);
            BigOrderBean bigOrderBean = new BigOrderBean();
            bigOrderBean.setCode(txt.get("股票代码") + "");
            bigOrderBean.setName(txt.get("股票简称") + "");
            bigOrderBean.setPrice(txt.get("最新价") + "");
            try{
            bigOrderBean.setRate(CommonUtils.formatStringPrice(txt.get("最新涨跌幅") + ""));}
            catch (Exception e){
            }
            bigOrderBean.setMoneyInNum(txt.get("dde大单净流入量" + date) + "");
            bigOrderBean.setCicuration(txt.get("流通a股" + date) + "");
            bigOrderBean.setNetInFlow(txt.get("dde大单净量" + date) + "");
            list.add(bigOrderBean);
        }
        Iterator<BigOrderBean> allstockBeanIterator = list.iterator();
        while (allstockBeanIterator.hasNext()) {
            BigOrderBean allstockBean = allstockBeanIterator.next();
            if (allstockBean.getCode().startsWith("8") || allstockBean.getCode().startsWith("3") || allstockBean.getCode().startsWith("68") || allstockBean.getName().contains("ST")) {
                allstockBeanIterator.remove();
            }
        }
        // 使用JSONPath表达式直接访问
//        List<Map<String, Object>> tMap = getNestedValue(result, "data.answer");
//        List<Map<String, Object>> answerMap = getNestedValue(tMap.get(0), "txt");
//        List<Map<String, Object>> contentMap = getNestedValue(answerMap.get(0), "content.components");
//        List<Map<String, Object>> resultMap = getNestedValue(contentMap.get(0), "data");


//        Map<String, Object> txtMap = (Map<String, Object>) contentMap.get(0);
//        txtMap.get("content");

        return list;
    }

    /**
     * 安全获取多层嵌套Map中的值
     *
     * @param map  原始Map
     * @param path 层级路径（如 "data.answer.txt"）
     * @return 目标值，不存在则返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> T getNestedValue(Map<?, ?> map, String path) {
        String[] keys = path.split("\\.");
        Object current = map;

        for (String key : keys) {
            if (!(current instanceof Map)) return null;
            current = ((Map<?, ?>) current).get(key);
            if (current == null) return null;
        }
        return (T) current;
    }

    public static void test() throws ScriptException {
        String targetUrl = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        String url = "https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
        if (StringUtils.isBlank(targetUrl)) {
            targetUrl = url;
        }
        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));
        String hexinV = WencaiUtils.generateHexinV();
        Map<String, Object> params = new HashMap<>();
        params.put("source", "Ths_iwencai_Xuangu");
        params.put("user_id", "721659935");
        params.put("user_name", "mx_721659935");
        params.put("version", "2.0");
        params.put("rsh", "721659935");

        params.put("query", "大单净量");
        params.put("urp_sort_way", "desc");
        params.put("page", "1");
        params.put("perpage", "50");
        params.put("addheaderindexes", "");
        params.put("condition", "[{\"dateText\":\"\",\"ci\":true,\"indexName\":\"dde大单净量\",\"indexProperties\":[\"nodate 1\",\"交易日期 20250523\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250523\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"大单净量\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"chunkedResult\":\"大单净量\",\"domain\":\"abs_股票领域\",\"uiText\":\"dde大单净量\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0}]");
        params.put("logid", "a9bc42bba8365280f3baf30b8fc79110");
        params.put("ret", "json_all");
//        params.put("sessionid", "a9bc42bba8365280f3baf30b8fc79110");
        params.put("user_id", "721659935");
        params.put("uuids[0]", "24087");
//        params.put("query_type", "stock");
        params.put("comp_id", "6836372");
        params.put("business_cat", "soniu");
        params.put("uuid", "24087");
        params.put("", "");
        String result = HttpRequest.post(targetUrl)
                .header("hexin-v", hexinV)// token鉴权
                .header("content-type", "application/x-www-form-urlencoded")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://www.iwencai.com")
                .header("pragma", "no-cache")
                .header("referer", "https://www.iwencai.com/unifiedwap/result?w=%E5%A4%A7%E5%8D%95%E5%87%80%E9%87%8F&querytype=stock&addSign=1748012857589")
                .form(params)
                .execute().body();
        System.out.println(result);
    }
}