package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.DecryptRequest;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.ShengKaoDataBean;
import com.lin.bean.StockRule;
import com.lin.service.ShengKaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 股票板块
 */
@Controller
public class ShengKaoController {
    @Autowired
    ShengKaoService shengKaoService;

    @RequestMapping("/sysnAllShengKaoData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> sysnAllShengKaoData(HttpServletRequest request, Model model) {
        shengKaoService.sysnAllShengKaoData();
        return ResponseEntity.ok(true);
    }

    @RequestMapping("/getShengKaoDataList")
    @ResponseBody
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<List<ShengKaoDataBean>> getShengKaoDataList(HttpServletRequest request) {
        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");
        List<ShengKaoDataBean> result = shengKaoService.getShengKaoDataList(type, keyword);
        return ResponseEntity.ok(result);
    }
}
