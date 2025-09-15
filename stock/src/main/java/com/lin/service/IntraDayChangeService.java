package com.lin.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.intradaychange.BuyChangeBean;
import com.lin.bean.intradaychange.IntradayChange;
import com.lin.bean.stockstudy.StockStudy;
import com.lin.service.impl.StockStudyServiceImpl;
import com.lin.util.CommonUtils;
import com.lin.util.WechatMessageUtils;
import com.ugrong.framework.redis.repository.cache.IStringRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * 接口页面：https://quote.eastmoney.com/changes/?from=center
 */
@Service
public class IntraDayChangeService {
    @Autowired
    IStringRedisRepository iStringRedisRepository;
    public static final String INCREASE = "8201";
    public static final String DECREASE = "8204";
    public static final String BUY = "8193";
    public static final String SELL = "8194";
    public static final String SIXTY_DAY = "8215";
    public static final String OPEN_LIMIT_DOWN = "32";

    @Autowired
    StockStudyService stockStudyService;

    public static void main(String[] args) {

//        getIntradayChangeList(INCREASE);
//        System.out.println("-----------------------------------------------------");
//        getIntradayChangeList(DECREASE);
//        getBuyChangeList(BUY);
    }

    /**
     * 东方财富快速涨幅
     *
     * @param type
     * @return
     */
    public List<IntradayChange.DataBean.AllstockBean> getIntradayChangeList(String type, String checkedData) {
        String url = "https://push2ex.eastmoney.com/getAllStockChanges";

//        20250121
        Map<String, Object> param = new HashMap<>();
//        param.put("cb", "jQuery1123006755088795545139_173727505367");
        param.put("type", type);
//        8204
        param.put("pageindex", "0");
        param.put("pagesize", "64");
        param.put("dpt", "wzchanges");
        param.put("_", System.currentTimeMillis());
        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");

        String result = HttpUtil.get(url, param);
        IntradayChange bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), IntradayChange.class);
        List<IntradayChange.DataBean.AllstockBean> allstockBeanList = bean.getData().getAllstock();
        HashMap<String, Integer> countMap = new HashMap<>();

        for (int i = 1; i < 200; i++) {
            param.put("pageindex", i);
            String tempResult = HttpUtil.get(url, param);
            IntradayChange tempBean = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), IntradayChange.class);
            if (null == tempBean.getData()) {
                break;
            }
            allstockBeanList.addAll(tempBean.getData().getAllstock());
        }

        Set<String> codeSet = new HashSet<>();
        Iterator<IntradayChange.DataBean.AllstockBean> allstockBeanIterator = allstockBeanList.iterator();

        List<String> filterNameList = Arrays.asList(checkedData.split(","));
        List<String> filterCodeList = CommonUtils.dealFilterCodeList(filterNameList);
        while (allstockBeanIterator.hasNext()) {
            IntradayChange.DataBean.AllstockBean allstockBean = allstockBeanIterator.next();
            countMap.put(allstockBean.getC(), countMap.get(allstockBean.getC()) == null ? allstockBean.getCount() : countMap.get(allstockBean.getC()) + 1);
//            if (codeSet.contains(allstockBean.getC()) || allstockBean.getC().startsWith("8") || allstockBean.getC().startsWith("3") || allstockBean.getC().startsWith("68") || allstockBean.getN().contains("ST")) {
//                allstockBeanIterator.remove();
//            }
            boolean isMatch = false;
            for (String prefix : filterCodeList) {
                if (allstockBean.getC().startsWith(prefix)) {
                    isMatch = true;
                    break;
                }
            }

            // 移除条件（满足任一条件即移除）：
            // 1. 代码已存在（重复）
            // 2. 是ST/*ST股票
            // 3. 不匹配 filterCodeList 中的任何前缀
            if (codeSet.contains(allstockBean.getC())
                    || allstockBean.getN().contains("ST")
                    || !isMatch) {  // 关键修改：!isMatch 表示不在过滤列表中
                allstockBeanIterator.remove();
            } else {
                // 仅保留的股票才加入codeSet（避免重复）
                codeSet.add(allstockBean.getC());
            }

//            codeSet.add(allstockBean.getC());
        }
        CollectionUtil.reverse(allstockBeanList);
        String message = "";
        String increase;
        StringBuilder txtBuilder = new StringBuilder();
        List<StockStudy> allStockList = stockStudyService.getAllStockList();
        Map<String, String> stockStudyMap = new HashMap<>();
        for (StockStudy stockStudy : allStockList) {
            stockStudyMap.put(stockStudy.getCode(), stockStudy.getIndustry() == null ? "" : stockStudy.getIndustry().replaceAll("-(.*)", ""));
        }
        for (IntradayChange.DataBean.AllstockBean allstockBean : allstockBeanList) {
            if (type.equals(INCREASE)) {
                message = "\033[34;4m";
                if (countMap.get(allstockBean.getC()) >= 3 || Double.valueOf(allstockBean.getI().substring(0, allstockBean.getI().indexOf(","))) > 0.035) {
                    message = "\033[31;4m";
                }
                message += "快速涨幅 次数：" + countMap.get(allstockBean.getC()) + " ";
            } else if (type.equals(DECREASE)) {
                message = "\033[32;4m";
                if (countMap.get(allstockBean.getC()) >= 3 || Double.valueOf(allstockBean.getI().substring(0, allstockBean.getI().indexOf(","))) < -0.035) {
                    message = "\033[35;4m";
                }
                message += "快速下跌 次数：" + countMap.get(allstockBean.getC()) + " ";
            }


            increase = allstockBean.getI().substring(0, allstockBean.getI().indexOf(","));
            double IncreasePersent = Double.parseDouble(increase) * 100;
            allstockBean.setTime(getTime(allstockBean.getTm()));
            allstockBean.setPercent(CommonUtils.formatPrice(IncreasePersent));
            allstockBean.setCount(countMap.get(allstockBean.getC()));
            allstockBean.setIndustry(stockStudyMap.get(allstockBean.getC()));
            String finalSys = message + allstockBean.getTime() + " " + allstockBean.getN() + "(" + allstockBean.getC() + ")" + "上涨幅度：" + CommonUtils.formatPrice(IncreasePersent) + "%" + "\033[0m" + "\r\n";
            System.out.println(finalSys);
            txtBuilder.append(finalSys);
        }
