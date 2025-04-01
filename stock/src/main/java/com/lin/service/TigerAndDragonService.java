package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.market.MarketBean;
import com.lin.bean.market.MarketTimeLineBean;
import com.lin.bean.market.MarketVolumeBean;
import com.lin.bean.market.OneWordLimitBean;
import com.lin.bean.tigerdragon.LhbBean;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 龙虎榜
 * https://summary.jrj.com.cn/dataCenter/lhb
 */
@Service
public class TigerAndDragonService {
    public static void main(String[] args) {
        getTigerAndDragonData();
    }

    public static LhbBean getTigerAndDragonData() {

        String url = "https://gateway.jrj.com/quot-dc/lhb/stocklist";
        Map<String, Object> param = new HashMap<>();
        String date = CommonUtils.getTradeDay(0);
        String today = DateUtils.transferFormatTime(date, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE);


        param.put("endDate", today);
        param.put("pageNum", "0");
        param.put("queryFlag", "2");
        param.put("pageSize", "0");

        String result = HttpUtil.createPost(url).contentType("application/json").body(JSONUtil.toJsonStr(param)).execute().body();
        LhbBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), LhbBean.class);
        if (bean.getData().getLhbStocks().size() == 0) {
            date = CommonUtils.getTradeDay(1);
            today = DateUtils.transferFormatTime(date, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE);
            param.put("endDate", today);
            result = HttpUtil.createPost(url).contentType("application/json").body(JSONUtil.toJsonStr(param)).execute().body();
            bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), LhbBean.class);
        }

        for (LhbBean.DataBean.LhbStocksBean stocksBean : bean.getData().getLhbStocks()) {
            stocksBean.setChg(CommonUtils.formatPrice(stocksBean.getChg() * 100));
            stocksBean.setInfoClassName(dealTag(stocksBean.getInfoClassName()));
            for (LhbBean.DataBean.LhbStocksBean.LhbBranchBean.BuyBranchesBean buy : stocksBean.getLhbBranch().getBuyBranches()) {
                buy.setBuyRatio(CommonUtils.formatPrice(buy.getBuyRatio() * 100));
                buy.setSellRatio(CommonUtils.formatPrice(buy.getSellRatio() * 100));
            }


        }
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

        String result = HttpUtil.get(url, param);
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
        map.put("totalMoney", CommonUtils.formatBigNum(String.valueOf(totalMoney)));
//        System.out.println("\033[34;4m" + message.toString() + "\033[0m");
        return map;
    }

    public static String dealTag(String reason) {
        switch (reason) {
            case "日振幅值达15%的前五只证券":
                return "振幅15%";
            case "日涨幅偏离值达7%的前五只证券":
                return "振幅7%";
            case "连续三个交易日内,涨幅偏离值累计达20%的证券":
                return "三日20%";
            case "日换手率达20%的前五只证券":
                return "换手20%";
            case "日换手率达到30%的前五只证券":
                return "换手30%";
            case "退市整理的证券":
                return "退市";
            case "日跌幅偏离值达7%的前五只证券":
                return "跌幅超7%";
            case "连续三个交易日内,涨幅偏离值累计达12%的ST、*ST证券和S证券":
                return "st三日涨12%";
            case "连续三个交易日内,涨幅偏离值累计达30%的证券":
                return "三日涨30%";
            case "日涨幅达到15%严重异常期间日收盘价格涨幅偏离值累计达到100%的证券":
                return "日涨15%";

            case "严重异常期间日收盘价格涨幅偏离值累计达到100%的证券":
                return "收盘偏离100%";

            case "日涨幅达到15%的前五只证券":
                return "日涨15%";

        }
        return reason;
    }

    public String getMainFunds(String code, String name) {
        switch (code) {
            case "201963008":
                return "宁波桑田路";
            case "200061003":
                return "陈小群";
        }
        return name;
    }

}
