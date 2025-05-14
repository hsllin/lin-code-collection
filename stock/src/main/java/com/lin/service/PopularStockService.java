package com.lin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.LianBanNew;
import com.lin.bean.popular.DongCaiPopularStock;
import com.lin.bean.popular.FlushPopularStock;
import com.lin.bean.popular.IndustryPopularStock;
import com.lin.bean.strongstock.StrongStockBean;
import com.lin.util.AdvancedConceptCloudUtil;
import com.lin.util.CommonUtils;
import com.lin.util.ConceptFrequencyAnalyzer;
import com.lin.util.DateUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 同花顺和东方财富热门股票
 * https://dq.10jqka.com.cn/fuyao/hot_list_data/out/hot_list/v1/stock?stock_type=a&type=hour&list_type=normal 大家都在看
 * https://dq.10jqka.com.cn/fuyao/hot_list_data/out/hot_list/v1/stock?stock_type=a&type=hour&list_type=skyrocket  快速飙升
 * https://dq.10jqka.com.cn/fuyao/hot_list_data/out/hot_list/v1/plate?type=concept 热门概念
 * <p>
 * https://dq.10jqka.com.cn/fuyao/hot_list_data/out/hot_list/v1/plate?type=industry 热门行业
 */
@Service
public class PopularStockService {
    public static void main(String[] args) {
        getPopularConceptList("normal");
    }

    public static List<FlushPopularStock.DataDTO.StockListDTO> getPopularStockList(String type) {

        String url = "https://dq.10jqka.com.cn/fuyao/hot_list_data/out/hot_list/v1/stock?stock_type=a&type=hour&list_type=" + type;
        String result = HttpUtil.get(url);
        FlushPopularStock bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), FlushPopularStock.class);
        List<FlushPopularStock.DataDTO.StockListDTO> infoList = bean.getData().getStockList();
        return infoList;
    }

    public static List<IndustryPopularStock.DataDTO.PlateListDTO> getPopularConceptList(String type) {

        String url = "https://dq.10jqka.com.cn/fuyao/hot_list_data/out/hot_list/v1/plate?type=" + type;
        String result = HttpUtil.get(url);
        IndustryPopularStock bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), IndustryPopularStock.class);
        List<IndustryPopularStock.DataDTO.PlateListDTO> infoList = bean.getData().getPlateList();
        return infoList;
    }
    public static List<DongCaiPopularStock.DataDTO.DiffDTO> getDongCaiPopularStocktList(String type) {

        String url = "https://push2.eastmoney.com/api/qt/ulist.np/get" ;
        Map<String, Object> params = new HashMap<>();
        params.put("fltt", "2");
        params.put("np", "3");
        params.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
        params.put("invt", 2);
        params.put("secids", "0.000966,0.002261,1.600744,0.002251,1.600505,1.600726,0.000601,0.002354,1.600101,0.300059,0.002229,0.300040,1.600644,1.601956,0.002129,0.002537,0.002475,0.000722,0.002163,0.002298");
        params.put("fields", "f1,f2,f3,f4,f12,f13,f14,f152,f15,f16");
        String result = HttpUtil.get(url,params);
        DongCaiPopularStock bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), DongCaiPopularStock.class);
        List<DongCaiPopularStock.DataDTO.DiffDTO> infoList = bean.getData().getDiff();
        return infoList;
    }

}
