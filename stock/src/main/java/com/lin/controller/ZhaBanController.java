package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.bean.YiDong;
import com.lin.bean.lianban.ZhaBanBean;
import com.lin.service.YidongService;
import com.lin.service.ZhaBanChiService;
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
public class ZhaBanController {
    @Autowired
    ZhaBanChiService zhaBanChiService;

    @RequestMapping("/getZhaBanData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<ZhaBanBean.DataDTO.InfoDTO>> list(HttpServletRequest request, Model model) {
        String date = CommonUtils.getTradeDay(0);
        List<ZhaBanBean.DataDTO.InfoDTO> list = zhaBanChiService.getZhaBanData(date);
        return ResponseEntity.ok(list);
    }
}