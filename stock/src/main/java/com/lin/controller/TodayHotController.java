package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.bean.TodayHotBean;
import com.lin.bean.lianban.ZhaBanBean;
import com.lin.service.TodayHotService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 获取同花顺异动信息
 * @create 2025-05-12 16:29
 */
@Controller
public class TodayHotController {
    @Autowired
    TodayHotService todayHotService;

    @RequestMapping("/getDayHotData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<TodayHotBean.DataDTO>> list(HttpServletRequest request) {
        String date = CommonUtils.getTradeDay(0);
        List<TodayHotBean.DataDTO> list = todayHotService.getTodayHotData();
        return ResponseEntity.ok(list);
    }
}