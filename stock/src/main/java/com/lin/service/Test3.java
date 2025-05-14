package com.lin.service;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-04-30 11:31
 */
public class Test3 {
    public static void main(String[] args) {
        // 设置 ChromeDriver 路径（替换为本地路径）
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");

        // 配置 ChromeOptions，绕过反爬检测
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless"); // 无头模式，可选
        options.addArguments("--disable-blink-features=AutomationControlled"); // 隐藏自动化标志
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        options.addArguments("--disable-gpu");
//        options.addArguments("--no-sandbox");

        // 初始化 WebDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // 访问目标网页
            driver.get("https://q.10jqka.com.cn/gn/detail/code/309182/");

            // 隐藏 webdriver 属性
            ((JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

            // 等待页面加载
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5).toSeconds());
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cate_items table")));

            // 存储所有股票数据
            List<String> allStocks = new ArrayList<>();

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
                    allStocks.add(stockName + " (" + stockCode + ")");
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
            for (String stock : allStocks) {
                System.out.println(stock);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭浏览器
            driver.quit();
        }
    }
}