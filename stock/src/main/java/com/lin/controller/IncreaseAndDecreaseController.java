package com.lin.controller;

import com.lin.bean.tonghuashun.AutionTradingBean;
import com.lin.bean.tonghuashun.BigOrderBean;
import com.lin.bean.tonghuashun.IncreaseRankData;
import com.lin.bean.tonghuashun.LimitUpData;
import com.lin.service.BigOrderService;
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
import java.util.Map;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 获取同花顺涨跌幅信息
 * @create 2025-05-12 16:29
 */
@Controller
public class IncreaseAndDecreaseController {
    @Autowired
    IncreaseAndDecreaseService increaseAndDecreaseService;

    @RequestMapping("/getIncreaseData")
    public ResponseEntity<List<IncreaseRankData>> getIncreaseData(HttpServletRequest request, Model model) throws ScriptException, IOException, InterruptedException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getIncreaseData();
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/getDecreaseData")
    public ResponseEntity<List<IncreaseRankData>> getDecreaseData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getDecreaseData();
        return ResponseEntity.ok(list);
    }

    /**
     * 一字涨停
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getOneWordData")
    public ResponseEntity<List<IncreaseRankData>> getOneWordData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getOneWordData();
        return ResponseEntity.ok(list);
    }

    /**
     * T字涨停
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getTLimitupData")
    public ResponseEntity<List<IncreaseRankData>> getTLimitupData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getTLimitupData();
        return ResponseEntity.ok(list);
    }

    /**
     * 一字涨停
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getLeaderData")
    public ResponseEntity<List<IncreaseRankData>> getLeaderData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getLeaderData();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺趋势数据
     * 连续2日上涨；今日涨幅>2%；量比>1.2；  5日均线>10日均线>20日均线；  换手率>5%；流通市值<200亿>30亿；  非ST股；非停牌
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getTrendData")
    public ResponseEntity<List<IncreaseRankData>> getTrendData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getTrendData();
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/downloadTrendData")
    public ResponseEntity<Boolean> downloadTrendData(HttpServletRequest request, Model model) {
        increaseAndDecreaseService.downloadTrendData();
        return ResponseEntity.ok(true);
    }

    @RequestMapping("/generateTrendDataPhoto")
    public ResponseEntity<Boolean> generateTrendDataPhoto(HttpServletRequest request, Model model) {
        increaseAndDecreaseService.generateTrendDataPhoto();
        return ResponseEntity.ok(true);
    }

    /**
     * 获取同花顺个股热度数据
     * 热点个股前100；所属行业概念；所属行业;大单净量
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getTop100Data")
    public ResponseEntity<List<IncreaseRankData>> getTop100Data(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getTop100Data();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺昨日涨停数据
     * 热点个股前100；所属行业概念；所属行业;大单净量
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getYesterdayLimitUpData")
    public ResponseEntity<List<LimitUpData>> getLimitUpYesterDayData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        List<LimitUpData> list = increaseAndDecreaseService.getYesterdayLimitUpData();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺昨日涨停数据
     * 热点个股前100；所属行业概念；所属行业;大单净量
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getTodayLimitUpData")
    public ResponseEntity<List<LimitUpData>> getTodayLimitUpData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        List<LimitUpData> list = increaseAndDecreaseService.getTodayLimitUpData();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺早盘集合竞价
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getAutionTradingData")
    public ResponseEntity<List<AutionTradingBean>> getAutionTradingData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        List<AutionTradingBean> list = increaseAndDecreaseService.getAutionTradingData();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺早盘集合竞价未匹配大于1万手
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getAutionNoMatchedData")
    public ResponseEntity<List<AutionTradingBean>> getAutionNoMatchedData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        List<AutionTradingBean> list = increaseAndDecreaseService.getAutionNoMatchedData();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺板块和概念前排
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getHotConceptAndIndustry")
    public ResponseEntity<Map<String, List<IncreaseRankData>>> getHotConceptAndIndustry(HttpServletRequest request, Model model) throws ScriptException, IOException {
        Map<String, List<IncreaseRankData>> list = increaseAndDecreaseService.getHotConcept();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺成交额超10亿以上的数据
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getTradeData")
    public ResponseEntity<List<IncreaseRankData>> getTradeData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getTradeData();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺弱转强
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getWeakToStrongData")
    public ResponseEntity<List<IncreaseRankData>> getWeakToStrongData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getWeakToStrongData();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺龙头首阴
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getDragonToGreenData")
    public ResponseEntity<List<IncreaseRankData>> getDragonToGreenData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getDragonToGreenData();
        return ResponseEntity.ok(list);
    }
    /**
     * 获取同花顺60日新高
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getNewHighData")
    public ResponseEntity<List<IncreaseRankData>> getNewHighData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getNewHighData();
        return ResponseEntity.ok(list);
    }



    /**
     * 获取同花顺今日超昨日前高个股
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getIncreaseYesterdayData")
    public ResponseEntity<List<IncreaseRankData>> getIncreaseYesterdayData(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        List<IncreaseRankData> list = increaseAndDecreaseService.getIncreaseYesterdayData();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取同花顺涨幅板块
     *
     * @param request
     * @param model
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    @RequestMapping("/getIncreaseConcept")
    public ResponseEntity<List<IncreaseRankData>> getIncreaseConcept(HttpServletRequest request, Model model) throws ScriptException, IOException {
        String date = CommonUtils.getTradeDay(0);
        String sort = request.getParameter("sort");
        List<IncreaseRankData> list = increaseAndDecreaseService.getIncreaseConcept(sort);
        return ResponseEntity.ok(list);
    }




}