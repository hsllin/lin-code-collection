package com.lin.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.pluginking.RankIncreaseData;
import com.lin.config.DataPathConfig;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-07-30 11:29
 */
@Service
public class PluginKingService {
    
    @Autowired
    private DataPathConfig dataPathConfig;
    
    public static void main(String[] args) {
//        getRangeIncreaseBoardData("1", "1");
//        downLoadRangeIncreaseStockData("1", "1");
//        testData();
        getStockThemeChance();
    }

    public static String getLiveStreamingData() {
        String url = "https://apphq.longhuvip.com/w1/api/index.php?a=ZhiBoContent&apiv=w21&c=ConceptionPoint&_=" + System.currentTimeMillis();


//        20250121
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        return result;
    }

    /**
     * 获取精选板块
     *
     * @return
     */
    public static String getFeaturedBoards() {
        String url = "https://apphq.longhuvip.com/w1/api/index.php?Order=1&a=RealRankingInfo&st=30&apiv=w21&Type=1&c=ZhiShuRanking&PhoneOSNew=1&ZSType=7&&_=" + System.currentTimeMillis();
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        return result;
    }

    /**
     * 获取精选板块
     *
     * @return
     */
    public static String getFeaturedBoardsData(String boardId) {
        String url = "https://apphq.longhuvip.com/w1/api/index.php?Order=1&st=30&a=ZhiShuStockList_W8&c=ZhiShuRanking&PhoneOSNew=1&old=1&DeviceID=6575961a-b25b-3831-aa2b-4a373b51b256&VerSion=5.12.0.0&IsZZ=0&Index=0&apiv=w21&Type=6&PlateID=" + boardId + "&_=" + System.currentTimeMillis();
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        return result;
    }

    /**
     * 获取区间涨幅板块数据
     *
     * @return
     */
    public static String getRangeIncreaseBoardData(String days, String sort) {
        String currentTray = CommonUtils.getTradeDay(0);
        String startTray = CommonUtils.getTradeDay(Integer.parseInt(days) - 1);
        currentTray = DateUtils.transferFormatTime(currentTray, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE);
        startTray = DateUtils.transferFormatTime(startTray, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE);
        String url = "https://apphq.longhuvip.com/w1/api/index.php?Order=" + sort + "&a=GetInterviewsByDateZS&st=30&c=StockLineData&DEnd=" + currentTray + "&Index=0&DStart=" + startTray + "&apiv=w31&Type=1&_=" + System.currentTimeMillis();
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        return result;
    }

    /**
     * 获取区间涨幅板块数据个股数据
     *
     * @return
     */
    public static String getRangeIncreaseStockData(String days, String sort) {
        String currentTray = CommonUtils.getTradeDay(0);
        String startTray = CommonUtils.getTradeDay(Integer.parseInt(days) - 1);
        currentTray = DateUtils.transferFormatTime(currentTray, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE);
        startTray = DateUtils.transferFormatTime(startTray, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE);
        String url = "https://apphq.longhuvip.com/w1/api/index.php?Order=" + sort + "&st=100&a=GetInterviewsByDateStock&c=StockLineData&DEnd=" + currentTray + "&Index=0&DStart=" + startTray + "&apiv=w31&Type=2&_=" + System.currentTimeMillis();
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        return result;
    }

    /**
     * 获取区间涨幅板块数据个股数据
     *
     * @return
     */
    public static String testData() {
        String url = "http://hot.icfqs.com:7615/TQLEX?Entry=CWServ.pcwebcall_yzfp_ltgg";

        // 直接构造 JSON 字符串（确保格式正确）
        String requestBody = "{\"Params\": [\"880904\", \"2025-08-04\"]}";

        // 发送请求
        String result = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .execute()
                .body();
        return result;
    }

    /**
     * 获取主题机会
     *
     * @return
     */
    public static String getStockThemeChance() {
        String url = "https://apparticle.longhuvip.com/w1/api/index.php?apiv=w28&c=ThemeNews&a=GetList&Type=-1&st=30&_=" + System.currentTimeMillis();
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        return result;
    }

    /**
     * 获取涨停原因分析
     *
     * @return
     */
    public static String getLimitUpAnalyze(String date) {
        String url = "https://api.zizizaizai.com/v2/api/review/uplimit/reason?date1=" + date;
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        return result;
    }



