package com.lin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.LianBanBean;
import com.lin.util.DateUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

@Service
public class LianBanChiService {
    public static void main(String[] args) {

        String today = DateUtil.format(DateUtil.date(), DateFormatEnum.DATE_WITH_OUT_LINE.getValue());
        String beforeDay = DateUtils.getBeforeDay(DateFormatEnum.DATE_WITH_OUT_LINE);
        String yesterDay = DateUtils.getYesterday(DateFormatEnum.DATE_WITH_OUT_LINE);
//        logLianBan(beforeDay);
//        logLianBan(yesterDay);
//        logLianBan(today);
//        lianBan("20250122");
//        lianBan("20250123");
//        lianBan("20250127");
        downloadData("20250127");
    }

    public static List<LianBanBean.DataBean> lianBan(String date) {
        String url = "https://data.10jqka.com.cn/dataapi/limit_up/continuous_limit_up";

//        20250121
        Map<String, Object> param = new HashMap<>();
//        param.put("cb", "jQuery1123006755088795545139_173727505367");
        param.put("filter", "HS,GEM2STAR");
        param.put("date", date);

        String result = HttpUtil.get(url, param);
        LianBanBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), LianBanBean.class);
        Map<Integer, List<Map<String, Object>>> dealData = new HashMap<>();
        List<LianBanBean.DataBean> listBeanList = bean.getData();
        if (null == listBeanList) {
            return new ArrayList<>();
        }
        for (LianBanBean.DataBean dataBean : listBeanList) {
            List<LianBanBean.DataBean.CodeListBean> codeListBeanList = dataBean.getCode_list();
            for (LianBanBean.DataBean.CodeListBean codeListBean : codeListBeanList) {
                dealData.put(codeListBean.getContinue_num(), new ArrayList<>());
            }

        }
        StringBuilder message = new StringBuilder(128);
        Date temDate = DateUtil.parse(date);
        ;
        message.append(DateUtil.formatDate(temDate) + "连板信息\n");
        for (LianBanBean.DataBean dataBean : listBeanList) {
            List<LianBanBean.DataBean.CodeListBean> codeListBeanList = dataBean.getCode_list();
            message.append(dataBean.getHeight() + "连板：");
            for (LianBanBean.DataBean.CodeListBean codeListBean : codeListBeanList) {

                message.append(codeListBean.getName() + " " + codeListBean.getCode() + " ");
            }
            message.append("\n");
        }
        System.out.println("\033[34;4m" + message.toString() + "\033[0m");
        return listBeanList;
    }

    public static void downloadData(String date) {

        try {
            FileWriter fw = new FileWriter("D:\\1stock\\连板池.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<LianBanBean.DataBean> list = lianBan(date);
            for (LianBanBean.DataBean dataBean : list) {
                List<LianBanBean.DataBean.CodeListBean> codeListBeanList = dataBean.getCode_list();
                bw.write(dataBean.getHeight() + "连板：");
                bw.newLine();
                for (LianBanBean.DataBean.CodeListBean codeListBean : codeListBeanList) {

                    bw.write(codeListBean.getName() + " " + codeListBean.getCode() + " ");
                    bw.newLine();
                }
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }


    }
}
