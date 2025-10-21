package com.lin.service;

import com.lin.bean.index.ConceptAndIndex;
import com.lin.bean.lianban.LianBanNew;
import com.lin.bean.lianban.ZhaBanBean;
import com.lin.bean.market.MarketBean.DataBean.MoneyBean;
import com.lin.bean.market.MarketBean.DataBean.StockBean;

import com.lin.bean.MarketSentiment;
import com.lin.bean.lianban.LimitDownPoolResultBean;
import com.lin.bean.lianban.LimitUpPoolResultBean;
import com.lin.bean.market.MarketBean;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.bean.tonghuashun.LimitUpData;
import com.lin.util.CommonUtils;
import jdk.jfr.Unsigned;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-10-18 11:31
 */
@Service
public class MarketSentimentService {
    @Autowired
    IncreaseAndDecreaseService increaseAndDecreaseService;
    @Autowired
    LimitUpPoolService limitUpPoolService;
    @Autowired
    LimitDownPoolService limitDownPoolService;
    @Autowired
    MarketVolumeTemperatureService marketVolumeTemperatureService;
    @Autowired
    StrongStockService strongStockService;

    @Autowired
    ZhaBanChiService zhaBanChiService;

    @Autowired
    GlobalIndexService globalIndexService;

    public MarketSentiment getMarketSentiment() {
        String dateIndex = "0";
        String date = CommonUtils.getTradeDay(Integer.valueOf(dateIndex));
        MarketSentiment marketSentiment = new MarketSentiment();


        try {
            List<LimitUpPoolResultBean> todayLimitUpList = limitUpPoolService.getLimitUpPoolList(date, "330323", "1");
            List<LimitDownPoolResultBean> todayLimitDownList = limitDownPoolService.getLimitDownPoolList(date, "330323", "1");
            marketSentiment.setLimitUpNum(todayLimitUpList.size());
            marketSentiment.setLimitDownNum(todayLimitDownList.size());
            MarketBean.DataBean marketBean = marketVolumeTemperatureService.getMarketList().getData();
            MoneyBean moneyBean = marketBean.getMoneyBean();
            StockBean stock = marketBean.getStock();

            marketSentiment.setTemperature(marketBean.getTemperature());
            marketSentiment.setShangzhengUpNum(moneyBean.getAUp());
            marketSentiment.setShangzhengDownNum(moneyBean.getADown());
            marketSentiment.setShenzhenUpNum(moneyBean.getBUp());
            marketSentiment.setShenzhengDownNum(moneyBean.getBDown());

            marketSentiment.setTotalUpNum(stock.getUp());
            marketSentiment.setTotalDownNum(stock.getDown());
            marketSentiment.setTotalNum(stock.getTotal());
            marketSentiment.setTotalNum(stock.getUp());
            marketSentiment.setFiveUpNum(stock.getUp5p());
            marketSentiment.setFiveDownNum(stock.getDown5p());
            marketSentiment.setTotalMoney(moneyBean.getTotalMoney());

            List<ZhaBanBean.DataDTO.InfoDTO> zhaBanList = zhaBanChiService.getZhaBanData(date);
            //炸板率用 炸板/涨停+炸板
            double zhaBanRate = (double) zhaBanList.size() / (todayLimitUpList.size() + zhaBanList.size()) * 100;
            marketSentiment.setZhaBanNum(zhaBanList.size());
            marketSentiment.setZhaBanRate(CommonUtils.formatPrice(zhaBanRate));

            increaseAndDecreaseService.getLimitUp2Data();
            List<LimitUpData> yesterdayListData = increaseAndDecreaseService.getYesterdayLimitUpData();
            double yesterdayRate = 0;
            for (LimitUpData bean : yesterdayListData) {
                yesterdayRate += bean.getRate();
            }
            yesterdayRate = yesterdayRate / yesterdayListData.size();
            marketSentiment.setYesterDayLimitUpRate(CommonUtils.formatPrice(yesterdayRate));

            List<IncreaseRankData> yesterdayUp2Data = increaseAndDecreaseService.getLimitUp2Data();
            double yesterdayUp2Rate = 0;
            for (IncreaseRankData bean : yesterdayUp2Data) {
                yesterdayUp2Rate += bean.getRate();
            }
            yesterdayUp2Rate = yesterdayUp2Rate / yesterdayUp2Data.size();
            marketSentiment.setYesterDayTwoLimitUpRate(CommonUtils.formatPrice(yesterdayUp2Rate));
            marketSentiment.setYesterDayLimitUpNum(yesterdayListData.size());
            marketSentiment.setYesterDayTwoLimitUpNum(yesterdayListData.size());

            List<ConceptAndIndex.Index> globalIndexList = globalIndexService.getIndexInfo();
            marketSentiment.setShanZhengRate(globalIndexList.get(0).getIndex());
            marketSentiment.setShenZhengRate(globalIndexList.get(1).getIndex());
            marketSentiment.setChuangyeBanRate(globalIndexList.get(2).getIndex());
            marketSentiment.setHuShenRate(globalIndexList.get(3).getIndex());
            marketSentiment.setZhongZheng500Rate(globalIndexList.get(4).getIndex());
            marketSentiment.setDaoQiongsiRate(globalIndexList.get(5).getIndex());
            marketSentiment.setRiJing225Rate(globalIndexList.get(6).getIndex());
            marketSentiment.setHengShengRate(globalIndexList.get(7).getIndex());
            marketSentiment.setHengShengKeJiRate(globalIndexList.get(8).getIndex());
            marketSentiment.setShanZhengCurrent(globalIndexList.get(0).getCurrent());

            List<LianBanNew> lianBanNewList = strongStockService.dealLianBanData(date);
            marketSentiment.setLianBanNum(lianBanNewList.get(0).getLimitNum());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketSentiment;
    }
}