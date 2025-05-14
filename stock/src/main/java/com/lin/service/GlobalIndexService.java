package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.index.ConceptAndIndex;
import com.lin.bean.index.GloabalIndex;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import com.lin.util.WechatMessageUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取指数地址:https://quote.eastmoney.com/gb/zsHSNCON.html,通过secids
 */
@Service
public class GlobalIndexService {
    private static Integer shangZhenCountTime = 0;
    private static Integer shenZhenCountTime = 0;
    private static Integer chuangyebanCountTime = 0;
    private static Integer hengshengCountTime = 0;
    private static Integer hengshengKejiCountTime = 0;

    private static Integer shangZhenPaoLuCountTime = 0;
    private static Integer shenZhenPaoLuCountTime = 0;
    private static Integer chuangyebanPaoLuCountTime = 0;
    private static Integer hengshengPaoLuCountTime = 0;
    private static Integer hengshengKejiPaoLuCountTime = 0;

    private static Double maxIndex = 0.0;
    private static Double maxHengshengIndex = 0.0;
    private static Double maxHengshengKejiIndex = 0.0;

    private static Double minIndex = 0.0;
    private static Double minHengshengIndex = 0.0;
    private static Double minHengshengKejiIndex = 0.0;

    public static void main(String[] args) {
        observeIndex();
//        cleanCountTime();
    }

