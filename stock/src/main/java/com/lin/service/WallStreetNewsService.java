package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.NewsBean;
import com.lin.util.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lin.bean.DateFormatEnum.HOUR_MIN_SECORD;


/**
 * 华尔街新闻
 * https://wallstreetcn.com/live/global
 */
@Service
public class WallStreetNewsService {
    static Logger logger = Logger.getLogger(WallStreetNewsService.class);
    static String html = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>新闻列表</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            background-color: #f9f9f9;\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        .news-list {\n" +
            "            max-width: 800px;\n" +
            "            margin: 20px auto;\n" +
            "            padding: 20px;\n" +
            "            background-color: #fff;\n" +
            "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
            "            border-radius: 8px;\n" +
            "        }\n" +
            "        .news-item {\n" +
            "            display: flex;\n" +
            "            margin-bottom: 20px;\n" +
            "            padding-bottom: 20px;\n" +
            "            border-bottom: 1px solid #eee;\n" +
            "        }\n" +
            "        .news-item:last-child {\n" +
            "            border-bottom: none;\n" +
            "            margin-bottom: 0;\n" +
            "            padding-bottom: 0;\n" +
            "        }\n" +
            "        .news-image {\n" +
            "            width: 150px;\n" +
            "            height: 100px;\n" +
            "            object-fit: cover;\n" +
            "            border-radius: 8px;\n" +
            "            margin-right: 20px;\n" +
            "        }\n" +
            "        .news-content {\n" +
            "            flex: 1;\n" +
            "        }\n" +
            "        .news-title {\n" +
            "            font-size: 18px;\n" +
            "            font-weight: bold;\n" +
            "            color: #333;\n" +
            "            margin-bottom: 10px;\n" +
            "        }\n" +
            "        .news-title a {\n" +
            "            color: inherit;\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "        .news-title a:hover {\n" +
            "            color: #007BFF;\n" +
            "        }\n" +
            "        .news-meta {\n" +
            "            font-size: 14px;\n" +
            "            color: #666;\n" +
            "            margin-bottom: 10px;\n" +
            "        }\n" +
            "        .news-meta span {\n" +
            "            margin-right: 15px;\n" +
            "        }\n" +
            "        .news-summary {\n" +
            "            font-size: 14px;\n" +
            "            color: #444;\n" +
            "            line-height: 1.5;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"news-list\">";

    public static void main(String[] args) {
        getNews();
    }

    public static List<NewsBean.DataBean.ItemsBean> getNews() {
        String url = "https://api-one-wscn.awtmt.com/apiv1/search/live";


//        20250121
        Map<String, Object> param = new HashMap<>();
        param.put("channel", "global-channel");
        param.put("limit", "100");
        param.put("score", "2");

        String result = HttpUtil.get(url, param);
        NewsBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), NewsBean.class);

        List<NewsBean.DataBean.ItemsBean> allList = bean.getData().getItems();
        StringBuilder message = new StringBuilder(128);
        String newsContent1 = " <div class=\"news-item\">\n" +
                "            <img src=\"https://via.placeholder.com/150x100\" alt=\"新闻图片\" class=\"news-image\">\n" +
                "            <div class=\"news-content\">\n" +
                "                <div class=\"news-title\">\n" +
                "                    <a href=\"#";
        String newsContent2 = "</a>\n" +
                "                </div>\n" +
                "                <div class=\"news-meta\">\n" +
                "                    <span>发布时间：";
        String newsConetent3 = "</span>\n" +
                "</div>\n" +
                "            </div>\n" +
                "        </div>";
        for (NewsBean.DataBean.ItemsBean itemsBean : allList) {
            itemsBean.setTime(DateUtils.changeTime(itemsBean.getDisplay_time(), DateFormatEnum.DEFAULT));
//            String info="\033[31;4m"+DateUtils.changeTime(itemsBean.getDisplay_time(),HOUR_MIN_SECORD) + itemsBean.getContent() + " " + itemsBean.getUri()+"\033[0m";
            String info = itemsBean.getTime() + itemsBean.getContent() + " " + itemsBean.getUri();
            message.append(newsContent1 + itemsBean.getUri() + "\">" + itemsBean.getContent() + newsContent2 + DateUtils.changeTime(itemsBean.getDisplay_time(), HOUR_MIN_SECORD) + newsConetent3);
            System.out.println(info);
        }
        try (FileWriter writer = new FileWriter("news.html")) {
            // 写入HTML内容
            writer.write(html + message.toString() + "</div>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allList;

    }

}