//        File file = new File("D:/intraDay.txt");
//        try {
//            FileWriter writer = new FileWriter(file);
//            writer.write(txtBuilder.toString());
//            writer.flush();
//            writer.close();
//        } catch (Exception e) {
//
//        }
        return allstockBeanList;

    }

    public static String getTime(int time) {
        int hour = time / 10000;
        int minute = time % 10000 / 100;
        int second = time % 100;
        String hourStr = hour > 9 ? hour + "" : "0" + hour;
        String minuteStr = minute > 9 ? minute + "" : "0" + minute;
        String secondStr = second > 9 ? second + "" : "0" + second;
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    /**
     * 同花顺大买盘与大卖盘
     *
     * @param type
     * @return
     */
    public List<BuyChangeBean.DataDTO.AllstockDTO> getBuyChangeList(String type, String checkedData) {
        String url = "https://push2ex.eastmoney.com/getAllStockChanges";

//        8193
        Map<String, Object> param = new HashMap<>();
//        param.put("cb", "jQuery1123006755088795545139_173727505367");
        param.put("type", type);
//        8204
        param.put("pageindex", "0");
        param.put("pagesize", "64");
        param.put("dpt", "wzchanges");
        param.put("_", System.currentTimeMillis());
        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");

        String result = HttpUtil.get(url, param);
        BuyChangeBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), BuyChangeBean.class);
        List<BuyChangeBean.DataDTO.AllstockDTO> allstockBeanList = bean.getData().getAllstock();
        HashMap<String, Integer> countMap = new HashMap<>();

        for (int i = 1; i < 200; i++) {
            param.put("pageindex", i);
            String tempResult = HttpUtil.get(url, param);
            BuyChangeBean tempBean = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), BuyChangeBean.class);
            if (null == tempBean.getData()) {
                break;
            }
            allstockBeanList.addAll(tempBean.getData().getAllstock());
        }

        Set<String> codeSet = new HashSet<>();
        Iterator<BuyChangeBean.DataDTO.AllstockDTO> allstockBeanIterator = allstockBeanList.iterator();
        List<String> filterNameList = Arrays.asList(checkedData.split(","));
        List<String> filterCodeList = CommonUtils.dealFilterCodeList(filterNameList);
        while (allstockBeanIterator.hasNext()) {
            BuyChangeBean.DataDTO.AllstockDTO allstockBean = allstockBeanIterator.next();
            countMap.put(allstockBean.getC(), countMap.get(allstockBean.getC()) == null ? allstockBean.getCount() : countMap.get(allstockBean.getC()) + 1);
//            if (codeSet.contains(allstockBean.getC()) || allstockBean.getC().startsWith("8") || allstockBean.getC().startsWith("3") || allstockBean.getC().startsWith("68") || allstockBean.getN().contains("ST")) {
//                allstockBeanIterator.remove();
//            }
            boolean isMatch = false;
            for (String prefix : filterCodeList) {
                if (allstockBean.getC().startsWith(prefix)) {
                    isMatch = true;
                    break;
                }
            }

            // 移除条件（满足任一条件即移除）：
            // 1. 代码已存在（重复）
            // 2. 是ST/*ST股票
            // 3. 不匹配 filterCodeList 中的任何前缀
            if (codeSet.contains(allstockBean.getC())
                    || allstockBean.getN().contains("ST")
                    || !isMatch) {  // 关键修改：!isMatch 表示不在过滤列表中
                allstockBeanIterator.remove();
            } else {
                // 仅保留的股票才加入codeSet（避免重复）
                codeSet.add(allstockBean.getC());
            }

//            codeSet.add(allstockBean.getC());
        }
        CollectionUtil.reverse(allstockBeanList);
        String message = "";
        String buy = "";
        String percent = "";
        StringBuilder txtBuilder = new StringBuilder();
        List<StockStudy> allStockList = stockStudyService.getAllStockList();
        Map<String, String> stockStudyMap = new HashMap<>();
        for (StockStudy stockStudy : allStockList) {
            stockStudyMap.put(stockStudy.getCode(), stockStudy.getIndustry() == null ? "" : stockStudy.getIndustry().replaceAll("-(.*)", ""));
        }
        for (BuyChangeBean.DataDTO.AllstockDTO allstockBean : allstockBeanList) {
            buy = allstockBean.getI().substring(0, allstockBean.getI().indexOf(","));
            percent = getContentBetweenCommas(allstockBean.getI());
            double IncreasePersent = Double.parseDouble(percent) * 100;
            allstockBean.setPercent(CommonUtils.formatPrice(IncreasePersent));
            //多少手数
            double buyNum = CommonUtils.formatPrice(Double.parseDouble(buy) / 1000000);
            allstockBean.setTime(getTime(allstockBean.getTm()));
            allstockBean.setBuyNum(buyNum);
            allstockBean.setCount(countMap.get(allstockBean.getC()));
            allstockBean.setIndustry(stockStudyMap.get(allstockBean.getC()));
            String finalSys = message + allstockBean.getTime() + " " + allstockBean.getN() + "(" + allstockBean.getC() + ")" + "大买单：" + buyNum + "万手" + "\033[0m" + "\r\n";
            System.out.println(finalSys);
            txtBuilder.append(finalSys);
            //TODO:当手数大于3万手且间隔多少小时发消息提醒
            //WechatMessageUtils.sendMessageToWechat();
        }

        return allstockBeanList;

    }

    /**
     * 东方财富60日大幅上涨
     *
     * @param type
     * @return
     */
    public static List<IntradayChange.DataBean.AllstockBean> getSixtyDayList(String type) {
        String url = "https://push2ex.eastmoney.com/getAllStockChanges";

        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
//        8215
        param.put("pageindex", "0");
        param.put("pagesize", "64");
        param.put("dpt", "wzchanges");
        param.put("_", System.currentTimeMillis());
        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");

        String result = HttpUtil.get(url, param);
        IntradayChange bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), IntradayChange.class);
        List<IntradayChange.DataBean.AllstockBean> allstockBeanList = bean.getData().getAllstock();
        HashMap<String, Integer> countMap = new HashMap<>();

        for (int i = 1; i < 200; i++) {
            param.put("pageindex", i);
            String tempResult = HttpUtil.get(url, param);
            IntradayChange tempBean = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), IntradayChange.class);
            if (null == tempBean.getData()) {
                break;
            }
            allstockBeanList.addAll(tempBean.getData().getAllstock());
        }

        Set<String> codeSet = new HashSet<>();
        Iterator<IntradayChange.DataBean.AllstockBean> allstockBeanIterator = allstockBeanList.iterator();
        while (allstockBeanIterator.hasNext()) {
            IntradayChange.DataBean.AllstockBean allstockBean = allstockBeanIterator.next();
            countMap.put(allstockBean.getC(), countMap.get(allstockBean.getC()) == null ? allstockBean.getCount() : countMap.get(allstockBean.getC()) + 1);
            if (codeSet.contains(allstockBean.getC()) || allstockBean.getC().startsWith("8") || allstockBean.getC().startsWith("3") || allstockBean.getC().startsWith("68") || allstockBean.getN().contains("ST")) {
                allstockBeanIterator.remove();
            }

            codeSet.add(allstockBean.getC());
        }
        CollectionUtil.reverse(allstockBeanList);
        String message = "";
        String increase;
        StringBuilder txtBuilder = new StringBuilder();
        for (IntradayChange.DataBean.AllstockBean allstockBean : allstockBeanList) {

            increase = allstockBean.getI().substring(0, allstockBean.getI().indexOf(","));
            double IncreasePersent = Double.parseDouble(increase) * 100;
            allstockBean.setTime(getTime(allstockBean.getTm()));
            allstockBean.setPercent(CommonUtils.formatPrice(IncreasePersent));
            allstockBean.setCount(countMap.get(allstockBean.getC()));
            String finalSys = message + allstockBean.getTime() + " " + allstockBean.getN() + "(" + allstockBean.getC() + ")" + "上涨幅度：" + CommonUtils.formatPrice(IncreasePersent) + "%" + "\033[0m" + "\r\n";
            System.out.println(finalSys);
            txtBuilder.append(finalSys);
        }
        return allstockBeanList;

    }

    /**
     * 东方财富60日大幅上涨
     *
     * @param type
     * @return
     */
    public static List<IntradayChange.DataBean.AllstockBean> openLimitDown(String type) {
        String url = "https://push2ex.eastmoney.com/getAllStockChanges";

        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
//        8215
        param.put("pageindex", "0");
        param.put("pagesize", "64");
        param.put("dpt", "wzchanges");
        param.put("_", System.currentTimeMillis());
        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");

        String result = HttpUtil.get(url, param);
        IntradayChange bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), IntradayChange.class);
        List<IntradayChange.DataBean.AllstockBean> allstockBeanList = bean.getData().getAllstock();
        if (null == allstockBeanList || CollectionUtil.isEmpty(allstockBeanList)) {
            return new ArrayList<>();
        }
        HashMap<String, Integer> countMap = new HashMap<>();

        for (int i = 1; i < 200; i++) {
            param.put("pageindex", i);
            String tempResult = HttpUtil.get(url, param);
            IntradayChange tempBean = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), IntradayChange.class);
            if (null == tempBean.getData()) {
                break;
            }
            allstockBeanList.addAll(tempBean.getData().getAllstock());
        }

        Set<String> codeSet = new HashSet<>();
        Iterator<IntradayChange.DataBean.AllstockBean> allstockBeanIterator = allstockBeanList.iterator();
        while (allstockBeanIterator.hasNext()) {
            IntradayChange.DataBean.AllstockBean allstockBean = allstockBeanIterator.next();
            countMap.put(allstockBean.getC(), countMap.get(allstockBean.getC()) == null ? allstockBean.getCount() : countMap.get(allstockBean.getC()) + 1);
            if (codeSet.contains(allstockBean.getC()) || allstockBean.getC().startsWith("8") || allstockBean.getC().startsWith("3") || allstockBean.getC().startsWith("68") || allstockBean.getN().contains("ST")) {
                allstockBeanIterator.remove();
            }

            codeSet.add(allstockBean.getC());
        }
        CollectionUtil.reverse(allstockBeanList);
        String message = "";
        String increase;
        StringBuilder txtBuilder = new StringBuilder();
        for (IntradayChange.DataBean.AllstockBean allstockBean : allstockBeanList) {

            increase = allstockBean.getI().substring(0, allstockBean.getI().indexOf(","));
            double IncreasePersent = Double.parseDouble(increase);
            allstockBean.setTime(getTime(allstockBean.getTm()));
            allstockBean.setPercent(CommonUtils.formatPrice(IncreasePersent));
            allstockBean.setCount(countMap.get(allstockBean.getC()));
            String finalSys = message + allstockBean.getTime() + " " + allstockBean.getN() + "(" + allstockBean.getC() + ")" + "上涨幅度：" + CommonUtils.formatPrice(IncreasePersent) + "%" + "\033[0m" + "\r\n";
            System.out.println(finalSys);
            txtBuilder.append(finalSys);
        }
        return allstockBeanList;

    }


    public static String getContentBetweenCommas(String input) {
        // 查找第一个逗号位置
        int firstIndex = input.indexOf(',');
        if (firstIndex == -1) return "逗号不足";

        // 查找第二个逗号位置
        int secondIndex = input.indexOf(',', firstIndex + 1);
        if (secondIndex == -1) return "逗号不足";

        // 查找第三个逗号位置
        int thirdIndex = input.indexOf(',', secondIndex + 1);
        if (thirdIndex == -1) return "逗号不足";

        return input.substring(secondIndex + 1, thirdIndex);
    }


}
