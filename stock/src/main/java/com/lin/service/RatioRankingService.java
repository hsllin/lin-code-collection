package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.RatioRankingBean;
import com.lin.bean.RatioRankingResultBean;
import com.lin.util.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 量比排行
 * 东方财富：https://quote.eastmoney.com/center/gridlist.html#hs_a_board
 */
@Service
public class RatioRankingService {
    public static void main(String[] args) {
        getRatioRanking();
    }
    public static RatioRankingResultBean getRatioRanking() {
        String url = "\n" +
                "https://push2.eastmoney.com/api/qt/clist/get";
        Map<String, Object> param = new HashMap<>();

        param.put("fields", "f12,f13,f14,f1,f2,f4,f3,f152,f5,f6,f7,f15,f18,f16,f17,f10,f8,f9,f23");
        param.put("np", "1");
        param.put("fid", "f10");
        param.put("np", "1");
        param.put("pn", "1");
        param.put("pz", "20");
        param.put("po", "1");
        param.put("dect", "1");

        param.put("secids", "1.000001,0.399001");
        param.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        param.put("fs", "m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048");

        param.put("invt", "2");
        param.put("fltt", "1");
        param.put("wbp2u", "|0|0|0|web");
        param.put("_", System.currentTimeMillis());


        String result = HttpUtil.get(url, param);
        RatioRankingBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), RatioRankingBean.class);
        RatioRankingBean.DataBean dataBean = bean.getData();
        List<RatioRankingBean.DataBean.DiffBean> infoList = dataBean.getDiff();
        Map<String, Object> map = new HashMap<>();
        Double totalMoney = 0.0;
        RatioRankingResultBean rankingResultBean = new RatioRankingResultBean();
        List<RatioRankingResultBean.DataBean> dataBeanList = new ArrayList<>();
        //搞个100条数据看看
        for (int i = 2; i < 6; i++) {
            param.put("pn", i);
            String tempResult = HttpUtil.get(url, param);
            RatioRankingBean tempBean = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), RatioRankingBean.class);
            infoList.addAll(tempBean.getData().getDiff());
        }
        for (RatioRankingBean.DataBean.DiffBean diffBean : infoList) {
            RatioRankingResultBean.DataBean item = new RatioRankingResultBean.DataBean();
            item.setCode(diffBean.getF12());
            item.setName(diffBean.getF14());
            item.setRatio(CommonUtils.formatPrice(diffBean.getF10() / 100));
            item.setIncreaseAndDecrease(CommonUtils.formatPrice(diffBean.getF3() / 100));
            item.setCurrent(CommonUtils.formatPrice(diffBean.getF2() / 100));
            item.setChangeRate(CommonUtils.formatPrice(diffBean.getF8() / 100));
            item.setAmplitude(CommonUtils.formatPrice(diffBean.getF7() / 100));
            item.setMax(CommonUtils.formatPrice(diffBean.getF15() / 100));
            item.setMin(CommonUtils.formatPrice(diffBean.getF16() / 100));
            item.setSellMoney(CommonUtils.formatBigNum(String.valueOf(diffBean.getF6())));
            dataBeanList.add(item);

        }
        rankingResultBean.setDataBeanList(dataBeanList);

        return rankingResultBean;
    }
}
