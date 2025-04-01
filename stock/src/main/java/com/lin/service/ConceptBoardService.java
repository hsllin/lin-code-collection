package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.board.IndustryBoard;
import com.lin.bean.index.ConceptAndIndex;
import com.lin.util.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 概念板块
 * https://quote.eastmoney.com/center/gridlist.html#concept_board
 */
@Service
public class ConceptBoardService {
    public static void main(String[] args) {
//
        System.out.println("涨幅概念-----------------------------------------------------");
        logLianBan("1", "concept");
        System.out.println("跌幅概念-----------------------------------------------------");
        logLianBan("0","concept");
    }

    public static List<ConceptAndIndex.Concept> logLianBan(String order, String type) {
        String url = "https://push2.eastmoney.com/api/qt/clist/get";
        String fs = "m:90+t:3+f:!50";
        if (type.equals("industry")) {
            fs = "m:90+t:2+f:!50";
        } else if (type.equals("concept")) {
            fs = "m:90+t:3+f:!50";
        }

//        20250121
        Map<String, Object> param = new HashMap<>();
//        param.put("wbp2u", "9650037204260920|0|1|0|web");
        param.put("np", "1");
        param.put("fid", "f3");
        param.put("fs", fs);
        param.put("pn", "1");
        param.put("pz", "20");

        param.put("dect", "1");
        param.put("fltt", "1");
        param.put("fields", "f12,f13,f14,f1,f2,f4,f3,f152,f20,f8,f104,f105,f128,f140,f141,f207,f208,f209,f136,f222");
        param.put("invt", "2");
//        param.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        //排序，1和0
        param.put("po", order);
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        IndustryBoard bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), IndustryBoard.class);

        List<IndustryBoard.DataBean.DiffBean> allList = bean.getData().getDiff();
        Integer total = bean.getData().getTotal();
        String increase = "";
        String decrease = "";
        StringBuilder message = new StringBuilder(128);
        String increaseOrDecrease = "";
        if ("1".equals(order)) {
            increaseOrDecrease = "\033[31;4m" + "上涨";
        } else if ("0".equals(order)) {
            increaseOrDecrease = "\033[35;4m" + "下跌";
        }
        List<ConceptAndIndex.Concept> resultList = new ArrayList<>();
        for (IndustryBoard.DataBean.DiffBean diffBean : allList) {
            double percent = (double) diffBean.getF3() / 100;
            double faucetPercent = (double) diffBean.getF136() / 100;
            double decreasePercent = (double) diffBean.getF222() / 100;
            increase = " 领涨龙头：" + diffBean.getF14() + "(" + diffBean.getF140() + ")" + " 涨幅：" + CommonUtils.formatPrice(faucetPercent) + " ";
            decrease = " 最大跌幅：" + diffBean.getF207() + "(" + diffBean.getF208() + ")" + " 涨幅：" + CommonUtils.formatPrice(decreasePercent);
            message.append(increaseOrDecrease + "行业：" + diffBean.getF14() + " 涨幅：" + CommonUtils.formatPrice(percent) + "%" + increase + decrease + "%\n" + "\033[0m");
            System.out.println(message.toString());
            message.setLength(0);
            ConceptAndIndex.Concept concept = new ConceptAndIndex.Concept();
            concept.setLeader(diffBean.getF128() + "(" + diffBean.getF140() + ")");
            concept.setLeaderPercent(CommonUtils.formatPrice(faucetPercent));
            concept.setConcept(diffBean.getF14());
            concept.setConceptPercent(CommonUtils.formatPrice(percent));
            concept.setUp(diffBean.getF104());
            concept.setDown(diffBean.getF105());

            concept.setDownConcept(diffBean.getF207() + "(" + diffBean.getF208() + ")");
            concept.setDownConceptPercent(CommonUtils.formatPrice(decreasePercent));
            resultList.add(concept);
        }
        return resultList;
    }
}
