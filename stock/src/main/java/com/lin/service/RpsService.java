package com.lin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.EnumCacheType;
import com.lin.bean.lianban.LianBanBean;
import com.lin.bean.rps.RpsBean;
import com.lin.bean.rps.RpsDetailBean;
import com.lin.bean.rps.RpsUserInfo;
import com.lin.repository.RpsUserInfoRepository;
import com.lin.util.DateUtils;
import com.ugrong.framework.redis.repository.cache.IRedisObjectRepository;
import com.ugrong.framework.redis.repository.cache.IStringRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lin.bean.EnumCacheType.RPS_USER_INFO;

@Service
public class RpsService {
    @Autowired
    private static RpsUserInfoRepository rpsUserInfoRepository;

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
        getRpsData();
//        getRpsData();
    }

    public static List<RpsBean> getRpsData() {
        RpsUserInfo rpsUserInfo = generateUserData();
        String url = "https://www.rpshelper.cn/rpsapi/getBlockList";
        Map<String, Object> param = new HashMap<>();
        param.put("v", "1");
        param.put("src_type", "1");
        param.put("orderField", "pct_change");
        param.put("orderType", "desc");
        String result = HttpRequest.get(url)
                .header("Authorization", "Bearer "+rpsUserInfo.getToken())// token鉴权
                .form(param)
                .execute().body();
        List<RpsBean> listBeanList = JSONUtil.toList(JSONUtil.parseArray(result), RpsBean.class);
        return listBeanList;
    }

    public static List<RpsDetailBean> getRpsDataDetail(String blockName) {
        RpsUserInfo rpsUserInfo = generateUserData();
        String url = "https://www.rpshelper.cn/rpsapi/getBlockStocks?market_type[]=ZB&market_type[]=CYB&market_type[]=KCB&market_type[]=BJS";
        Map<String, Object> param = new HashMap<>();
        param.put("src_type", "1");
        param.put("orderField", "pct_change");
        param.put("orderType", "desc");
        param.put("blockName", blockName);
        param.put("isFond", "1");
        param.put("orderField", "day_rate");
        param.put("orderType", "2");
        param.put("orderField", "day_rate");

        String result = HttpRequest.get(url)
                .header("Authorization", "Bearer "+rpsUserInfo.getToken())// token鉴权
                .form(param)
                .execute().body();
        List<RpsDetailBean> listBeanList = JSONUtil.toList(JSONUtil.parseArray(result), RpsDetailBean.class);
        return listBeanList;
    }

    public static RpsUserInfo generateUserData() {
//        RpsUserInfo userInfo = rpsUserInfoRepository.get(RPS_USER_INFO.getValue()).get();
//        if (userInfo != null) {
//            return userInfo;
//        }
        //每次都生成一个token，注意返回的数据有过期日期
        String url = "https://www.rpshelper.cn/rpsapi/login?v=1747293188287&src_type=1";
        Map<String, Object> param = new HashMap<>();
        param.put("user", "linmeimei88");
        param.put("password", "34f2042426d1648625572ecbc6ce3f3d");
        param.put("captcha", "true");
        String result = HttpUtil.post(url, param);
        RpsUserInfo bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), RpsUserInfo.class);
//        rpsUserInfoRepository.set(RPS_USER_INFO.getValue(), bean);
//        rpsUserInfoRepository.expire(RPS_USER_INFO.getValue(), 30, TimeUnit.MINUTES);
        return bean;
    }


}
