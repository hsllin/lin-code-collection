package com.lin.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.DateFormatEnum;
import com.lin.bean.StockDocument;
import com.lin.bean.document.DocumentResultBean;
import com.lin.util.DateUtils;
import com.lin.util.WechatMessageUtils;
import com.ugrong.framework.redis.repository.cache.IStringRedisRepository;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lin.bean.EnumCacheType.DOCUMENT_CACHE;

@Service
@EnableScheduling
public class DocumentService {
    @Autowired
    private static IStringRedisRepository stringRedisRepository;

    public static void main(String[] args) throws UnsupportedEncodingException {
//        getNewDocumentInfo();
//        getDocumentInfo();
//        HttpRequest getUserReq = HttpUtil.createGet("https://portal.ly-sky.com:6202/lyuapServer/serviceValidate?ticket=ST-151640-1aa15475f3c84e3bbab5ee1f71bde653&service=http%3A%2F%2Fportal.ly-sky.com%3A9831%2Fwebconsole%2Flogin%2FlySso%2FrepairConsole");
//        System.out.println(getUserReq.execute().body());

        String token = "a1bc864955aaba9ba59b041c8eca9294a225c7de75e63d8ba977f515";
//        String url = "https://api.tushare.pro" + "/stock/basic?token=" + token + "&ts_code=";
//        String stockCodeurl="http://suggest3.sinajs.cn/suggest/type=11,12,13,14,15&key=600";
//        String boardurl =  "http://push2.eastmoney.com/api/qt/clist/get?pn=1&pz=10000&fs=m:90+t:2";
        String conceptUrl = "http://q.10jqka.com.cn/gn/index/field/addMarketTime/order/desc/page/2/ajax/1/" ;

        HttpClient client = HttpClient.newHttpClient();
        String result = HttpUtil.get(conceptUrl);

        System.out.println(result);
    }

    public static List<DocumentResultBean> getNewDocumentInfo() {
        List<DocumentResultBean> resultBeanList = new ArrayList<>();
        String url = "https://np-anotice-stock.eastmoney.com/api/security/ann";
        Map<String, Object> param = new HashMap<>();
        String yesterDay = DateUtils.getYesterday(DateFormatEnum.DATE);
//        String yesterDay = "2025-01-25";
        String tomorrow = DateUtils.getTomorrowTime(DateUtils.getNowTime(), DateFormatEnum.DATE);
        param.put("sr", "-1");
        param.put("page_size", "100");
        param.put("page_index", "1");

        param.put("ann_type", "SHA%2CCYB%2CSZA%2CBJA%2CINV");
        param.put("client_source", "web");
        param.put("s_node", "0");
        param.put("f_node", "0");
        param.put("begin_time", yesterDay);
        param.put("end_time", tomorrow);

        String result = HttpUtil.get(url, param);
        StockDocument stockDocument = JSONUtil.toBean(JSONUtil.toJsonStr(result), StockDocument.class);
        List<StockDocument.DataBean.ListBean> listBeanList = stockDocument.getData().getList();
        Integer total = stockDocument.getData().getTotal_hits();
        Integer totalPage = total / 100 + 1;
        if (totalPage > 1) {
            for (int i = 2; i <= totalPage; i++) {
                param.put("page_index", i);
                String tempResult = HttpUtil.get(url, param);
                StockDocument tempStockDocument = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), StockDocument.class);
                listBeanList.addAll(tempStockDocument.getData().getList());
            }
        }

        System.out.println(listBeanList.size());
