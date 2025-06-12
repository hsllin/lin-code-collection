package com.lin.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.TradeDay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtils {
    public static void main(String[] args) {
        System.out.println(getTradeDay(0));
        System.out.println(getTradeDay(1));
        System.out.println(getTradeDay(2));

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
}
