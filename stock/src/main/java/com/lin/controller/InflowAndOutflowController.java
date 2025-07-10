package com.lin.controller;

import com.lin.bean.jfzt.InflowAndOutFlowBean;
import com.lin.bean.jfzt.InflowAndOutFlowRankBean;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.service.InAndOutFlowService;
import com.lin.service.IncreaseAndDecreaseService;
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
 * @Description 获取九方智投信息
 * @create 2025-05-12 16:29
 */
@Controller
public class InflowAndOutflowController {
    @Autowired
    InAndOutFlowService inAndOutFlowService;

    @RequestMapping("/getInflowAndOutFlowListData")
    public ResponseEntity<List<InflowAndOutFlowBean.DataDTO.ListDTO>> getInflowAndOutFlowListData(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String date = CommonUtils.getTradeDay(0);
        List<InflowAndOutFlowBean.DataDTO.ListDTO> list = inAndOutFlowService.getInflowAndOutFlowListData();
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/getInflowAndOutFlowRankData")
    public ResponseEntity<List<InflowAndOutFlowRankBean.DataDTO.ListDTO>> getInflowAndOutFlowRankData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<InflowAndOutFlowRankBean.DataDTO.ListDTO> list = inAndOutFlowService.getInflowAndOutFlowRankData();
        return ResponseEntity.ok(list);
    }
}