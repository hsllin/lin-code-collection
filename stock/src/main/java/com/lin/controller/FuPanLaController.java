package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.service.FupanlaService;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 获取同花顺大单净量信息
 * @create 2025-05-12 16:29
 */
@Controller
public class FuPanLaController {
    @Autowired
    FupanlaService fupanlaService;

    @GetMapping(value = "getFuPanLaData", produces = "text/html;charset=utf-8")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<String> getBigOrderData(HttpServletRequest request, HttpServletResponse response) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String result = fupanlaService.getFupanData();
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        return ResponseEntity.ok(result);
    }
}