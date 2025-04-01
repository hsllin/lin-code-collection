package com.lin.controller;

import com.lin.bean.DateFormatEnum;
import com.lin.bean.lianban.LianBanBean;
import com.lin.service.LianBanChiService;
import com.lin.util.CommonUtils;
import com.lin.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LianBanController {
    @Autowired
    LianBanChiService lianBanChiService;

    /**
     * 展示三天的连板信息
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getLianBanChiList")
    public ResponseEntity<List<LianBanBean>> getLianBanChiList(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        String today = CommonUtils.getTradeDay(0);
        String yesterDay = CommonUtils.getTradeDay(1);
        String preDay = CommonUtils.getTradeDay(2);
        List<LianBanBean.DataBean> todayList = lianBanChiService.lianBan(today);
        List<LianBanBean.DataBean> yesterDayList = lianBanChiService.lianBan(yesterDay);
        List<LianBanBean.DataBean> preDayList = lianBanChiService.lianBan(preDay);
        List<LianBanBean> resultList = new ArrayList<>();
        LianBanBean todayBean = new LianBanBean();
        todayBean.setDate(DateUtils.transferFormatTime(today, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE));
        todayBean.setData(todayList);

        LianBanBean yesterDayBean = new LianBanBean();
        yesterDayBean.setDate(DateUtils.transferFormatTime(yesterDay, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE));
        yesterDayBean.setData(yesterDayList);

        LianBanBean preDayBean = new LianBanBean();
        preDayBean.setDate(DateUtils.transferFormatTime(preDay, DateFormatEnum.DATE_WITH_OUT_LINE, DateFormatEnum.DATE));
        preDayBean.setData(preDayList);

        resultList.add(todayBean);
        resultList.add(yesterDayBean);
        resultList.add(preDayBean);


//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(resultList);
    }

    @RequestMapping("/downloadLianBanData")
    public ResponseEntity<Boolean> downloadData(HttpServletRequest request, Model model) {
        String dateIndex = request.getParameter("dateIndex");
        String date = CommonUtils.getTradeDay(Integer.valueOf(dateIndex));
        lianBanChiService.downloadData(date);

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(true);
    }


}
