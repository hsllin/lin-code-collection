package com.lin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.Constants;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.LianBanBean;
import com.lin.bean.lianban.ZhaBanBean;
import com.lin.config.DataPathConfig;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

@Service
public class ZhaBanChiService {
    
    @Autowired
    private DataPathConfig dataPathConfig;
    
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
//        downloadData("20250127");
//        getZhaBanData("20250513");
//        downloadData("20250513");
    }

    public static List<ZhaBanBean.DataDTO.InfoDTO> getZhaBanData(String date) {
        String url = "https://data.10jqka.com.cn/dataapi/limit_up/open_limit_pool";

//        20250121
        Map<String, Object> param = new HashMap<>();
//        param.put("cb", "jQuery1123006755088795545139_173727505367");
        param.put("page", "1");
        param.put("limit", "100");
        param.put("filter", "HS,GEM2STAR");
        param.put("field", "199112,9002,48,1968584,19,3475914,9003,10,9004");
        param.put("order_field", "199112");
        param.put("order_type", "0");
        param.put("date", date);

        String result = HttpUtil.get(url, param);
        ZhaBanBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), ZhaBanBean.class);
        Map<Integer, List<Map<String, Object>>> dealData = new HashMap<>();
        List<ZhaBanBean.DataDTO.InfoDTO> listBeanList = bean.getData().getInfo();

        return listBeanList;
    }

    public void downloadData(String date) {

        try {
            FileWriter fw = dataPathConfig.getDataFileWriter("炸板池.txt");

            BufferedWriter bw = new BufferedWriter(fw);

            List<ZhaBanBean.DataDTO.InfoDTO> list = getZhaBanData(date);
            for (ZhaBanBean.DataDTO.InfoDTO dataBean : list) {
                bw.write(dataBean.getCode() + " " + dataBean.getName() + " 是否反复开板："+ ("1".equals(dataBean.getIsAgainLimit())?"是":"否")  + " 涨跌幅："+ CommonUtils.formatPrice(dataBean.getChangeRate())  + "% 换手率："+ CommonUtils.formatPrice(dataBean.getTurnoverRate()) + "% ");
                bw.newLine();
            }

            bw.close();

            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }


    }
}
