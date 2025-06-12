package com.lin.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.TodayHotBean;
import com.lin.bean.lianban.LimitUpPoolBean;
import com.lin.bean.lianban.LimitUpPoolResultBean;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 来源东方财富，https://vipmoney.eastmoney.com/collect/watchstock/focus.html?appfenxiang=1
 * @create 2025-05-20 11:00
 */
@Service
public class TodayHotService {
    public static void main(String[] args) {
        getTodayHotData();
    }

    public static List<TodayHotBean.DataDTO> getTodayHotData() {
        String date = CommonUtils.getTradeDay(0);
        List<TodayHotBean.DataDTO> resultList = new ArrayList<>();
        String url = "https://emcfgdata.eastmoney.com/api/themeInvest/getFryTomorrowList";
        Map<String, Object> param = new HashMap<>();
        ArgsBean argsBean = new ArgsBean();
        argsBean.setLastTradeDate("");
        argsBean.setPageSize(10);
        param.put("args", argsBean);
        param.put("client", "wap");
        param.put("clientType", "cfw");
        param.put("clientVersion", "9001");
        param.put("randomCode", "16876811901271687682191747709426356");
        param.put("timestamp", System.currentTimeMillis());

        String result = HttpUtil.post(url, JSONUtil.toJsonStr(param));
        TodayHotBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), TodayHotBean.class);
        resultList = bean.getData();
        return resultList;
    }

    @Data
    public static class ArgsBean {
        String lastTradeDate;
        Integer pageSize;
    }
}