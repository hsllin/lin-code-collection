package com.lin.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.Constants;
import com.lin.bean.HotSpotBean;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 来源：https://kuaixun.eastmoney.com/yw.html
 */
@Service
public class HotSpotService {
    public static void main(String[] args) {
//        getNews();
        test();
    }

    public static List<HotSpotBean.DataBean.FastNewsListBean> getNews() {
        String url = "https://np-weblist.eastmoney.com/comm/web/getFastNewsList";


//        20250121
        Map<String, Object> param = new HashMap<>();
        param.put("client", "web");
        param.put("req_trace", System.currentTimeMillis());
//        param.put("sortStart",DateUtils.getDateTime(DateUtils.getYesterday(DateFormatEnum.DEFAULT),DateFormatEnum.DEFAULT) );
//        param.put("sortStart","1738575035032411");
//       pageSize=20&req_trace=1738596441677&_=1738596441678

        param.put("sortEnd", "");
        param.put("biz", "web_724");
        param.put("fastColumn", "101");
        param.put("pageSize", "100");
        param.put("client_source", "web");
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        HotSpotBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), HotSpotBean.class);

        List<HotSpotBean.DataBean.FastNewsListBean> allList = bean.getData().getFastNewsList();
        StringBuilder message = new StringBuilder(128);
        for (HotSpotBean.DataBean.FastNewsListBean resultBean : allList) {
//            String info="\033[31;4m"+DateUtils.changeTime(itemsBean.getDisplay_time(),HOUR_MIN_SECORD) + itemsBean.getContent() + " " + itemsBean.getUri()+"\033[0m";
            String info = resultBean.getShowTime() + resultBean.getSummary() + " " + "https://finance.eastmoney.com/a/" + resultBean.getCode() + ".html";
            message.append(Constants.newsContent1 + "https://finance.eastmoney.com/a/" + resultBean.getCode() + ".html" + "\">" + resultBean.getSummary() + Constants.newsContent2 + resultBean.getShowTime() + Constants.newsConetent3);
            resultBean.setUrl("https://finance.eastmoney.com/a/" + resultBean.getCode() + ".html");
            System.out.println(info);
        }
        try (FileWriter writer = new FileWriter("E:/stockOutput/hotspot.html")) {
            // 写入HTML内容
            writer.write(Constants.newsCss + message.toString() + "</div>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allList;

    }

    public static void test() {
        String url = "http://page.tdx.com.cn:7615/TQLEX?Entry=HQServ.CombHQ";
        Map<String,Object> params = new HashMap<>();
        params.put("WantCol", "[\"CLOSE\",\"NOW\"]");
        params.put("Secu", "[{\"Code\":880201,\"Setcode\":1},{\"Code\":880202,\"Setcode\":1},{\"Code\":880203,\"Setcode\":1},{\"Code\":880204,\"Setcode\":1},{\"Code\":880205,\"Setcode\":1},{\"Code\":880206,\"Setcode\":1},{\"Code\":880207,\"Setcode\":1},{\"Code\":880208,\"Setcode\":1},{\"Code\":880209,\"Setcode\":1},{\"Code\":880210,\"Setcode\":1},{\"Code\":880211,\"Setcode\":1},{\"Code\":880212,\"Setcode\":1},{\"Code\":880213,\"Setcode\":1},{\"Code\":880214,\"Setcode\":1},{\"Code\":880215,\"Setcode\":1},{\"Code\":880216,\"Setcode\":1},{\"Code\":880217,\"Setcode\":1},{\"Code\":880218,\"Setcode\":1},{\"Code\":880219,\"Setcode\":1},{\"Code\":880220,\"Setcode\":1},{\"Code\":880221,\"Setcode\":1},{\"Code\":880222,\"Setcode\":1},{\"Code\":880223,\"Setcode\":1},{\"Code\":880224,\"Setcode\":1},{\"Code\":880225,\"Setcode\":1},{\"Code\":880226,\"Setcode\":1},{\"Code\":880227,\"Setcode\":1},{\"Code\":880228,\"Setcode\":1},{\"Code\":880229,\"Setcode\":1},{\"Code\":880230,\"Setcode\":1},{\"Code\":880231,\"Setcode\":1},{\"Code\":880232,\"Setcode\":1},{\"Code\":880501,\"Setcode\":1},{\"Code\":880502,\"Setcode\":1},{\"Code\":880505,\"Setcode\":1},{\"Code\":880506,\"Setcode\":1},{\"Code\":880507,\"Setcode\":1},{\"Code\":880513,\"Setcode\":1},{\"Code\":880515,\"Setcode\":1},{\"Code\":880516,\"Setcode\":1},{\"Code\":880519,\"Setcode\":1},{\"Code\":880520,\"Setcode\":1},{\"Code\":880521,\"Setcode\":1},{\"Code\":880522,\"Setcode\":1},{\"Code\":880523,\"Setcode\":1},{\"Code\":880524,\"Setcode\":1},{\"Code\":880525,\"Setcode\":1},{\"Code\":880526,\"Setcode\":1},{\"Code\":880527,\"Setcode\":1},{\"Code\":880528,\"Setcode\":1},{\"Code\":880529,\"Setcode\":1},{\"Code\":880530,\"Setcode\":1},{\"Code\":880532,\"Setcode\":1},{\"Code\":880533,\"Setcode\":1},{\"Code\":880534,\"Setcode\":1},{\"Code\":880535,\"Setcode\":1},{\"Code\":880536,\"Setcode\":1},{\"Code\":880537,\"Setcode\":1},{\"Code\":880538,\"Setcode\":1},{\"Code\":880539,\"Setcode\":1}]");
        String result = HttpRequest.post(url)
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")// token鉴权
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "page.tdx.com.cn:7615")
                .header("origin", "https://www.iwencai.com")
                .header("pragma", "no-cache")
                .header("Content-Length","1716")
                .header("Cookie","ASPSessionID=; LST=00")
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko, Electron) Chrome/129.0.0.0 Safari/537.36")
                .header("X-Requested-With","XMLHttpRequest")
                .header("referer", "http://page.tdx.com.cn:7615/site/pcwebcall/html/pc_tcld_bkzqb.html?color=0")
                .body(JSONUtil.toJsonStr(params) )
                .execute().body();
        System.out.println(result);
    }
}
