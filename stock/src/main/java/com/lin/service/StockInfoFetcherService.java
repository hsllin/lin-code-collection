package com.lin.service;
import com.lin.bean.risefall.StockIntroduce;
import com.lin.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-19 9:06
 */
public class StockInfoFetcherService {
    private static final String BASE_URL = "http://basic.10jqka.com.cn/";

    public static void main(String[] args) {
        String stockCode = "301595";

        try {
            StockIntroduce sector = parseStockSector(stockCode);
            System.out.println("\n所属板块信息：\n" + sector);
        } catch (IOException | IllegalArgumentException | InterruptedException e) {
            handleException(e);
        }
    }

    public static StockIntroduce parseStockSector(String code) throws IOException, InterruptedException {
        String targetUrl = BASE_URL + code + "/company.html";
//        Document doc = JsoupUtil.safeGet(targetUrl);
        Document doc = Jsoup.connect(targetUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .referrer("http://q.10jqka.com.cn/")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .timeout(15000)
                .get();
        StockIntroduce stockIntroduce = new StockIntroduce();
        // 解析核心板块信息
        String industry = doc.select("strong:contains(所属申万行业) + span").text();
        String introduce = doc.select("strong:contains(公司简介) + p").text();
        stockIntroduce.setIntroduce(introduce);
        stockIntroduce.setIndustry(industry);
        System.out.println(industry + "\n" + introduce);
        return stockIntroduce;
    }

    private static String validateInput(String input) {
        if (!input.matches("\\d{6}")) {
            throw new IllegalArgumentException("股票代码格式错误");
        }
        return input;
    }

    private static void handleException(Exception e) {
        if (e instanceof IOException) {
            System.err.println("");
        } else {
            System.err.println(e.getMessage());
        }
    }
}