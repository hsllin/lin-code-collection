package com.lin.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.intradaychange.IntradayChange;
import com.lin.util.CommonUtils;
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

    public static void main(String[] args) {

        getIntradayChangeList(INCREASE);
        System.out.println("-----------------------------------------------------");
        getIntradayChangeList(DECREASE);
    }

    public static List<IntradayChange.DataBean.AllstockBean> getIntradayChangeList(String type) {
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
        Set<String> codeSet = new HashSet<>();
        for (int i = 1; i < 200; i++) {
            param.put("pageindex", i);
            String tempResult = HttpUtil.get(url, param);
            IntradayChange tempBean = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), IntradayChange.class);
            if (null == tempBean.getData()) {
                break;
            }
            allstockBeanList.addAll(tempBean.getData().getAllstock());
        }
        CollectionUtil.reverse(allstockBeanList);
        Iterator<IntradayChange.DataBean.AllstockBean> allstockBeanIterator = allstockBeanList.iterator();
        while (allstockBeanIterator.hasNext()) {
            IntradayChange.DataBean.AllstockBean allstockBean = allstockBeanIterator.next();
            countMap.put(allstockBean.getC(), countMap.get(allstockBean.getC()) == null ? allstockBean.getCount() : countMap.get(allstockBean.getC()) + 1);
            if (codeSet.contains(allstockBean.getC()) || allstockBean.getC().startsWith("8") || allstockBean.getC().startsWith("3") || allstockBean.getC().startsWith("68")) {
                allstockBeanIterator.remove();
            }

            codeSet.add(allstockBean.getC());
        }
        String message = "";
        String Increase;
        StringBuilder txtBuilder = new StringBuilder();
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


            Increase = allstockBean.getI().substring(0, allstockBean.getI().indexOf(","));
            double IncreasePersent = Double.parseDouble(Increase) * 100;
            allstockBean.setTime(getTime(allstockBean.getTm()));
            allstockBean.setPercent(CommonUtils.formatPrice(IncreasePersent));
            allstockBean.setCount(countMap.get(allstockBean.getC()));
            String finalSys = message + allstockBean.getTime() + " " + allstockBean.getN() + "(" + allstockBean.getC() + ")" + "上涨幅度：" + CommonUtils.formatPrice(IncreasePersent) + "%" + "\033[0m" + "\r\n";
            System.out.println(finalSys);
            txtBuilder.append(finalSys);
        }
        File file = new File("D:/intraDay.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(txtBuilder.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
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


}