//        String[] words = {"停牌", "复牌", "重组", "购买", "募集", "重大资产", "出售", "转让", "收购", "退市","受让","业绩预增"};
        for (StockDocument.DataBean.ListBean bean : listBeanList) {
            String stockCode = bean.getCodes().get(0).getStock_code();
            if (stockCode.startsWith("30") || stockCode.startsWith("83") || stockCode.startsWith("12")) {
                continue;
            }

            String title = bean.getDisplay_time() + " " + bean.getColumns().get(0).getColumn_name() + " " + bean.getCodes().get(0).getStock_code() + " " + bean.getTitle();
//            title.contains("停牌") || title.contains("复牌") || title.contains("重组") || title.contains("并购") ||
//            title.contains("购买")
            if (containsAny(title)) {
                String messageUrl = "https://data.eastmoney.com/notices/detail/" + bean.getCodes().get(0).getStock_code() + "/" + bean.getArt_code() + ".html";//
//                WechatMessageUtils.sendMessageToWechat(title, messageUrl);
                System.out.println(title);
                DocumentResultBean documentResultBean = new DocumentResultBean();
                documentResultBean.setCode(bean.getCodes().get(0).getStock_code());
                documentResultBean.setTime(DateUtils.transferTimeFormat(bean.getDisplay_time(),DateFormatEnum.DEFAULT) );
                documentResultBean.setType(bean.getColumns().get(0).getColumn_name());
                documentResultBean.setName(bean.getCodes().get(0).getShort_name());
                documentResultBean.setContent(bean.getTitle());
                documentResultBean.setUrl("https://data.eastmoney.com/notices/detail/" + bean.getCodes().get(0).getStock_code() + "/" + bean.getArt_code() + ".html");
                resultBeanList.add(documentResultBean);

            }
        }
        return resultBeanList;
    }

    //    @Scheduled(fixedDelay = 15000)
    public static void getDocumentInfo() {

        String url = "https://np-anotice-stock.eastmoney.com/api/security/ann";
        Map<String, Object> param = new HashMap<>();
        String yesterDay = DateUtils.getYesterday(DateFormatEnum.DATE);
        String tomorrow = DateUtils.getTomorrowTime(DateUtils.getNowTime(), DateFormatEnum.DATE);
//        param.put("cb", "jQuery1123006755088795545139_173727505367");
        param.put("sr", "-1");
        param.put("page_size", "100");
        param.put("page_index", "1");

        param.put("ann_type", "SHA%2CCYB%2CSZA%2CBJA%2CINV");
        param.put("client_source", "web");
        param.put("s_node", "0");
        param.put("f_node", "0");
        param.put("begin_time", yesterDay);
        param.put("end_time", tomorrow);

        String result = HttpUtil.get(url, param);
        StockDocument stockDocument = JSONUtil.toBean(JSONUtil.toJsonStr(result), StockDocument.class);
        List<StockDocument.DataBean.ListBean> listBeanList = stockDocument.getData().getList();
        Integer total = stockDocument.getData().getTotal_hits();
        System.out.println(total);
        Integer totalPage = total / 100 + 1;
        if (totalPage > 1) {
            for (int i = 2; i <= totalPage; i++) {
                param.put("page_index", i);
                String tempResult = HttpUtil.get(url, param);
                StockDocument tempStockDocument = JSONUtil.toBean(JSONUtil.toJsonStr(tempResult), StockDocument.class);
                listBeanList.addAll(tempStockDocument.getData().getList());
            }
        }

        System.out.println(listBeanList.size());
//        String[] words = {"停牌", "复牌", "重组", "购买", "重大资产", "出售", "转让", "收购", "退市","受让","业绩预增"};
        for (StockDocument.DataBean.ListBean bean : listBeanList) {
            stringRedisRepository.remove(DOCUMENT_CACHE);
            Optional<String> optional = stringRedisRepository.getString(DOCUMENT_CACHE, bean.getCodes().get(0).getStock_code());
//            if (!optional.isEmpty()) {
//                continue;
//            }
            String stockCode = bean.getCodes().get(0).getStock_code();
            if (stockCode.startsWith("30") || stockCode.startsWith("83") || stockCode.startsWith("12")) {
                continue;
            }
            String title = bean.getCodes().get(0).getStock_code() + " " + bean.getTitle();
//            title.contains("停牌") || title.contains("复牌") || title.contains("重组") || title.contains("并购") ||
//            title.contains("购买")
            if (containsAny(title)) {
                String messageUrl = "https://data.eastmoney.com/notices/detail/" + bean.getCodes().get(0).getStock_code() + "/" + bean.getArt_code() + ".html";//
                WechatMessageUtils.sendMessageToWechat(title, messageUrl);
                System.out.println(title);
            }
            stringRedisRepository.setStringValue(DOCUMENT_CACHE, bean.getCodes().get(0).getStock_code(), bean.getCodes().get(0).getStock_code());
            stringRedisRepository.expire(DOCUMENT_CACHE, bean.getCodes().get(0).getStock_code(), 1, TimeUnit.DAYS);

        }

    }

    public static void testss() {
        String url = "https://x-quote.cls.cn/quote/index/basic?app=CailianpressWeb&fields=preclose_px&os=web&secu_code=sh000001&sv=8.4.6";
        String result = HttpUtil.get(url);
        System.out.println(result);
    }

    public static boolean containsAny(String text) {
//        String[] keywords = {"停牌", "复牌", "重组", "购买", "重大资产", "出售", "转让", "收购", "风险", "退市", "扭亏", "盈利", "受让", "业绩预增", "预盈", "名称变更"};
        String[] keywords={"摘帽","重组","复牌","资产注入","收购","st风险","退市风险","控股权变更","资产注入","风险警示","实控人变更"};
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                text = "[" + keyword + "]" + text;
                return true;
            }
        }
        return false;
    }


}
