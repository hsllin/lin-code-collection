package com.lin.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-16 18:00
 */
public class JsoupUtil {
    // 代理池（示例为免费代理，建议使用付费高匿代理）
    private static final List<String> PROXY_POOL = new ArrayList<>() {{
        add("183.236.232.160:8080");
        add("113.194.50.238:8080");
        add("121.13.252.61:41564");
    }};

    // 动态User-Agent池
    private static final String[] USER_AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_5 like Mac OS X) AppleWebKit/605.1.15"
    };
    public static Document safeGet(String url) throws IOException, InterruptedException {
        Random rand = new Random();

        // 1. 动态请求头配置
        Jsoup.connect(url)
                .userAgent(USER_AGENTS[rand.nextInt(USER_AGENTS.length)])
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Referer", "https://www.10jqka.com.cn/") // 模拟来源
                .ignoreHttpErrors(true)
                .ignoreContentType(true);

        // 2. 代理IP轮换（每3次请求切换IP）
//        if (requestCount % 3 == 0) {
        String proxy = PROXY_POOL.get(rand.nextInt(PROXY_POOL.size()));
        System.setProperty("http.proxyHost", proxy.split(":")[0]);
        System.setProperty("http.proxyPort", proxy.split(":")[1]);
//        }

        // 3. 随机延时控制（3-10秒）
//        TimeUnit.SECONDS.sleep(3 + rand.nextInt(7));

        // 4. 自动重试机制（最多3次）
        int retry = 0;
        while (retry < 3) {
            try {
                return Jsoup.connect(url).get();
            } catch (IOException e) {
                if (e.getMessage().contains("403")) {
                    System.out.println("触发反爬，更换代理重试...");
                    retry++;
                } else {
                    throw e;
                }
            }
        }
        throw new IOException("重试失败");
    }
}