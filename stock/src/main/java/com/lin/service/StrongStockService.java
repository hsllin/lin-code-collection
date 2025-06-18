package com.lin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.LianBanNew;
import com.lin.bean.strongstock.StrongStockBean;
import com.lin.util.AdvancedConceptCloudUtil;
import com.lin.util.CommonUtils;
import com.lin.util.ConceptFrequencyAnalyzer;
import com.lin.util.DateUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

/**
 * 最强风口
 * https://data.10jqka.com.cn/datacenterph/limitup/limtupInfo.html#/bestTureye
 */
@Service
public class StrongStockService {
    public static void main(String[] args) {

        String today = DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue());
        String beforeDay = DateUtils.getBeforeDay(DateFormatEnum.DATE_WITH_OUT_LINE);
        String yesterDay = DateUtils.getYesterday(DateFormatEnum.DATE_WITH_OUT_LINE);
//        logLianBan(beforeDay);
//        logLianBan(yesterDay);
//        logLianBan(today);
//        getLimitUpPoolList("330324", "1");
        analyzeStrongStockWord();
    }

    public static List<StrongStockBean.DataBean> getStrongStockList(String date) {

        String url = "https://data.10jqka.com.cn/dataapi/limit_up/block_top";
        Map<String, Object> param = new HashMap<>();
        param.put("filter", "HS,GEM2STAR");
        param.put("date", date);
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        StrongStockBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), StrongStockBean.class);
        List<StrongStockBean.DataBean> infoList = bean.getData();
        for (StrongStockBean.DataBean dataBean : infoList) {
            dataBean.setChange(CommonUtils.formatPrice(dataBean.getChange()==null?0:dataBean.getChange()));
            for (StrongStockBean.DataBean.StockListBean stockListBean : dataBean.getStock_list()) {
                stockListBean.setChange_rate(CommonUtils.formatPrice(stockListBean.getChange_rate()==null?0:stockListBean.getChange_rate()));
                stockListBean.setFirstTime(DateUtils.changeTime(stockListBean.getFirst_limit_up_time(), DateFormatEnum.DEFAULT));
            }

        }
        return infoList;
    }

    public static List<LianBanNew> dealLianBanData(String date) {

        Set<String> codeSet = new HashSet<>();

        List<StrongStockBean.DataBean> list = getStrongStockList(date);
        HashMap<Integer, List<StrongStockBean.DataBean.StockListBean>> map = new HashMap<>();

        for (StrongStockBean.DataBean dataBean : list) {

            for (StrongStockBean.DataBean.StockListBean stockListBean : dataBean.getStock_list()) {
                if (codeSet.contains(stockListBean.getCode())) {
                    continue;
                }
                List<StrongStockBean.DataBean.StockListBean> tempList = map.get(stockListBean.getContinue_num());
                if (tempList == null) {
                    tempList = new ArrayList<>();
                }
                tempList.add(stockListBean);
                map.put(stockListBean.getContinue_num(), tempList);
                codeSet.add(stockListBean.getCode());
            }

        }
        List<LianBanNew> banNewList = new ArrayList<>();
        for (Map.Entry<Integer, List<StrongStockBean.DataBean.StockListBean>> entry : map.entrySet()) {


            List<StrongStockBean.DataBean.StockListBean> stockListBeanList = entry.getValue();
            LianBanNew lianBanNew = new LianBanNew();
            lianBanNew.setLimitNum(entry.getKey());
            List<LianBanNew.stock> stockList = new ArrayList<>();
            for (StrongStockBean.DataBean.StockListBean stockListBean : stockListBeanList) {
                LianBanNew.stock stock = new LianBanNew.stock();
                stock.setCode(stockListBean.getCode());
                stock.setName(stockListBean.getName());
                stock.setConcept(stockListBean.getReason_type());
                stock.setPrice(String.valueOf(stockListBean.getLatest()));
                stock.setFirstTime(stockListBean.getFirstTime());
                stockList.add(stock);

            }
            lianBanNew.setStockList(stockList);
            banNewList.add(lianBanNew);
        }
        Collections.reverse(banNewList);
        return banNewList;
    }

    public static void downloadStrongSrockData(String date) {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\最强风口.txt");
            FileWriter oneFw = new FileWriter("D:\\1stock\\一板.txt");
            FileWriter twoFw = new FileWriter("D:\\1stock\\二板以上.txt");

            BufferedWriter bw = new BufferedWriter(fw);
            BufferedWriter oneBw = new BufferedWriter(oneFw);
            BufferedWriter twoBw = new BufferedWriter(twoFw);

            Set<String> codeSet = new HashSet<>();

            List<StrongStockBean.DataBean> list = getStrongStockList(date);
            HashMap<Integer, List<StrongStockBean.DataBean.StockListBean>> map = new HashMap<>();
            StringBuilder words = new StringBuilder(1600);
            for (StrongStockBean.DataBean dataBean : list) {

                for (StrongStockBean.DataBean.StockListBean stockListBean : dataBean.getStock_list()) {
                    if (codeSet.contains(stockListBean.getCode())) {
                        continue;
                    }
                    List<StrongStockBean.DataBean.StockListBean> tempList = map.get(stockListBean.getContinue_num());
                    if (tempList == null) {
                        tempList = new ArrayList<>();
                    }
                    tempList.add(stockListBean);
                    map.put(stockListBean.getContinue_num(), tempList);
                    codeSet.add(stockListBean.getCode());
                    words.append(stockListBean.getReason_type());
                }

            }
            for (Map.Entry<Integer, List<StrongStockBean.DataBean.StockListBean>> entry : map.entrySet()) {
                bw.write(entry.getKey() + "连板:");
                oneBw.write(entry.getKey() + "连板:");
                twoBw.write(entry.getKey() + "连板:");

                bw.newLine();
                oneBw.newLine();
                twoBw.newLine();
                List<StrongStockBean.DataBean.StockListBean> stockListBeanList = entry.getValue();
                for (StrongStockBean.DataBean.StockListBean stockListBean : stockListBeanList) {
                    bw.write(stockListBean.getName() + "(" + stockListBean.getCode() + ") " + "价格：" + stockListBean.getLatest() + " 概念：" + stockListBean.getReason_type() + " 涨停时间：" + stockListBean.getFirstTime());
                    bw.newLine();
                    if (entry.getKey() == 1) {
                        oneBw.write(stockListBean.getName() + "(" + stockListBean.getCode() + ") " + "价格：" + stockListBean.getLatest() + " 概念：" + stockListBean.getReason_type() + " 涨停时间：" + stockListBean.getFirstTime());
                        oneBw.newLine();
                    } else {
                        twoBw.write(stockListBean.getName() + "(" + stockListBean.getCode() + ") " + "价格：" + stockListBean.getLatest() + " 概念：" + stockListBean.getReason_type() + " 涨停时间：" + stockListBean.getFirstTime());
                        twoBw.newLine();
                    }
                }
            }
            ConceptFrequencyAnalyzer.getWord(words.toString());
            AdvancedConceptCloudUtil.generate(words.toString());

            bw.close();
            oneBw.close();
            twoBw.close();

            fw.close();
            oneFw.close();
            twoFw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void analyzeStrongStockWord() {

        Set<String> codeSet = new HashSet<>();
        String today = CommonUtils.getTradeDay(0);
        String yesterDay = CommonUtils.getTradeDay(1);
        String preDay = CommonUtils.getTradeDay(2);
        List<StrongStockBean.DataBean> list = getStrongStockList(today);
        list.addAll(getStrongStockList(yesterDay));
        list.addAll(getStrongStockList(preDay));

        HashMap<Integer, List<StrongStockBean.DataBean.StockListBean>> map = new HashMap<>();
        StringBuilder words = new StringBuilder(1600);
        for (StrongStockBean.DataBean dataBean : list) {

            for (StrongStockBean.DataBean.StockListBean stockListBean : dataBean.getStock_list()) {
                if (codeSet.contains(stockListBean.getCode())) {
                    continue;
                }
                List<StrongStockBean.DataBean.StockListBean> tempList = map.get(stockListBean.getContinue_num());
                if (tempList == null) {
                    tempList = new ArrayList<>();
                }
                tempList.add(stockListBean);
                map.put(stockListBean.getContinue_num(), tempList);
                codeSet.add(stockListBean.getCode());
                words.append(stockListBean.getReason_type());
            }

        }
        ConceptFrequencyAnalyzer.getWord(words.toString());
        AdvancedConceptCloudUtil.generate(words.toString());

    }

}
