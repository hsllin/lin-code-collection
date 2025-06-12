package com.lin.service;

import com.lin.bean.YiDong;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-24 11:35
 */
@Service
public class FupanlaService {

    public static String getFupanData() {
        String result = "";
        try {
            // 创建 HTTP 客户端
            HttpClient client = HttpClient.newHttpClient();

            // 构建 HTTP 请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.fupanwang.com/fupanla/"))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 检查请求是否成功
            if (response.statusCode() == 200) {
                // 使用 Jsoup 解析 HTML
                Document doc = Jsoup.parse(response.body());

                // 定位包含异动数据的容器（请根据实际网页调整选择器）
                result = doc.select(".layui-tab-item").get(3).html(); // 示例选择器，需替换
            } else {
                System.out.println("请求失败，状态码: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        getFupanData();
    }
}