    /**
     * 获取重要指数指标
     *
     * @return
     */
    public static List<ConceptAndIndex.Index> getIndexInfo() {

        String url = "https://push2.eastmoney.com/api/qt/ulist/get";
        Map<String, Object> param = new HashMap<>();
//        String yesterDay = DateUtils.getYesterday(DateFormatEnum.DATE);
        String yesterDay = "2025-01-25";
        String tomorrow = DateUtils.getTomorrowTime(DateUtils.getNowTime(), DateFormatEnum.DATE);
        param.put("fltt", "1");
        param.put("invt", "2");
        param.put("fields", "f2,f12,f13,f14,f3,f152,f4");

        param.put("pn", "1");
        param.put("np", "1");
        param.put("pz", "100");
        param.put("dect", "1");
        param.put("wbp2u", "9650037204260920|0|1|0|web");
        param.put("secids", "1.000001,0.399001,0.399006,1.000300,1.000905,116.128229,105.NDX,105.SPX,100.DJIA,100.N225,100.HSI,124.HSTECH,885823,USDCNH.OTC");
        param.put("_", System.currentTimeMillis());


        String result = HttpUtil.get(url, param);
        GloabalIndex gloabalIndex = JSONUtil.toBean(JSONUtil.toJsonStr(result), GloabalIndex.class);
        List<GloabalIndex.DataBean.DiffBean> listBeanList = gloabalIndex.getData().getDiff();
        Integer total = gloabalIndex.getData().getTotal();
        Integer totalPage = total / 100 + 1;
        if (totalPage > 1) {
            for (int i = 2; i <= totalPage; i++) {
                param.put("pn", i);
                String tempResult = HttpUtil.get(url, param);
                GloabalIndex tempStockDocument = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), GloabalIndex.class);
                listBeanList.addAll(tempStockDocument.getData().getDiff());
            }
        }

        System.out.println(listBeanList.size());
        List<ConceptAndIndex.Index> resultList = new ArrayList<>();
        for (GloabalIndex.DataBean.DiffBean bean : listBeanList) {
            double index = CommonUtils.formatPrice(bean.getF3() == null ? 0 : bean.getF3() / 100);
            double curent = bean.getF2() == null ? 0.00 : CommonUtils.formatPrice(bean.getF2() / 100);
            String title = bean.getF14() + " " + curent + " " + index + "% ";
            System.out.println(title);
            ConceptAndIndex.Index data = new ConceptAndIndex.Index();
            data.setName(bean.getF14());
            data.setIndex(index);
            data.setCurrent(curent);
            resultList.add(data);
        }
        return resultList;
    }

    /**
     * 检测指数
     */


    @Scheduled(cron = "0 */1 * * * ?")
    public static void observeIndex() {
        LocalTime now = LocalTime.now();
        if ((now.isAfter(LocalTime.of(10, 0)) && now.isBefore(LocalTime.of(11, 30))) || // 10:00-11:30（不含11:30）
                (now.isAfter(LocalTime.of(13, 30)) && now.isBefore(LocalTime.of(15, 0))) || // 13:30-15:00（不含15:00）
                now.equals(LocalTime.of(15, 0))) {
            List<ConceptAndIndex.Index> indexList = getIndexInfo();
            for (ConceptAndIndex.Index bean : indexList) {
                if (bean.getName().equals("上证指数")) {
                    if (bean.getIndex() > 0.3) {
                        shangZhenCountTime += 1;
                        if (shangZhenCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "牛回速归 " + "涨幅：" + bean.getIndex() + "%", "");
                        }

                    }
                    if (bean.getIndex() < 0.3) {
                        shangZhenPaoLuCountTime += 1;
                        if (shangZhenPaoLuCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "砸盘，快跑 " + "跌幅：" + bean.getIndex() + "%", "");
                        }
                    }
                }
                if (bean.getName().equals("深证成指")) {
                    if (bean.getIndex() > 0.3) {
                        shenZhenCountTime += 1;
                        if (shenZhenCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "牛回速归 " + "涨幅：" + bean.getIndex() + "%", "");
                        }

                    }
                    if (bean.getIndex() < 0.3) {
                        shenZhenPaoLuCountTime += 1;
                        if (shenZhenPaoLuCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "砸盘，快跑 " + "跌幅：" + bean.getIndex() + "%", "");
                        }
                    }
                }
                if (bean.getName().equals("创业板指")) {
                    if (bean.getIndex() > 0.3) {
                        chuangyebanCountTime += 1;
                        if (chuangyebanCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "牛回速归 " + "涨幅：" + bean.getIndex() + "%", "");
                        }

                    }
                    if (bean.getIndex() < 0.3) {
                        chuangyebanPaoLuCountTime += 1;
                        if (chuangyebanPaoLuCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "砸盘，快跑 " + "跌幅：" + bean.getIndex() + "%", "");
                        }
                    }
                }
                if (bean.getName().equals("恒生指数")) {
                    if (bean.getIndex() > 0.3) {
                        hengshengCountTime += 1;
                        if (hengshengCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "牛回速归 " + "涨幅：" + bean.getIndex() + "%", "");
                        }

                    }
                    if (bean.getIndex() < 0.3) {
                        hengshengPaoLuCountTime += 1;
                        if (hengshengPaoLuCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "砸盘，快跑 " + "跌幅：" + bean.getIndex() + "%", "");
                        }
                    }
                }

                if (bean.getName().equals("恒生科技指数")) {
                    if (bean.getIndex() > 0.3) {
                        hengshengKejiCountTime += 1;
                        if (hengshengKejiCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "牛回速归 " + "涨幅：" + bean.getIndex() + "%", "");
                        }

                    }
                    if (bean.getIndex() < 0.3) {
                        hengshengKejiPaoLuCountTime += 1;
                        if (hengshengKejiPaoLuCountTime == 1) {
                            WechatMessageUtils.sendMessageToWechat(bean.getName() + "-" + "砸盘，快跑 " + "跌幅：" + bean.getIndex() + "%", "");
                        }
                    }
                }

            }
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public static void cleanCountTime() {
        LocalTime now = LocalTime.now();
        // 判断当前时间是否在指定时间段内
        if ((now.isAfter(LocalTime.of(10, 0)) && now.isBefore(LocalTime.of(11, 30))) || // 10:00-11:30（不含11:30）
                (now.isAfter(LocalTime.of(13, 30)) && now.isBefore(LocalTime.of(15, 0))) || // 13:30-15:00（不含15:00）
                now.equals(LocalTime.of(15, 0))) { //
            shenZhenCountTime = 0;
            chuangyebanCountTime = 0;
            hengshengCountTime = 0;
            hengshengKejiCountTime = 0;

            shangZhenPaoLuCountTime = 0;
            shenZhenPaoLuCountTime = 0;
            chuangyebanPaoLuCountTime = 0;
            hengshengPaoLuCountTime = 0;
            hengshengKejiPaoLuCountTime = 0;
        }
    }
}
