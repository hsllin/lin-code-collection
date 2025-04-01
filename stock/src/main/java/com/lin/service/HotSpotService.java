package com.lin.service;

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
        getNews();
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
}
