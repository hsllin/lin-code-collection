package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.market.MarketBean;
import com.lin.bean.market.MarketTimeLineBean;
import com.lin.bean.market.MarketVolumeBean;
import com.lin.bean.market.OneWordLimitBean;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 市场成交温度计
 */
@Service
public class MarketVolumeTemperatureService {
    @Autowired
    VolumeTrendService volumeTrendService;
    @Autowired
    StrongStockService strongStockService;

    public static void main(String[] args) {
        String url = "https://data.eastmoney.com/bkzj/jlr.html";
        String url2 = "\n" +
                "https://push2.eastmoney.com/api/qt/ulist.np/get?cb=jQuery1123011745193146180988_1739285807613&fltt=2&secids=1.000001%2C0.399001&fields=f1%2Cf2%2Cf3%2Cf4%2Cf6%2Cf12%2Cf13%2Cf104%2Cf105%2Cf106&ut=b2884a393a59ad64002292a3e90d46a5&_=173928580761;";
        url2 = "https://summary.jrj.com.cn/dataCenter/zdtwdj?jrjbq";

        //龙虎榜：https://summary.jrj.com.cn/dataCenter/lhb?jrjbq
        //大盘云图 https://summary.jrj.com.cn/dataCenter/dpyt/
//        getMarketList("20250211");
        getMarketVolume();
        getStrongStockList("");
    }

    public static MarketBean getMarketList(String date) {

        String url = "https://gateway.jrj.com/quot-dc/zdt/market";
        Map<String, Object> param = new HashMap<>();
//        param.put("tradedate", "20250211");
//        param.put("date", date);

        String result = HttpUtil.post(url, param);
        MarketBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), MarketBean.class);
        MarketBean.DataBean.MoneyBean moneyBean = new MarketBean.DataBean.MoneyBean();
        Map<String, Object> map = getMarketVolume();
        moneyBean.setAMoney(map.get("aMoney").toString());
        moneyBean.setAUp(map.get("aUp").toString());
        moneyBean.setADown(map.get("aDown").toString());
        moneyBean.setBMoney(map.get("bMoney").toString());
        moneyBean.setBUp(map.get("bUp").toString());
        moneyBean.setBDown(map.get("bDown").toString());
        moneyBean.setTotalMoney(map.get("totalMoney").toString());
        moneyBean.setTotalOriginalMoney(map.get("totalOriginalMoney").toString());


        bean.getData().setMoneyBean(moneyBean);

        return bean;
    }

    /**
     * 一字涨停
     *
     * @param date
     * @return
     */
    public static OneWordLimitBean getStrongStockList(String date) {

        String url = "https://gateway.jrj.com/quot-dc/zdt/yzzdt";
        Map<String, Object> param = new HashMap<>();

        String result = HttpUtil.post(url, param);
        OneWordLimitBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), OneWordLimitBean.class);
        return bean;
    }

    /**
     * 一字涨停
     *
     * @param date
     * @return
     */
    public static MarketTimeLineBean getMarketTimeLine(String date) {

        String url = " https://gateway.jrj.com/quot-dc/zdt/market_timeline";
        Map<String, Object> param = new HashMap<>();

        String result = HttpUtil.post(url, param);
        MarketTimeLineBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), MarketTimeLineBean.class);
        return bean;
    }

    /**
     * 获取大a涨跌
     *
     * @return
     */
    public static Map<String, Object> getMarketVolume() {
        String url = "https://push2.eastmoney.com/api/qt/ulist.np/get";
        Map<String, Object> param = new HashMap<>();

        param.put("fields", "f1%2Cf2%2Cf3%2Cf4%2Cf6%2Cf12%2Cf13%2Cf104%2Cf105%2Cf10");

        param.put("fltt", "2");
        param.put("secids", "1.000001,0.399001");
        param.put("ut", "b2884a393a59ad64002292a3e90d46a5");

        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        MarketVolumeBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), MarketVolumeBean.class);
        MarketVolumeBean.DataBean dataBean = bean.getData();
        List<MarketVolumeBean.DataBean.DiffBean> infoList = dataBean.getDiff();
        Map<String, Object> map = new HashMap<>();
        Double totalMoney = 0.0;
        for (int i = 0; i < infoList.size(); i++) {

            MarketVolumeBean.DataBean.DiffBean data = infoList.get(i);
            //a是上证，b是深证
            if (i == 0) {
                map.put("aMoney", data.getF6());
                map.put("aUp", data.getF104());
                map.put("aDown", data.getF105());
            } else {
                map.put("bMoney", data.getF6());
                map.put("bUp", data.getF104());
                map.put("bDown", data.getF105());
            }
            totalMoney += data.getF6();
        }

        map.put("totalOriginalMoney", String.valueOf(totalMoney));
        map.put("totalMoney", CommonUtils.formatBigNum(String.valueOf(totalMoney)));
//        System.out.println("\033[34;4m" + message.toString() + "\033[0m");
        return map;
    }


}
