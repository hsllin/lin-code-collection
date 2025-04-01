package com.lin.bean;

public class Constants {
    public static String newsCss = "<!DOCTYPE html>\n" +
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

    public static String newsContent1 = " <div class=\"news-item\">\n" +
            "            <img src=\"https://via.placeholder.com/150x100\" alt=\"新闻图片\" class=\"news-image\">\n" +
            "            <div class=\"news-content\">\n" +
            "                <div class=\"news-title\">\n" +
            "                    <a href=\"#";
    public static String newsContent2 = "</a>\n" +
            "                </div>\n" +
            "                <div class=\"news-meta\">\n" +
            "                    <span>发布时间：";
    public static String newsConetent3 = "</span>\n" +
            "</div>\n" +
            "            </div>\n" +
            "        </div>";
}
