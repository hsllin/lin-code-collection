package com.lin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.bean.xuangutong.XuanGuTongBoardBean;
import com.lin.bean.xuangutong.XuanGuTongCardBean;
import com.lin.service.IncreaseAndDecreaseService;
import com.lin.service.IndustryAndBoardService;
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
import java.util.stream.Collectors;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 获取选股通的板块涨幅
 * @create 2025-05-12 16:29
 */
@Controller
public class BoardAndConceptsController {
    @Autowired
    IndustryAndBoardService industryAndBoardService;

    @RequestMapping("/getBoardAndConcepts")
    public ResponseEntity<List<XuanGuTongBoardBean>> getIncreaseData(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        List<String> codeList = industryAndBoardService.getRankCodeList();
        if (CollectionUtil.isNotEmpty(codeList)) {
            //拿50个吧
            codeList = codeList.subList(0, 50);
        }
        List<XuanGuTongBoardBean> list = industryAndBoardService.getBoardAndConceptList(codeList);
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/getBoardCardList")
    public ResponseEntity<List<XuanGuTongCardBean.DataDTO.ItemsDTO>> getBoardCardList(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        List<XuanGuTongCardBean.DataDTO.ItemsDTO> list = industryAndBoardService.getBoardCardList();
        return ResponseEntity.ok(list);
    }


}