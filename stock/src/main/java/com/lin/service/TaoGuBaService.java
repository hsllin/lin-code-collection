package com.lin.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.Constants;
import com.lin.bean.HotSpotBean;
import com.lin.bean.taoguba.TaoGuBaHotStock;
import com.lin.bean.taoguba.TaoGuBaHotStockPrice;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 来源：https://kuaixun.eastmoney.com/yw.html
 */
@Service
public class TaoGuBaService {
    public static void main(String[] args) {
//        getNews();
//        getTaoGuBaHotStock("2");
        getTaoGuBaStockPrice("");
    }

    public static TaoGuBaHotStock getTaoGuBaHotStock(String type) {
        //分为1小时热榜和24小时热榜
        String url = "https://www.tgb.cn/new/nrnt/getNoticeStock?type=H";
        if (type.equals("2")) {
            url = "https://www.tgb.cn/new/nrnt/getNoticeStock?type=D";
        }
        Map<String, Object> param = new HashMap<>();
        String result = HttpUtil.get(url, param);
        TaoGuBaHotStock bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), TaoGuBaHotStock.class);
        return bean;
    }

    public static TaoGuBaHotStockPrice getTaoGuBaStockPrice(String stocks) {
        String data = convertToUrlEncodedJsonArray(stocks);
        //分为1小时热榜和24小时热榜
        String url = null;
        try {
            url = "https://hq.tgb.cn/tgb/realHQList?stockCodeList=" + data;;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ;
        Map<String, Object> param = new HashMap<>();
        String result = HttpUtil.get(url, param);
        TaoGuBaHotStockPrice bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), TaoGuBaHotStockPrice.class);
        return bean;
    }

    /**
     * 将逗号分隔的股票代码字符串转换为URL编码的JSON数组
     *
     * @param stockCodes 逗号分隔的股票代码字符串
     * @return URL编码后的JSON数组字符串
     */
    public static String convertToUrlEncodedJsonArray(String stockCodes) {
        try {
            // 1. 分割字符串并转换为JSON数组格式
            String jsonArray = Arrays.stream(stockCodes.split(","))
                    .map(String::trim) // 去除空格
                    .map(code -> "\"" + code + "\"") // 添加双引号
                    .collect(Collectors.joining(",", "[", "]")); // 组合成JSON数组

            // 2. 进行URL编码
            return URLEncoder.encode(jsonArray, StandardCharsets.UTF_8.toString());

        } catch (Exception e) {
            throw new RuntimeException("转换过程中发生错误", e);
        }
    }
}
