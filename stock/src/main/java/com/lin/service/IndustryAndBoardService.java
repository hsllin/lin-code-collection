package com.lin.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.bean.YiDong;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.bean.xuangutong.RankCodeBean;
import com.lin.bean.xuangutong.XuanGuTongBoardBean;
import com.lin.bean.xuangutong.XuanGuTongCardBean;
import com.lin.bean.xuangutong.XuanGuTongStock;
import com.lin.util.WencaiUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-12 14:11
 */
@Service
public class IndustryAndBoardService {
    public static void main(String[] args) {
//        getStock("002878.SZ");
        getBoardCardList();
    }

    private static final List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    );

    public static List<String> getRankCodeList() {

        String url = "https://flash-api.xuangubao.com.cn/api/plate/rank";
        Map<String, Object> param = new HashMap<>();
        param.put("field", "core_avg_pcp");
        param.put("type", "0");


        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));

        String result = HttpRequest.get(url)
                .header("content-type", "application/x-www-form-urlencoded")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://xuangutong.com.cn")
                .header("pragma", "no-cache")
                .header("referer", "https://xuangutong.com.cn/zhutiku")
                .form(param)
                .execute().body();
        RankCodeBean codeBean = JSONUtil.toBean(result, RankCodeBean.class);

        return codeBean.getData();
    }

    public static List<XuanGuTongBoardBean> getBoardAndConceptList(List<String> codeList) {

        String url = "https://flash-api.xuangubao.com.cn/api/plate/data";
        Map<String, Object> param = new HashMap<>();
        param.put("fields", "plate_id,plate_name,fund_flow,rise_count,fall_count,stay_count,limit_up_count,core_avg_pcp,core_avg_pcp_rank,core_avg_pcp_rank_change,top_n_stocks,bottom_n_stocks");
        List<XuanGuTongBoardBean> resultList = new ArrayList<>();
        param.put("plates", codeList.stream()
                .collect(Collectors.joining(",")));

        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));

        String result = HttpRequest.get(url)
                .header("content-type", "application/json; charset=UTF-8")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://xuangutong.com.cn")
                .header("pragma", "no-cache")
                .header("referer", "https://xuangutong.com.cn/zhutiku")
                .form(param)
                .execute().body();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> temp = objectMapper.readValue(result, Map.class);
            Map<String, Object> dataMap = (Map<String, Object>) temp.get("data");
            for (String code : codeList) {
                XuanGuTongBoardBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(dataMap.get(code)), XuanGuTongBoardBean.class);
                resultList.add(bean);
            }
        } catch (Exception e) {

        }


        return resultList;
    }

    public static List<XuanGuTongCardBean.DataDTO.ItemsDTO> getBoardCardList() {

        String url = "https://baoer-api.xuangubao.com.cn/api/v2/tab/recommend?module=trending_plates";
        Map<String, Object> param = new HashMap<>();
        param.put("fields", "plate_id,plate_name,fund_flow,rise_count,fall_count,stay_count,limit_up_count,core_avg_pcp,core_avg_pcp_rank,core_avg_pcp_rank_change,top_n_stocks,bottom_n_stocks");
        List<XuanGuTongBoardBean> resultList = new ArrayList<>();

        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));

        String result = HttpRequest.get(url)
                .header("content-type", "application/json; charset=UTF-8")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://xuangutong.com.cn")
                .header("pragma", "no-cache")
                .header("referer", "https://xuangutong.com.cn/zhutiku")
                .form(param)
                .execute().body();

        XuanGuTongCardBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), XuanGuTongCardBean.class);
        List<XuanGuTongCardBean.DataDTO.ItemsDTO> list = bean.getData().getItems();
        for (XuanGuTongCardBean.DataDTO.ItemsDTO item : list) {
            List<String> codeList = new ArrayList<>();
            codeList.add(item.getPlateId());
            List<XuanGuTongBoardBean> boardBeanList = getBoardAndConceptList(codeList);
            if (boardBeanList != null && boardBeanList.size() > 0) {
                item.setPlateRate(boardBeanList.get(0).getCoreAvgPcp());
            }
            for (XuanGuTongCardBean.DataDTO.ItemsDTO.StocksDTO stocksDTO : item.getStocks()) {
                Map<String, Object> stockMap = getStock(stocksDTO.getSymbol());
                stocksDTO.setPrice(stockMap.get("price") + "");
                stocksDTO.setRate(stockMap.get("rate") + "");
            }
        }
        return list;
    }

    public static Map<String, Object> getStock(String code) {

        String url = "https://api-ddc-wscn.xuangubao.com.cn/market/real";
        Map<String, Object> param = new HashMap<>();
        param.put("fields", "fields=prod_name,last_px,px_change,px_change_rate,symbol,trade_status,market_type");
        param.put("prod_code", code);

        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));

        String result = HttpRequest.get(url)
                .header("content-type", "application/json; charset=UTF-8")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://xuangutong.com.cn")
                .header("pragma", "no-cache")
                .header("referer", "https://xuangutong.com.cn/zhutiku")
                .form(param)
                .execute().body();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = objectMapper.readValue(result, Map.class);
            Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
            Map<String, Object> stockMap = (Map<String, Object>) dataMap.get("snapshot");
            List<String> list = (List<String>) stockMap.get(code);
            if (list != null && list.size() > 0) {
                resultMap.put("price", list.get(0));
                resultMap.put("rate", list.get(2));
            }

        } catch (Exception e) {

        }
        return resultMap;
    }
}