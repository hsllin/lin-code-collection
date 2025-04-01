package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.IndustryAnalyzeBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 产业投研
 * https://www.jiuyangongshe.com/industryChain
 */
@Service
public class IndustryAnalyzeService {
    public static void main(String[] args) {
        getIndustryAnalyzeData();
    }

    public static IndustryAnalyzeBean getIndustryAnalyzeData() {
        String url = "\n" +
                "https://app.jiuyangongshe.com/jystock-app/api/v1/industry/list";
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", "");
        param.put("start", "1");
        param.put("limit", "15");

        Map<String, String> header = new HashMap<>();
        header.put("token", "8");
        header.put("timestamp", String.valueOf(System.currentTimeMillis()));
        header.put("Cookie","SESSION=ZDJmNTZhOGUtYzgzMi00ZWE4LWIwNjQtNWVlMjJiMTRkNjFl; Hm_lvt_58aa18061df7855800f2a1b32d6da7f4=1738031392,1739779073; Hm_lpvt_58aa18061df7855800f2a1b32d6da7f4=1739784373");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36");
        header.put("platform","3");

        String result = HttpUtil.createPost(url).addHeaders(header).body(JSONUtil.toJsonStr(param)).execute().body();
        IndustryAnalyzeBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), IndustryAnalyzeBean.class);
        return bean;

    }
}
