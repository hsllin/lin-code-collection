package com.lin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.IndustryAnalyzeBean;
import com.lin.bean.jfzt.InflowAndOutFlowBean;
import com.lin.bean.jfzt.InflowAndOutFlowRankBean;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.util.BeanFieldConverterUtil;
import com.lin.util.CommonUtils;
import com.lin.util.PhoUtil;
import com.lin.util.WencaiUtils;
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
public class InAndOutFlowService {
    private static final List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    );

    public static void main(String[] args) throws ScriptException, IOException {
//        getIncreaseData();
//        getDecreaseData();
//        test();
//        getInflowAndOutFlowRankData();
        getInflowAndOutFlowListData();
        getInflowAndOutFlowRankData();
    }

    public static List<InflowAndOutFlowBean.DataDTO.ListDTO> getInflowAndOutFlowListData() {

        String url = "https://api-hq.chongnengjihua.com/sector/api/1/pc/plate/block/sort/fund/flow/list?pageNum=1&pageSize=20&sortField=pure_down&prodType=GN";
        Map<String, Object> param = new HashMap<>();

        param.put("pageNum", "1");
        param.put("pageSize", "20");
        param.put("sortField", "pure_down");
        param.put("prodType", "GN");

        String result = HttpUtil.createGet(url).body(JSONUtil.toJsonStr(param)).execute().body();
        InflowAndOutFlowBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), InflowAndOutFlowBean.class);

        return bean.getData().getList();
    }

    public static List<InflowAndOutFlowRankBean.DataDTO.ListDTO> getInflowAndOutFlowRankData() {

        String url = "https://api-hq.chongnengjihua.com/capital/api/1/stock/flow/period/ranking?code=HSA&period=one&sortField=mainNetTurnover&sortType=down";
        Map<String, Object> param = new HashMap<>();
        param.put("code", "HSA");
        param.put("period", "one");
        param.put("sortField", "mainNetTurnover");
        param.put("sortType", "down");
        param.put("pageNum", "1");
        param.put("pageSize", "50");

        String result = HttpUtil
                .createGet(url)
//                .header("signature", "f3d4b02d7db4205a871295238eb281cd")
//                .header("timestamp", "1750950230635")
//                .header("referer", "https://stock.9fzt.com/")
                .body(JSONUtil.toJsonStr(param))
                .execute().body();
        InflowAndOutFlowRankBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), InflowAndOutFlowRankBean.class);

        return bean.getData().getList();
    }

    public static String getSignature() {

        String url = "https://gateway.chongnengjihua.com/rjhy-official-business/user/api/official/1/9fztgw/navigation/bar/column/list/get";
        Map<String, Object> param = new HashMap<>();
        param.put("code", "HSA");
        param.put("period", "one");
        param.put("sortField", "mainNetTurnover");
        param.put("sortType", "down");
        param.put("pageNum", "1");
        param.put("pageSize", "50");

        HttpResponse result = HttpUtil
                .createGet(url)
                .form(JSONUtil.toJsonStr(param))
                .execute();
// 获取指定Header的值（如Content-Type）
        String contentType = result.header("signature");

// 获取重定向地址（如Location）
        String redirectUrl = result.header("timestamp");
        return "";
    }

}