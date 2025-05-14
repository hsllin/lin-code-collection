package com.lin.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bean.index.ConceptAndIndex;
import com.lin.bean.index.VolumeTrend;
import com.lin.bean.lianban.LianBanBean;
import com.lin.bean.lianban.LianBanNew;
import com.lin.bean.market.MarketBean;
import com.lin.bean.stockknow.StockConcept;
import com.lin.bean.stockknow.StockMarketConcept;
import com.lin.mapper.StockConceptMapper;
import com.lin.mapper.VolumeTrendMapper;
import com.lin.service.*;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import groovy.util.logging.Log;
import groovy.util.logging.Log4j;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Slf4j
public class StockConceptServiceImpl extends ServiceImpl<StockConceptMapper, StockConcept> implements StockConceptService {
    private static final String BASE_URL = "http://q.10jqka.com.cn/gn/";
    private static final String PAGE_URL_TEMPLATE = "http://q.10jqka.com.cn/gn/index/page/%d/";

    @Override
    public Page<StockConcept> getStockConceptList(String keyword, Integer startIndex, Integer pageSize) {
        QueryWrapper<StockConcept> wrapper = new QueryWrapper();
        wrapper.orderByDesc("update_date");
        Page<StockConcept> page = new Page<>(startIndex, pageSize);
        Page<StockConcept> list = getBaseMapper().selectPage(page, wrapper);
        return list;
    }

