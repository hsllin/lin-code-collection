package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.bean.RatioRankingResultBean;
import com.lin.service.RatioRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取量比排行
 */
@Controller
public class RatioRankingController {
    @Autowired
    RatioRankingService ratioRankingService;

    @Cacheable(key = "ratioRanking:default", type = Cacheable.CacheType.STOCK_DATA)
    @RequestMapping("/getRatioRanking")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<RatioRankingResultBean> list(HttpServletRequest request, Model model) {
        String type = request.getParameter("type");
        RatioRankingResultBean bean = ratioRankingService.getRatioRanking();

//        CollectionUtil.reverse(list);
        return ResponseEntity.ok(bean);
    }
}
