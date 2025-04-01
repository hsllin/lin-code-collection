package com.lin.controller;

import com.lin.bean.IndustryAnalyzeBean;
import com.lin.service.IndustryAnalyzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndustryAnalyzeController {
    @Autowired
    IndustryAnalyzeService industryAnalyzeService;

    @RequestMapping("/getIndustryAnalyzeData")
    public ResponseEntity<IndustryAnalyzeBean> list(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        IndustryAnalyzeBean bean = industryAnalyzeService.getIndustryAnalyzeData();

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(bean);
    }
}
