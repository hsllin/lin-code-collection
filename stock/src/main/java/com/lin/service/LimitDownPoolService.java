package com.lin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.LimitDownBean;
import com.lin.bean.lianban.LimitDownPoolResultBean;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 涨停个股
 * https://data.10jqka.com.cn/datacenterph/limitup/limtupInfo.html#/limtupStrength
 */
@Service
public class LimitDownPoolService {
    public static void main(String[] args) {

        String today = DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue());
        String beforeDay = DateUtils.getBeforeDay(DateFormatEnum.DATE_WITH_OUT_LINE);
        String yesterDay = DateUtils.getYesterday(DateFormatEnum.DATE_WITH_OUT_LINE);
        getLimitDownPoolList(today, "", "");
    }

    public static List<LimitDownPoolResultBean> getLimitDownPoolList(String date, String orderFiled, String orderBy) {
        List<LimitDownPoolResultBean> resultList = new ArrayList<>();
        String url = "https://data.10jqka.com.cn/dataapi/limit_up/lower_limit_pool";
        Map<String, Object> param = new HashMap<>();

        param.put("filter", "HS,GEM2STAR");
        param.put("field", "199112,10,330333,330334,1968584,3475914,9004");

        param.put("page", "1");
        param.put("limit", "15");

        param.put("order_field", orderFiled);
        param.put("order_type", orderBy);

        param.put("filter", "HS,GEM2STAR");
        param.put("date", date);
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        LimitDownBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), LimitDownBean.class);
        LimitDownBean.DataBean dataBean = bean.getData();
        List<LimitDownBean.DataBean.InfoBean> infoList = dataBean.getInfo();
        Integer count = bean.getData().getPage().getCount();
        for (int i = 2; i <= count; i++) {
            param.put("page", i);
            String pageResult = HttpUtil.get(url, param);
            LimitDownBean pageBean = JSONUtil.toBean(JSONUtil.toJsonStr(pageResult), LimitDownBean.class);
            infoList.addAll(pageBean.getData().getInfo());
        }
        for (LimitDownBean.DataBean.InfoBean data : infoList) {
            LimitDownPoolResultBean resultBean = new LimitDownPoolResultBean();
            resultBean.setCode(data.getCode());
            resultBean.setName(data.getName());
            resultBean.setPercent(CommonUtils.formatPrice(data.getChange_rate()));
            resultBean.setPrice(data.getLatest());
//            resultBean.setReason(data.get());
            resultBean.setFirstTime(DateUtils.changeTime(data.getFirst_limit_down_time(), DateFormatEnum.HOUR_MIN_SECORD));
            resultBean.setEndTime(DateUtils.changeTime(data.getLast_limit_down_time(), DateFormatEnum.HOUR_MIN_SECORD));
//            resultBean.setUpType(data.getLimit_up_type());
//            resultBean.setBoardNum(data.getIs_again_limit());
//            resultBean.setUpMoney(CommonUtils.formatBigNum(String.valueOf(data.getOrder_amount())));
            resultBean.setChangeRate(CommonUtils.formatPrice(data.getTurnover_rate()));
            resultBean.setTotalPrice(CommonUtils.formatBigNum(String.valueOf(data.getCurrency_value())));
            resultList.add(resultBean);

        }
//        System.out.println("\033[34;4m" + message.toString() + "\033[0m");
        return resultList;
    }

    public static void downloadLimitDownData(String date) {
        try {
            FileWriter fw = new FileWriter("D:\\1stock\\昨日跌停.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<LimitDownPoolResultBean> list = getLimitDownPoolList(date, "330323", "1");
            for (LimitDownPoolResultBean dataBean : list) {

                bw.write(dataBean.getCode() + " " + dataBean.getName() + " " + dataBean.getFirstTime());
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