    public List<StockConcept> getAllStockConceptList() {
//        QueryWrapper<StockConcept> wrapper = new QueryWrapper();
//        wrapper.orderByDesc("update_date")
//                .notIn("");
        List<StockConcept> list = getBaseMapper().getAll();
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrEditStockConcept(StockConcept bean) {
        if (bean.getId() == null) {
            bean.setId(IdUtil.fastSimpleUUID());
        }
        UpdateWrapper<StockConcept> updateWrapper = new UpdateWrapper<StockConcept>().eq("id", bean.getId()).eq("name", bean.getName());
        return saveOrUpdate(bean, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStockConcept(String id) {
        return getBaseMapper().deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sysTonghuaShunConcept() {
        List<StockConcept> concepts = new ArrayList<>();
        try {
            concepts = fetchAllConcepts();
            concepts.addAll(fetchAddConcepts());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("concepts.size:" + concepts.size());
        return saveOrUpdateBatch(concepts);
    }

    /**
     * 从同花顺获取概念
     *
     * @return
     * @throws IOException
     */
    // 获取所有概念（包括分页）
    public static List<StockMarketConcept> getStockByConcept(StockConcept concept) throws IOException {
        String baseUrl = "https://q.10jqka.com.cn/gn/detail/code/" + concept.getId() + "/"; // 以“新能源”概念为例
        String pageUrl = "https://q.10jqka.com.cn/gn/detail/field/199112/order/desc/page/%d/ajax/1/code/" + concept.getId();
        List<StockMarketConcept> allConcepts = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        int page = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            String url = (page == 1) ? baseUrl : String.format(pageUrl, page);
            // 构造页面 URL
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + " for URL: " + url);
                }
                // 使用 Jsoup 发送 HTTP 请求并获取页面内容
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .timeout(5000)
                        .get();
                List<StockMarketConcept> tempConcepts = new ArrayList<>();
                // 定位个股表格（根据页面结构调整选择器）
                Elements stockRows = doc.select("table.m-table tbody tr");
                // 遍历表格行，提取个股信息

                for (Element row : stockRows) {
                    Elements columns = row.select("td");
                    if (columns.size() > 1) {
                        String stockCode = columns.get(1).text(); // 股票代码
                        String stockName = columns.get(2).text(); // 股票名称
                        System.out.println("股票代码: " + stockCode + ", 股票名称: " + stockName);
                        tempConcepts.add(new StockMarketConcept(IdUtil.fastSimpleUUID(), stockCode, concept.getId()));
                    }
                }
                if (tempConcepts.isEmpty()) {
                    hasNextPage = false; // 没有概念数据，停止
                } else {
                    allConcepts.addAll(tempConcepts);
                }

                // 检查是否有下一页
                Element nextPageElement = doc.selectFirst(".m-pager a:contains(下一页)");
                hasNextPage = (nextPageElement != null && !nextPageElement.hasClass("disabled"));
                page++;

                // 避免请求过快
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Thread interrupted", e);
            }
        }

        return allConcepts;
    }

    public static boolean getStockByConcept() {
        // 概念板块的 URL（需替换为具体概念的 URL）
        String url = "https://q.10jqka.com.cn/gn/detail/code/309093/"; // 以“新能源”概念为例
        try {
            // 使用 Jsoup 发送 HTTP 请求并获取页面内容
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(5000)
                    .get();

            // 定位个股表格（根据页面结构调整选择器）

            Elements stockRows = doc.select("table.m-table tbody tr");

            // 遍历表格行，提取个股信息
            for (Element row : stockRows) {
                Elements columns = row.select("td");
                if (columns.size() > 1) {
                    String stockCode = columns.get(1).text(); // 股票代码
                    String stockName = columns.get(2).text(); // 股票名称
                    System.out.println("股票代码: " + stockCode + ", 股票名称: " + stockName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("爬取失败，请检查网络或 URL 是否正确");
        }
        return true;
    }


    public static void main(String[] args) {
        StockConcept concept = new StockConcept();
        concept.setId("300008");
        try {
//            getStockByConcept(concept);
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getStockByConcept();
//        try {
//            List<StockConcept> concepts = fetchAllConcepts();
//            concepts.addAll(fetchAddConcepts());
//            for (StockConcept concept : concepts) {
//                System.out.println(concept);
//            }
//            getStockByConcept();
//            System.out.println("总计概念数量: " + concepts.size());
//        } catch (IOException e) {
//            System.err.println("获取概念列表失败: " + e.getMessage());
//        }
    }

    public static boolean test() {
        // 概念板块的基础 URL（以“新能源”概念为例）
        String baseUrl = "http://q.10jqka.com.cn/gn/detail/code/301558/";
        List<String[]> stocks = new ArrayList<>();

        try {
            int page = 1;
            boolean hasNextPage = true;

            while (hasNextPage) {
                // 构造分页 URL
                String pageUrl = page == 1 ? baseUrl : baseUrl + "index_" + page + ".shtml";
                System.out.println("正在爬取页面: " + pageUrl);

                // 发送 HTTP 请求并获取页面内容
                Document doc = Jsoup.connect(pageUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .timeout(5000)
                        .get();

                // 提取个股表格
                Elements stockRows = doc.select("table.m-table tbody tr");

                // 如果表格为空，说明没有更多数据
                if (stockRows.isEmpty()) {
                    hasNextPage = false;
                    break;
                }

                // 遍历表格行，提取个股信息
                for (Element row : stockRows) {
                    Elements columns = row.select("td");
                    if (columns.size() > 2) {
                        String stockCode = columns.get(1).text(); // 股票代码
                        String stockName = columns.get(2).text(); // 股票名称
                        stocks.add(new String[]{stockCode, stockName});
                    }
                }

                // 检查是否有下一页
                Elements nextPageLink = doc.select("a[title=下一页]");
                hasNextPage = !nextPageLink.isEmpty();
                page++;

                // 避免请求过快，添加延迟
                Thread.sleep(1000); // 1秒延迟
            }

            // 输出所有个股
            System.out.println("共获取 " + stocks.size() + " 只个股：");
            for (String[] stock : stocks) {
                System.out.println("股票代码: " + stock[0] + ", 股票名称: " + stock[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("爬取失败，请检查网络或 URL 是否正确");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
}

    /**
     * 从同花顺获取概念
     *
     * @return
     * @throws IOException
     */
    // 获取所有概念（包括分页）
    public static List<StockConcept> fetchAllConcepts() throws IOException {
        List<StockConcept> allConcepts = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        int page = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            // 构造页面 URL
            String url = (page == 1) ? BASE_URL : String.format(PAGE_URL_TEMPLATE, page);
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + " for URL: " + url);
                }
                String html = response.body().string();
                Document doc = Jsoup.parse(html);

                // 解析当前页的概念
                List<StockConcept> concepts = parseConceptList(doc);
                if (concepts.isEmpty()) {
                    hasNextPage = false; // 没有概念数据，停止
                } else {
                    allConcepts.addAll(concepts);
                }

                // 检查是否有下一页
                Element nextPageElement = doc.selectFirst(".pagebar a:contains(下一页)");
                hasNextPage = (nextPageElement != null && !nextPageElement.hasClass("disabled"));
                page++;

                // 避免请求过快
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Thread interrupted", e);
            }
        }

        return allConcepts;
    }

    // 获取新增的概念（包括分页）
    public static List<StockConcept> fetchAddConcepts() throws IOException {
        List<StockConcept> allConcepts = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        int page = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            // 构造页面 URL
            String url = (page == 1) ? BASE_URL : String.format(PAGE_URL_TEMPLATE, page);
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + " for URL: " + url);
                }
                String html = response.body().string();
                Document doc = Jsoup.parse(html);

                // 解析当前页的概念
                List<StockConcept> concepts = parseAddConceptList(doc);
                if (concepts.isEmpty()) {
                    hasNextPage = false; // 没有概念数据，停止
                } else {
                    allConcepts.addAll(concepts);
                }

                // 检查是否有下一页
                Element nextPageElement = doc.selectFirst(".m-pager a:contains(下一页)");
                hasNextPage = (nextPageElement != null && !nextPageElement.hasClass("disabled"));
                page++;

                // 避免请求过快
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Thread interrupted", e);
            }
        }

        return allConcepts;
    }

    // 解析单页的概念列表
    private static List<StockConcept> parseConceptList(Document doc) {
        List<StockConcept> concepts = new ArrayList<>();
        // 选择包含概念的元素（根据网页结构）
        Elements conceptElements = doc.select(".cate_items a");
        for (Element element : conceptElements) {
            String name = element.text(); // 概念名称
            String href = element.attr("href"); // 链接
            // 从 URL 中提取概念代码，例如 /gn/detail/code/301558/ -> 301558
            String code = href.substring(href.lastIndexOf('/') - 6, href.lastIndexOf('/'));
            concepts.add(new StockConcept(code, name, name));
        }
        return concepts;
    }

    // 解析单页的概念列表
    private static List<StockConcept> parseAddConceptList(Document doc) {

        // 2. 定位表格的tbody中的tr元素
        Elements rows = doc.select("table.m-pager-table tbody tr");

        // 3. 遍历每个tr提取数据
        List<StockConcept> concepts = new ArrayList<>();
        for (Element row : rows) {
            Elements tds = row.select("td");
            // 提取概念名称（第二个td中的a标签文本）
            String conceptName = tds.get(1).select("a").text();

            // 提取概念编码（从a标签的href中提取）
            String href = tds.get(1).select("a").attr("href");
            String code = extractCodeFromHref(href); // 自定义方法

            concepts.add(new StockConcept(code, conceptName, conceptName));

        }
        return concepts;
    }

    private static String extractCodeFromHref(String href) {
        String[] parts = href.split("/");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("code") && i + 1 < parts.length) {
                return parts[i + 1];
            }
        }
        return null; // 未找到编码
    }

}
