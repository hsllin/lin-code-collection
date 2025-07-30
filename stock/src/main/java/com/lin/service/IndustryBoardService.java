package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.board.IndustryBoard;
import com.lin.util.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndustryBoardService {

    public static void main(String[] args) {
//
//        System.out.println("涨幅行业-----------------------------------------------------");
//        logLianBan("1");
//        System.out.println("跌幅行业-----------------------------------------------------");
//        logLianBan("0");
//        testBobao();
    }

    public static void logLianBan(String order) {
        String url = "https://push2.eastmoney.com/api/qt/clist/get";


//        20250121
        Map<String, Object> param = new HashMap<>();
//        param.put("wbp2u", "9650037204260920|0|1|0|web");
        param.put("np", "1");
        param.put("fid", "f3");
        param.put("fs", "m%3A90%2Bt%3A2%2Bf%3A!50");
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
        String type = "";
        if ("1".equals(order)) {
            type = "\033[31;4m" + "快速上涨";
        } else if ("0".equals(order)) {
            type = "\033[35;4m" + "order";
        }
        for (IndustryBoard.DataBean.DiffBean diffBean : allList) {
            double percent = (double) diffBean.getF3() / 100;
            double faucetPercent = (double) diffBean.getF136() / 100;
            double decreasePercent = (double) diffBean.getF222() / 100;
            increase = " 领涨龙头：" + diffBean.getF14() + "(" + diffBean.getF128() + ")" + " 涨幅：" + CommonUtils.formatPrice(faucetPercent) + " ";
            decrease = " 最大跌幅：" + diffBean.getF207() + "(" + diffBean.getF208() + ")" + " 涨幅：" + CommonUtils.formatPrice(decreasePercent);
            message.append(type + "行业：" + diffBean.getF14() + " 涨幅：" + CommonUtils.formatPrice(percent) + "%" + increase + decrease + "%\n" + "\033[0m");
            System.out.println(message.toString());
            message.setLength(0);
        }


    }


}
