package com.lin.controller;

import com.lin.bean.pluginking.LiveStreamingBean;
import com.lin.bean.tonghuashun.BigOrderBean;
import com.lin.service.BigOrderService;
import com.lin.service.PluginKingService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 获取插件王里面的信息
 * @create 2025-05-12 16:29
 */
@Controller
public class PluginKingController {
    @Autowired
    PluginKingService pluginKingService;

    @RequestMapping("/getLiveStreamingData")
    public ResponseEntity<String> list(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String data = pluginKingService.getLiveStreamingData();
        return ResponseEntity.ok(data);
    }
}