    /**
     * 获取区间涨幅板块数据个股数据
     *
     * @return
     */
    public String downLoadRangeIncreaseStockData(String days, String sort) {
        String currentTray = CommonUtils.getTradeDay(0);
        String startTray = CommonUtils.getTradeDay(Integer.parseInt(days) - 1);
        currentTray = DateUtils.transferFormatTime(currentTray, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE);
        startTray = DateUtils.transferFormatTime(startTray, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE);
        String url = "https://apphq.longhuvip.com/w1/api/index.php?Order=" + sort + "&st=100&a=GetInterviewsByDateStock&c=StockLineData&DEnd=" + currentTray + "&Index=0&DStart=" + startTray + "&apiv=w31&Type=2&_=" + System.currentTimeMillis();
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        result = decodeUnicode(result);
        RankIncreaseData jsonObject = JSONUtil.toBean(result, RankIncreaseData.class);
//        transferData();
//        decodeUnicode(result);
        try {
            FileWriter fw = dataPathConfig.getDataFileWriter("涨幅榜" + days + ".txt");

            BufferedWriter bw = new BufferedWriter(fw);

            for (List<String> dataList : jsonObject.getList()) {
                if (dataList.get(0).startsWith("30") || dataList.get(0).startsWith("688") || dataList.get(1).contains("ST")) {
                    continue;
                }
                bw.write(dataList.get(0) + " " + dataList.get(1) + " " + dataList.get(2));
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public static void transferData() {
        String jsonResponse = "{\"List\":[[\"300584\",\"\\u6d77\\u8fb0\\u836f\\u4e1a\",41.27,20.01,454010562,-272065309,181945253,49.6366,1100530307,2374060509,\"\\u521b\\u65b0\\u836f\\u3001\\u5e7d\\u95e8\\u87ba\\u6746\\u83cc\",0,\"\",1,155209127,0],...],\"Count\":5138,\"ttag\":0.0023610000000000575,\"errcode\":\"0\"}\n";

        try {
            // 解析JSON响应
            JSONObject root = new JSONObject(jsonResponse);

            // 验证接口返回状态
            if (!"0".equals(root.getString("errcode"))) {
                System.err.println("API返回错误: " + root.getString("errcode"));
                return;
            }

            // 提取股票数据列表
            JSONArray stockList = root.getJSONArray("List");
            List<Map<String, Object>> parsedStocks = new ArrayList<>();

            // 定义字段映射关系（根据常见股票数据结构）
            String[] fieldNames = {
                    "stockCode", "stockName", "currentPrice", "changePercent",
                    "volume", "mainNetFlow", "retailNetFlow", "turnoverRate",
                    "totalMarketCap", "circulatingMarketCap", "concepts",
                    "unknownFlag1", "fundType", "buySignal", "largeOrderFlow", "unknownFlag2"
            };

            // 遍历处理每只股票
            for (int i = 0; i < stockList.length(); i++) {
                JSONArray stockData = stockList.getJSONArray(i);
                Map<String, Object> stockMap = new HashMap<>();

                // 按字段映射关系提取数据
                for (int j = 0; j < fieldNames.length && j < stockData.length(); j++) {
                    Object value = stockData.get(j);
                    // 特殊处理中文编码字段
                    if (fieldNames[j].equals("stockName") || fieldNames[j].equals("concepts")) {
                        stockMap.put(fieldNames[j], value);
                    } else {
                        stockMap.put(fieldNames[j], value);
                    }
                }
                parsedStocks.add(stockMap);
            }

            // 示例：打印前2只股票信息
            System.out.println("成功解析股票数量: " + parsedStocks.size());
            System.out.println("\n首只股票详情:");
            Map<String, Object> firstStock = parsedStocks.get(0);
            System.out.println("代码: " + firstStock.get("stockCode"));
            System.out.println("名称: " + firstStock.get("stockName"));
            System.out.println("价格: " + firstStock.get("currentPrice"));
            System.out.println("涨跌幅: " + firstStock.get("changePercent") + "%");
            System.out.println("概念: " + firstStock.get("concepts"));

        } catch (Exception e) {
            System.err.println("JSON解析异常: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 递归解码JSON对象中的Unicode字符串
     */
    private static void decodeUnicodeInJson(JSONObject jsonObj) {
        for (String key : jsonObj.keySet()) {
            Object value = jsonObj.get(key);
            if (value instanceof String) {
                jsonObj.put(key, decodeUnicode((String) value));
            } else if (value instanceof JSONObject) {
                decodeUnicodeInJson((JSONObject) value);
            } else if (value instanceof JSONArray) {
                decodeUnicodeInArray((JSONArray) value);
            }
        }
    }

    /**
     * Unicode转中文核心方法
     */
    private static String decodeUnicode(String unicodeStr) {
        StringBuilder sb = new StringBuilder();
        char[] chars = unicodeStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\\' && i + 1 < chars.length && chars[i + 1] == 'u') {
                // 解析4位十六进制编码
                String hex = new String(new char[]{chars[i + 2], chars[i + 3], chars[i + 4], chars[i + 5]});
                sb.append((char) Integer.parseInt(hex, 16));
                i += 5;  // 跳过已处理的Unicode序列
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 递归解码JSON数组中的Unicode字符串
     */
    private static void decodeUnicodeInArray(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object item = jsonArray.get(i);
            if (item instanceof String) {
                jsonArray.put(i, decodeUnicode((String) item));
            } else if (item instanceof JSONObject) {
                decodeUnicodeInJson((JSONObject) item);
            } else if (item instanceof JSONArray) {
                decodeUnicodeInArray((JSONArray) item);
            }
        }
    }

    /**
     * 获取涨停原因分析
     *
     * @return
     */
    public static String getHotTheme() {
        String url = "http://hot.icfqs.com:7615/TQLEX?Entry=CWServ.pcwebcall_zttzk_zttzk";
        // 直接构造 JSON 字符串（确保格式正确）
        String requestBody = " {\"Params\":[\"0\",\"\",\"0\"]}";

        // 发送请求
        String result = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .execute()
                .body();
        return result;
    }
}