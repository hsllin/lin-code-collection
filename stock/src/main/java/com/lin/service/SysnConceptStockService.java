package com.lin.service;

import cn.hutool.core.util.IdUtil;
import com.lin.bean.stockknow.Stock;
import com.lin.bean.stockknow.StockMarketConcept;
import com.lin.bean.stockknow.StockThsGnInfo;
import com.lin.bean.stockknow.StockConcept;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.lin.util.RandomIPGenerator.generateRandomIPs;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-04-29 15:18
 */
@Slf4j
@Service
public class SysnConceptStockService {
    @Autowired
    StockConceptService stockConceptService;
    @Autowired
    StockService stockService;
    @Autowired
    StockMarketConceptService stockMarketConceptService;

    /**
     * 阻塞队列
     */
    private ArrayBlockingQueue<StockConcept> arrayBlockingQueue = new ArrayBlockingQueue<>(1000);

    private static final String[] PROXY_LIST = {
            "http://123.45.67.89:8080",
            "http://98.76.54.32:3128",
            "http://45.67.89.12:8080",
            "http://123.45.67.88:8080",
            "http://98.76.54.34:3128",
            "http://45.67.89.11:8080",
            "http://123.45.67.83:8080",
            "http://98.76.54.35:3128",
            "http://45.67.89.18:8080",
            // 添加更多代理 IP
    };

    // 随机选择代理
    private static String getRandomProxy() {
        Random random = new Random();
//        return PROXY_LIST[random.nextInt(PROXY_LIST.length)];
        List<String> ips = generateRandomIPs(10, true);
        return ips.get(random.nextInt(ips.size()));
    }

    public static void main(String[] args) {
        System.out.println(getRandomProxy());
    }

    public boolean sysnData() {
        List<StockConcept> conceptList = stockConceptService.getAllStockConceptList();
        putAllArrayBlockingQueue(conceptList);
        ConsumeCrawlerGnDetailData(1);
        return true;
    }


    public void putAllArrayBlockingQueue(List<StockConcept> list) {
        if (!CollectionUtils.isEmpty(list)) {
            arrayBlockingQueue.addAll(list);
        }
    }


    public void ConsumeCrawlerGnDetailData(int threadNumber) {
        for (int i = 0; i < threadNumber; ++i) {
            log.info("开启线程第[{}]个消费", i);
            new Thread(new crawlerGnDataThread()).start();
        }
        log.info("一共开启线程[{}]个消费", threadNumber);
    }

    class crawlerGnDataThread implements Runnable {


        public void run() {
            try {
                while (true) {
                    // 设置 ChromeDriver 路径（替换为本地路径）
                    System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
                    StockConcept stockConcept = arrayBlockingQueue.take();
                    // 配置 ChromeOptions，绕过反爬检测
                    ChromeOptions options = new ChromeOptions();
                    // 设置代理
                    Proxy proxy = new Proxy();
                    String proxyAddress = getRandomProxy();
                    proxy.setHttpProxy(proxyAddress);
                    options.setProxy(proxy);
//        options.addArguments("--headless"); // 无头模式，可选
                    options.addArguments("--disable-blink-features=AutomationControlled"); // 隐藏自动化标志
                    options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--no-sandbox");

                    // 初始化 WebDriver
                    WebDriver driver = new ChromeDriver(options);

                    try {
                        // 访问目标网页
                        driver.get("https://q.10jqka.com.cn/gn/detail/code/" + stockConcept.getId() + "/");

                        // 隐藏 webdriver 属性
                        ((JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

                        // 等待页面加载
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5).toSeconds());
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cate_items table")));

                        // 存储所有股票数据
                        List<Stock> allStocks = new ArrayList<>();

                        // 循环提取数据并处理分页
                        boolean hasNextPage = true;
                        while (hasNextPage) {
                            // 提取当前页股票数据
                            WebElement table = driver.findElement(By.cssSelector(".m-pager-box table"));
                            List<WebElement> rows = table.findElements(By.tagName("tr"));
                            for (int i = 1; i < rows.size(); i++) { // 跳过表头
                                WebElement row = rows.get(i);
                                List<WebElement> cells = row.findElements(By.tagName("td"));
                                String stockCode = cells.get(1).getText(); // 股票代码
                                String stockName = cells.get(2).getText(); // 股票名称
                                Stock stock = new Stock();
                                stock.setCode(stockCode);
                                stock.setName(stockName);
                                allStocks.add(stock);
                            }

                            // 检查并点击“下一页”
                            try {
                                WebElement nextPage = driver.findElement(By.linkText("下一页"));
                                if (nextPage.isEnabled() && !nextPage.getAttribute("class").contains("disabled")) {
                                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPage); // 使用 JS 点击
                                    // 等待新页面加载
                                    wait.until(ExpectedConditions.stalenessOf(table));
                                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".m-pager-box table")));
                                    // 随机延迟，模拟人类行为
                                    Thread.sleep((long) (Math.random() * 2000 + 1000)); // 1-3 秒
                                } else {
                                    hasNextPage = false;
                                }
                            } catch (Exception e) {
                                hasNextPage = false; // 无下一页或出错
                            }
                        }

