package com.lin.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.TradeDay;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class CommonUtils {
    public static void main(String[] args) {
//        System.out.println(getTradeDay(0));
//        System.out.println(getTradeDay(1));
//        System.out.println(getTradeDay(2));
        System.out.println(getTradeDay(14));
    }

    /**
     * 签到率格式化
     *
     * @param totalPrice
     * @return
     */
    public static double formatPrice(double totalPrice) {
        BigDecimal bg = new BigDecimal(totalPrice).setScale(2, RoundingMode.UP);
        return bg.doubleValue();
    }

    /**
     * 签到率格式化
     *
     * @param totalPrice
     * @return
     */
    public static double formatStringPrice(String totalPrice) {
        if (totalPrice == null || StrUtil.isEmpty(totalPrice) || "null".equals(totalPrice)) {
            return 0;
        }
        BigDecimal bg = new BigDecimal(totalPrice).setScale(2, RoundingMode.UP);
        return bg.doubleValue();
    }

    /**
     * 获取今天index为0
     *
     * @param index
     * @return
     */
    public static String getTradeDay(Integer index) {
        String dateUrl = "https://data.10jqka.com.cn/dataapi/limit_up/trade_day";
        String today = DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue());
//        String today = "20250209";
        Map<String, Object> param = new HashMap<>();
        param.put("stock", "stock");
        param.put("date", today);
        param.put("next", "26");
        param.put("prev", "19");

        String result = HttpUtil.get(dateUrl, param);
        TradeDay bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), TradeDay.class);
        Map<Integer, List<Map<String, Object>>> dealData = new HashMap<>();
        List<String> listBeanList = bean.getData().getPrev_dates();
        if (0 == index) {
            if (bean.getData().getTrade_day()) {
                return today;
            }
            listBeanList.get(listBeanList.size() - 1);
        }
        if (bean.getData().getTrade_day()) {
            return listBeanList.get(listBeanList.size() - index);
        }

        return listBeanList.get(listBeanList.size() - index - 1);
    }

    /**
     * 获取前几个交易日
     *
     * @param index
     * @return
     */
    public static String getPreTradeDay(Integer index) {
        String dateUrl = "https://data.10jqka.com.cn/dataapi/limit_up/trade_day";
//        String today = DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue());
        String today = "20250208";
        Map<String, Object> param = new HashMap<>();
        param.put("stock", "stock");
        param.put("date", today);
        param.put("next", "26");
        param.put("prev", "19");

        String result = HttpUtil.get(dateUrl, param);
        TradeDay bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), TradeDay.class);
        Map<Integer, List<Map<String, Object>>> dealData = new HashMap<>();
        List<String> listBeanList = bean.getData().getPrev_dates();
        if (bean.getData().getTrade_day()) {
            return listBeanList.get(listBeanList.size() - index);
        }

        return listBeanList.get(listBeanList.size() - index - 1);
    }

    public static String formatBigNum(String input) {
        // 示例：将1.5E8转换为亿
        BigDecimal num = new BigDecimal(input);
        BigDecimal result = num.divide(new BigDecimal("100000000"));
        System.out.println(new DecimalFormat("#0.00").format(result) + "亿"); // 输出：1.50亿
        return new DecimalFormat("#0.00").format(result) + "亿";
    }

    public static String formatBigNumWithoutUnit(String input) {
        // 示例：将1.5E8转换为亿
        BigDecimal num = new BigDecimal(input);
        BigDecimal result = num.divide(new BigDecimal("100000000"));
        System.out.println(new DecimalFormat("#0.00").format(result) + "亿"); // 输出：1.50亿
        return new DecimalFormat("#0.00").format(result) + "亿";
    }

    /**
     * 生成一个不以0开头的6位随机数字
     *
     * @return 6位随机整数，范围在100000-999999之间
     */
    public static int generateRandomSixDigit() {
        Random random = new Random();
        // 确保第一位不是0（范围1-9）
        int firstDigit = random.nextInt(9) + 1;

        // 生成剩余的5位数字（范围0-9）
        int number = firstDigit * 100000; // 设置首位数字在正确位置

        for (int i = 4; i >= 0; i--) {
            int digit = random.nextInt(10); // 0-9
            number += digit * (int) Math.pow(10, i);
        }

        return number;
    }

    public static List<String> dealFilterCodeList(List<String> filterNameList) {
        List<String> resultList = new ArrayList<>();
        for (String name : filterNameList) {
            if ("创业板".equals(name)) {
                resultList.add("3");
            }
            if ("深沪".equals(name)) {
                resultList.add("60");
                resultList.add("00");
            }
            if ("北交所".equals(name)) {
                resultList.add("82");
                resultList.add("83");
                resultList.add("87");
            }
            if ("科创板".equals(name)) {
                resultList.add("688");
            }
        }
        return resultList;
    }
}
