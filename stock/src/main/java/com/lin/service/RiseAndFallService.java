package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.risefall.DayRiseBean;
import com.lin.util.CommonUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.cglib.beans.BeanMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 涨跌幅榜
 * @create 2025-05-19 15:39
 */
public class RiseAndFallService {
    public static void main(String[] args) throws IOException, InterruptedException {

        getStrongStockList();
    }

    public static List<Map> getStrongStockList() throws IOException, InterruptedException {

        String url = "https://d.10jqka.com.cn/v2/rank/33,17/199112/d100.js";
        Map<String, Object> param = new HashMap<>();
        param.put("filter", "HS,GEM2STAR");
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
        result = dealData(result);
        List<DayRiseBean> temp = JSONUtil.toList(JSONUtil.parseArray(result), DayRiseBean.class);

        List<Map> listBeanList = new ArrayList<>();
        listBeanList = JSONUtil.toList(JSONUtil.parseArray(result), Map.class);
        for (Map map : listBeanList) {
            map.put("name", StringEscapeUtils.unescapeJava(map.get("55") + ""));
            map.put("code", map.get("5") + "");
            map.put("riseRate", map.get("199112") + "");
            map.put("changeRate", map.get("1968584") + "");
            map.put("money", CommonUtils.formatPrice(Double.parseDouble(map.get("13") + "")));
            map.put("industry", StockInfoFetcherService.parseStockSector(map.get("5") + "").getIndustry());
        }
        return listBeanList;
    }

    public static String dealData(String str) {
        String result = "";
        int startIndex = str.indexOf('[');      // 定位第一个[
        int endIndex = str.lastIndexOf(']');     // 定位最后一个]

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            result = str.substring(startIndex, endIndex + 1);
            System.out.println(result);  // 输出：A=1], [B=2], [C=3
        } else {
            System.out.println("符号不存在或位置无效");
        }
        return result;
    }

    public static <T> List<T> convertByBeanMap(List<Map> mapList, Class<T> beanClass) {
        List<T> beanList = new ArrayList<>();
        try {
            for (Map<String, Object> map : mapList) {
                T bean = beanClass.getDeclaredConstructor().newInstance();
                BeanMap beanMap = BeanMap.create(bean);
                beanMap.putAll(map);
                beanList.add(bean);
            }
        } catch (Exception e) {
            throw new RuntimeException("CGLIB转换异常", e);
        }
        return beanList;
    }
}