                        // 输出所有股票数据
                        System.out.println("概念个股总数：" + allStocks.size());
                        System.out.println("所有概念个股数据：");
                        for (Stock stock : allStocks) {
                            stockService.addOrEditStock(stock);

                            StockMarketConcept stockMarketConcept = new StockMarketConcept();
                            stockMarketConcept.setId(IdUtil.simpleUUID());
                            stockMarketConcept.setCode(stock.getCode());
                            stockMarketConcept.setConceptId(stockConcept.getId());
                            stockMarketConceptService.addOrEditStock(stockMarketConcept);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // 关闭浏览器
                        driver.quit();
                    }
                }
            } catch (Exception e) {
                log.error("阻塞队列出现循环出现异常:", e);
            }
        }
    }

    public void parseHtmlAndInsertData(String html, String gnName, String crawlerDateStr, StockConcept stockConcept) {
        Document document = Jsoup.parse(html);
//        Element boardElement = document.getElementsByClass("board-hq").get(0);
//        String gnCode = boardElement.getElementsByTag("h3").get(0).getElementsByTag("span").get(0).text();

        Element table = document.getElementsByClass("m-pager-table").get(0);
        Element tBody = table.getElementsByTag("tbody").get(0);
        Elements trs = tBody.getElementsByTag("tr");
        for (Element tr : trs) {
            try {
                Elements tds = tr.getElementsByTag("td");
                String stockCode = tds.get(1).text();
                String stockName = tds.get(2).text();
                BigDecimal stockPrice = parseValueToBigDecimal(tds.get(3).text());
                BigDecimal stockChange = parseValueToBigDecimal(tds.get(4).text());
                BigDecimal stockChangePrice = parseValueToBigDecimal(tds.get(5).text());
                BigDecimal stockChangeSpeed = parseValueToBigDecimal(tds.get(6).text());
                BigDecimal stockHandoverScale = parseValueToBigDecimal(tds.get(7).text());
                BigDecimal stockLiangBi = parseValueToBigDecimal(tds.get(8).text());
                BigDecimal stockAmplitude = parseValueToBigDecimal(tds.get(9).text());
                BigDecimal stockDealAmount = parseValueToBigDecimal(tds.get(10).text());
                BigDecimal stockFlowStockNumber = parseValueToBigDecimal(tds.get(11).text());
                BigDecimal stockFlowMakertValue = parseValueToBigDecimal(tds.get(12).text());
                BigDecimal stockMarketTtm = parseValueToBigDecimal(tds.get(13).text());
                Stock stock = new Stock();
                stock.setCode(stockCode);
                stock.setName(stockName);
//                stockService.addOrEditStock(stock);

                StockMarketConcept stockMarketConcept = new StockMarketConcept();
                stockMarketConcept.setId(IdUtil.simpleUUID());
                stockMarketConcept.setCode(stockCode);
                stockMarketConcept.setConceptId(stockConcept.getId());
//                stockMarketConceptService.addOrEditStock(stockMarketConcept);

            } catch (Exception e) {
                log.error("插入同花顺概念板块数据出现异常:", e);
            }

        }
    }

    public BigDecimal parseValueToBigDecimal(String value) {
        if (StringUtils.isEmpty(value)) {
            return BigDecimal.ZERO;
        } else if ("--".equals(value)) {
            return BigDecimal.ZERO;
        } else if (value.endsWith("亿")) {
            return new BigDecimal(value.substring(0, value.length() - 1)).multiply(BigDecimal.ONE);
        }
        return new BigDecimal(value);
    }

    public boolean clicktoOneGnNextPage(WebDriver webDriver, String oneGnHtml, StockConcept concept, String crawlerDateStr) throws InterruptedException {
        // 是否包含下一页
        String pageNumber = includeNextPage(oneGnHtml);
        if (!StringUtils.isEmpty(pageNumber)) {
            WebElement nextPageElement = webDriver.findElement(By.linkText("下一页"));
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            nextPageElement.click();
            Thread.sleep(700);
            String nextPageHtml = webDriver.getPageSource();
            log.info("下一页：");
//            log.info(nextPageHtml);
            // TODO 解析并存储数据
            parseHtmlAndInsertData(nextPageHtml, concept.getName(), crawlerDateStr, concept);
            clicktoOneGnNextPage(webDriver, nextPageHtml, concept, crawlerDateStr);
        }
        return true;
    }

    public String includeNextPage(String html) {
        Document document = Jsoup.parse(html);
        List<Element> list = document.getElementsByTag("a");
        for (Element element : list) {
            String a = element.text();
            if ("下一页".equals(a)) {
                String pageNumber = element.attr("page");
                return pageNumber;
            }
        }
        return null;
    }
}