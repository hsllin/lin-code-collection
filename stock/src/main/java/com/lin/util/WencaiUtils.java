package com.lin.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.lin.bean.risefall.DayRiseBean;
import com.lin.service.StockInfoFetcherService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-23 15:53
 */
public class WencaiUtils {
    private static final List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    );
    private static final String url = "https://www.iwencai.com/unifiedwap/unified-wap/v2/result/get-robot-data";

    public static void main(String[] args) throws Exception {
        Map<String, Object> param = new HashMap<>();
        String result = "\"{\\\"dde\\\\u5927\\\\u5355\\\\u51c0\\\\u91cf\\\"";
        result = result.replace("\\\\u", "\\u");
        System.out.println(result);
//        System.out.println(generateHexinV());
//        getData(url, param);
//        getDataList(param);
    }

    public static Map<String, Object> getDataList(Map<String, Object> param) throws IOException, InterruptedException, ScriptException {
        String tradeDay = CommonUtils.getTradeDay(0);
        String url = " https://www.iwencai.com/gateway/urp/v7/landing/getDataList";
//        Map<String, Object> param = new HashMap<>();
        String condition = "\n" +
                "[{\"score\":0,\"node_type\":\"op\",\"chunkedResult\":\"个股涨幅榜显示行业和概念_&_涨停原因_&_大单净量\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"sonSize\":7,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"score\":0,\"ciChunk\":\"个股涨幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"and\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"\",\"uiText\":\"涨跌幅从大到小排名并且所属同花顺行业并且所属概念\",\"sonSize\":4,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"score\":0,\"ciChunk\":\"个股涨幅榜显示行业和概念\",\"node_type\":\"op\",\"children\":[],\"opName\":\"sort\",\"ci\":false,\"opPropertiesMap\":{},\"opProperty\":\"从大到小排名\",\"uiText\":\"涨跌幅从大到小排名\",\"sonSize\":1,\"opPropertyMap\":{},\"source\":\"text2sql\"},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"涨跌幅:前复权\",\"indexProperties\":[\"nodate 1\",\"交易日期 20250606\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250606\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"个股涨幅榜显示行业和概念\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨跌幅\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属同花顺行业\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股涨幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属同花顺行业\",\"valueType\":\"_所属同花顺行业\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":false,\"indexName\":\"所属概念\",\"indexProperties\":[],\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{},\"reportType\":\"null\",\"score\":0,\"ciChunk\":\"个股涨幅榜显示行业和概念\",\"node_type\":\"index\",\"domain\":\"abs_股票领域\",\"uiText\":\"所属概念\",\"valueType\":\"_所属概念\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"涨停原因类别\",\"indexProperties\":[\"nodate 1\",\"交易日期 20250606\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250606\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"涨停原因\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"涨停原因类别\",\"valueType\":\"_涨停原因类别\",\"sonSize\":0},{\"dateText\":\"\",\"ci\":true,\"indexName\":\"dde大单净量\",\"indexProperties\":[\"nodate 1\",\"交易日期 20250606\"],\"dateUnit\":\"日\",\"source\":\"text2sql\",\"type\":\"index\",\"indexPropertiesMap\":{\"交易日期\":\"20250606\",\"nodate\":\"1\"},\"reportType\":\"TRADE_DAILY\",\"score\":0,\"ciChunk\":\"大单净量\",\"createBy\":\"preCache\",\"node_type\":\"index\",\"dateType\":\"交易日期\",\"domain\":\"abs_股票领域\",\"uiText\":\"dde大单净量\",\"valueType\":\"_浮点型数值(%)\",\"sonSize\":0}]";
        param.put("query", "个股涨幅榜显示行业和概念;涨停原因");
        param.put("urp_sort_way", "desc");
//        param.put("urp_sort_index", "涨跌幅:前复权[20250606]");
        param.put("page", "1");
        param.put("perpage", "100");
//        param.put("condition", condition);
//        param.put("filter", "HS,GEM2STAR");
//        param.put("_", System.currentTimeMillis());
        param.put("question", "个股涨幅榜显示行业和概念;涨停原因");
//        param.put("source", "ths_mobile_iwencai");
//        param.put("user_id", "721659935");
//        param.put("user_name", "mx_721659935");
//        param.put("version", "2.0");
//        param.put("secondary_intent", "stock");
//        param.put("add_info", "{\"urp\":{\"scene\":3,\"company\":1,\"business\":1,\"is_lowcode\":1},\"contentType\":\"json\"}");
//        param.put("log_info", "{\"input_type\":\"click\"}");
//        param.put("rsh", "721659935");
        param.put("logid", "436438ba58126ecf7bab5eeff414ba67");
        param.put("ret", "json_all");
        param.put("sessionid", "436438ba58126ecf7bab5eeff414ba67");
        param.put("iwc_token", "0ac9880a17491921637324846");
        param.put("uuids[0]", "24087");
        param.put("comp_id", "6836372");
        param.put("user_id", "Ths_iwencai_Xuangu_ta7wv6968l1v5l2l8orrl4ohcg77279g");
        param.put("comp_id", "6836372");
        param.put("business_cat", "soniu");
        param.put("uuid", "24087");
        param.put("query_type", "stock");

        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));
        String hexinV = generateHexinV();

        String result = HttpRequest.post(url)
                .header("hexin-v", hexinV)// token鉴权
                .header("content-type", "application/x-www-form-urlencoded")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("cache-control", "no-cache")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://www.iwencai.com")
                .header("pragma", "no-cache")
                .header("referer", "https://www.iwencai.com/unifiedwap/result?w=%E5%A4%A7%E5%8D%95%E5%87%80%E9%87%8F&querytype=stock")
                .header("sec-ch-ua", "\"Chromium\";v=\"136\", \"Google Chrome\";v=\"136\", \"Not.A/Brand\";v=\"99\"")
                .header("cookie", "other_uid=Ths_iwencai_Xuangu_ta7wv6968l1v5l2l8orrl4ohcg77279g; ta_random_userid=cngo7x2see; cid=53f41f17e022c13ff3ca4103e57640601747925596; cid=53f41f17e022c13ff3ca4103e57640601747925596; ComputerID=53f41f17e022c13ff3ca4103e57640601747925596; WafStatus=0; wencai_pc_version=1; v=Axl_2nnjlcobdEleOKgg6awbKA72pgaN95IxCDvjlW-99zdwg_YdKIfqQenI")
                .form(param)
                .execute().body();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> temp = objectMapper.readValue(result, Map.class);
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.convertValue(result, JsonNode.class);
//        JsonNode txtNode = jsonNode.at("/answer/components").get(0).at("/data/datas");
//        List<Map<String, Object>> txtMap = mapper.convertValue(txtNode, List.class);
        return temp;
    }



    public static Map<String, Object> getData(String targetUrl, Map<String, Object> params) throws IOException, ScriptException {
        if (StringUtils.isBlank(targetUrl)) {
            targetUrl = url;
        }
        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));
        String hexinV = generateHexinV();
        params.put("hexin-v", hexinV);
        params.put("source", "ths_mobile_iwencai");
        params.put("user_id", "721659935");
        params.put("user_name", "mx_721659935");
        params.put("version", "2.0");
        params.put("secondary_intent", "stock");
        params.put("add_info", "{\"urp\":{\"scene\":3,\"company\":1,\"business\":1,\"is_lowcode\":1},\"contentType\":\"json\"}");
        params.put("log_info", "{\"input_type\":\"click\"}");
        params.put("rsh", "721659935");
        String result = HttpRequest.post(targetUrl)
                .header("hexin-v", hexinV)// token鉴权
                .header("content-type", "application/x-www-form-urlencoded")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://www.iwencai.com")
                .header("pragma", "no-cache")
                .header("referer", "https://www.iwencai.com/unifiedmobile/?q=%E6%B6%A8%E5%B9%85%E6%A6%9C&queryType=stock")
                .form(params)
                .execute().body();
//        result = JSONUtil.toJsonStr(result);
        ObjectMapper objectMapper = new ObjectMapper();
//        String output = StringEscapeUtils.unescapeJava(result);
        Map<String, Object> temp = objectMapper.readValue(result, Map.class);
//        result = result.replace("\\\\u", "\\u");
//        String output = StringEscapeUtils.unescapeJava(result);
        return temp;
    }

    public static Map<String, Object> getRootData(String targetUrl, Map<String, Object> params) throws IOException, ScriptException {
        if (StringUtils.isBlank(targetUrl)) {
            targetUrl = url;
        }
        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));
        String hexinV = generateHexinV();
        params.put("hexin-v", hexinV);
        params.put("source", "ths_mobile_iwencai");
        params.put("user_id", "721659935");
        params.put("user_name", "mx_721659935");
        params.put("version", "2.0");
        params.put("secondary_intent", "stock");
        params.put("add_info", "{\"urp\":{\"scene\":1,\"company\":1,\"business\":1},\"contentType\":\"json\",\"searchInfo\":true}");
        params.put("log_info", "{\"input_type\":\"typewrite\"}");
        params.put("rsh", "721659935");
        params.put("", "");
        String result = HttpRequest.post(targetUrl)
                .header("hexin-v", hexinV)// token鉴权
                .header("content-type", "application/json")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://www.iwencai.com")
                .header("pragma", "no-cache")
                .header("referer", "https://www.iwencai.com/unifiedmobile/?q=%E6%B6%A8%E5%B9%85%E6%A6%9C&queryType=stock")
                .body(JSONUtil.toJsonStr(params))
                .execute().body();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> temp = objectMapper.readValue(result, Map.class);
        return temp;
    }

    public static Map<String, Object> getConceptRootData(String targetUrl, Map<String, Object> params) throws IOException, ScriptException {
        if (StringUtils.isBlank(targetUrl)) {
            targetUrl = url;
        }
        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));
        String hexinV = generateHexinV();
        params.put("hexin-v", hexinV);
        params.put("source", "ths_mobile_iwencai");
        params.put("user_id", "721659935");
        params.put("user_name", "mx_721659935");
        params.put("version", "2.0");
        params.put("secondary_intent", "zhishu");
        params.put("add_info", "{\"urp\":{\"scene\":1,\"company\":1,\"business\":1},\"contentType\":\"json\",\"searchInfo\":true}");
        params.put("log_info", "{\"input_type\":\"typewrite\"}");
        params.put("rsh", "721659935");
        params.put("", "");
        String result = HttpRequest.post(targetUrl)
                .header("hexin-v", hexinV)// token鉴权
                .header("content-type", "application/json")// token鉴权
                .header("User-Agent", userAgent)
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("host", "www.iwencai.com")
                .header("origin", "https://www.iwencai.com")
                .header("pragma", "no-cache")
                .header("referer", "https://www.iwencai.com/unifiedmobile/?q=%E6%B6%A8%E5%B9%85%E6%A6%9C&queryType=stock")
                .body(JSONUtil.toJsonStr(params))
                .execute().body();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> temp = objectMapper.readValue(result, Map.class);
        return temp;
    }

    public static String generateHexinV() throws ScriptException {
        // 创建ScriptEngineManager
        ScriptEngineManager manager = new ScriptEngineManager();
        // 获取JavaScript引擎（Nashorn）
        ScriptEngine engine = manager.getEngineByName("nashorn");

        // 加载JavaScript代码（替换为实际的hexin-v生成逻辑）
        String jsCode = "function generateHexinV() {\n" +
                "    var TOKEN_SERVER_TIME = 1721028932.734;\n" +
                "// 补环境  ---------------------------------------------------------------------------------------\n" +
                "var document={}\n" +
                "var window={}\n" +
                "\n" +
                "// 全局对象 --------------------------------------------------------------------------------------\n" +
                "var target=this\n" +
                "!function(n,t){\n" +
                "    var r, e, a;\n" +
                "    r = e = a = n;\n" +
                "    var u, c, s;\n" +
                "    u = c = s = t;\n" +
                "    function v() {\n" +
                "        var n = arguments[s[0]];\n" +
                "        if (!n)\n" +
                "            return r[0];\n" +
                "        for (var t = u[1], o = a[1], i = c[2]; i < n.length; i++) {\n" +
                "            var v = n.charCodeAt(i)\n" +
                "                , f = v ^ o;\n" +
                "            o = v,\n" +
                "                t += e[2].fromCharCode(f)\n" +
                "        }\n" +
                "        return t\n" +
                "    }\n" +
                "    var f = c[3]\n" +
                "        , l = s[4]\n" +
                "        , p = Wn(e[3], r[4], s[5])\n" +
                "        , d = a[5]\n" +
                "        , h = Wn(c[6], s[7])\n" +
                "        , g = c[8]\n" +
                "        , w = c[9]\n" +
                "        , m = r[6]\n" +
                "        , I = u[10]\n" +
                "        , y = a[7]\n" +
                "        , _ = (s[11],\n" +
                "        c[12],\n" +
                "        s[13])\n" +
                "        , C = e[8]\n" +
                "        , E = u[14]\n" +
                "        , A = ot(e[9], e[10])\n" +
                "        , b = a[11]\n" +
                "        , T = u[15]\n" +
                "        , B = c[16]\n" +
                "        , R = r[12]\n" +
                "        , k = r[13]\n" +
                "        , S = s[17]\n" +
                "        , P = u[18]\n" +
                "        , M = Wn(s[19], s[20], u[21])\n" +
                "        , O = v(s[22], e[14])\n" +
                "        , D = s[23]\n" +
                "        , x = s[24]\n" +
                "        , N = u[25]\n" +
                "        , L = u[26]\n" +
                "        , W = Wn(s[27], r[15])\n" +
                "        , F = u[28]\n" +
                "        , Y = r[16]\n" +
                "        , j = a[17]\n" +
                "        , H = e[18]\n" +
                "        , $ = e[19]\n" +
                "        , U = r[20]\n" +
                "        , V = v(c[29], e[21], e[22])\n" +
                "        , X = s[30]\n" +
                "        , G = s[31]\n" +
                "        , K = s[32]\n" +
                "        , Q = s[33]\n" +
                "        , Z = r[23]\n" +
                "        , q = r[24]\n" +
                "        , z = v(u[12], u[34], s[35])\n" +
                "        , J = u[36]\n" +
                "        , nn = a[25]\n" +
                "        , tn = s[37]\n" +
                "        , rn = c[38]\n" +
                "        , en = r[26]\n" +
                "        , an = c[39]\n" +
                "        , on = s[40]\n" +
                "        , un = a[27]\n" +
                "        , cn = u[41]\n" +
                "        , sn = ot(s[42], c[43])\n" +
                "        , vn = r[28]\n" +
                "        , fn = u[8]\n" +
                "        , ln = s[44]\n" +
                "        , pn = a[29]\n" +
                "        , dn = s[45]\n" +
                "        , hn = a[30]\n" +
                "        , gn = c[46]\n" +
                "        , wn = a[31]\n" +
                "        , mn = a[32]\n" +
                "        , In = s[47]\n" +
                "        , yn = r[33]\n" +
                "        , _n = a[34]\n" +
                "        , Cn = c[48]\n" +
                "        , En = a[8]\n" +
                "        , An = v(a[35], s[49])\n" +
                "        , bn = c[50]\n" +
                "        , Tn = c[51]\n" +
                "        , Bn = at(r[36], s[52])\n" +
                "        , Rn = ot(r[37], e[38])\n" +
                "        , kn = e[39]\n" +
                "        , Sn = u[53]\n" +
                "        , Pn = r[40]\n" +
                "        , Mn = s[54]\n" +
                "        , On = s[55]\n" +
                "        , Dn = Wn(u[56], r[41], r[42])\n" +
                "        , xn = r[43]\n" +
                "        , Nn = u[57]\n" +
                "        , Ln = e[44];\n" +
                "    function Wn() {\n" +
                "        return arguments[u[0]].split(e[0]).reverse().join(c[1])\n" +
                "    }\n" +
                "    var Fn = r[45], Yn = Wn(c[58], e[46]), jn = v(s[59], u[60]), Hn = Wn(r[47], s[61]), $n = s[62], Un = s[63], Vn = u[2], Xn = [new u[27](r[48]), new u[27](c[64])], Gn = [new e[47](ot(a[49])), new u[27](ot(a[50], u[65]))], Kn = c[66][f + l] , Qn;\n" +
                "    // !function(n) {\n" +
                "    //     n[e[53]] = s[67];\n" +
                "    //     function t(n) {\n" +
                "    //         var t = r[51][u[68]]\n" +
                "    //           , o = u[69] + n + s[70]\n" +
                "    //           , i = t.indexOf(o);\n" +
                "    //         if (i == -e[54]) {\n" +
                "    //             if (o = n + c[70],\n" +
                "    //             t.substr(r[52], o.length) != o)\n" +
                "    //                 return;\n" +
                "    //             i = a[52]\n" +
                "    //         }\n" +
                "    //         var f = i + o[v(u[71], s[72])]\n" +
                "    //           , l = t.indexOf(r[55], f);\n" +
                "    //         return l == -a[54] && (l = t[a[56]]),\n" +
                "    //         t.substring(f, l)\n" +
                "    //     }\n" +
                "    //     n[a[57]] = f;\n" +
                "    //     function o(n, t, a, o, i) {\n" +
                "    //         var c = n + r[58] + t;\n" +
                "    //         o && (c += e[59] + o),\n" +
                "    //         i && (c += v(Jn, u[73], s[74]) + i),\n" +
                "    //         a && (c += u[75] + a),\n" +
                "    //         u[66][u[68]] = c\n" +
                "    //     }\n" +
                "    //     n[s[76]] = t;\n" +
                "    //     function i(n, t, r) {\n" +
                "    //         this.setCookie(n, u[1], u[77], t, r)\n" +
                "    //     }\n" +
                "    //     n[s[78]] = o;\n" +
                "    //     function f() {\n" +
                "    //         var t = a[60];\n" +
                "    //         this.setCookie(t, u[67]),\n" +
                "    //         this.getCookie(t) || (n[r[53]] = e[61]),\n" +
                "    //         this.delCookie(t)\n" +
                "    //     }\n" +
                "    //     n[Wn(N, r[62], c[79])] = i\n" +
                "    // }(Qn || (Qn = {}));\n" +
                "    var Zn;\n" +
                "    var qn = function() {\n" +
                "        var n, t, r;\n" +
                "        n = t = r = a;\n" +
                "        var e, o, i;\n" +
                "        e = o = i = s;\n" +
                "        var u = o[15]\n" +
                "            , c = o[102]\n" +
                "            , f = e[103];\n" +
                "        function l(r) {\n" +
                "            var a = o[102]\n" +
                "                , i = e[103];\n" +
                "            this[n[76]] = r;\n" +
                "            for (var u = t[52], c = r[a + g + i]; u < c; u++)\n" +
                "                this[u] = t[52]\n" +
                "        }\n" +
                "        return l[e[104]][w + m + I + u] = function() {\n" +
                "            for (var a = e[105], u = this[a + y], c = [], s = -e[0], v = o[2], f = u[r[56]]; v < f; v++)\n" +
                "                for (var l = this[v], p = u[v], d = s += p; c[d] = l & parseInt(t[77], n[78]),\n" +
                "                --p != r[52]; )\n" +
                "                    --d,\n" +
                "                        l >>= parseInt(n[79], i[106]);\n" +
                "            return c\n" +
                "        }\n" +
                "            ,\n" +
                "            l[v(t[80], t[81], b)][ot(i[107])] = function(n) {\n" +
                "                for (var r = e[8], a = this[ot(e[108], e[109])], o = t[52], u = e[2], s = a[c + r + f]; u < s; u++) {\n" +
                "                    var v = a[u]\n" +
                "                        , l = i[2];\n" +
                "                    do {\n" +
                "                        l = (l << t[82]) + n[o++]\n" +
                "                    } while (--v > t[52]);\n" +
                "                    this[u] = l >>> i[2]\n" +
                "                }\n" +
                "            }\n" +
                "            ,\n" +
                "            l\n" +
                "    }(), zn;\n" +
                "    !function(n) {\n" +
                "        var t = s[13]\n" +
                "            , o = c[53]\n" +
                "            , i = r[83]\n" +
                "            , f = r[84]\n" +
                "            , l = s[110]\n" +
                "            , d = r[85]\n" +
                "            , h = r[86];\n" +
                "        function g(n, a, o, i, u) {\n" +
                "            for (var c = s[13], v = r[87], f = n[s[111]]; a < f; )\n" +
                "                o[i++] = n[a++] ^ u & parseInt(c + v + t + _, r[88]),\n" +
                "                    u = ~(u * parseInt(e[89], e[82]))\n" +
                "        }\n" +
                "        function w(n) {\n" +
                "            for (var t = c[112], i = r[52], v = n[s[111]], f = []; i < v; ) {\n" +
                "                var l = n[i++] << parseInt(C + t, c[113]) | n[i++] << e[82] | n[i++];\n" +
                "                f.push(m.charAt(l >> parseInt(e[90], e[82])), m.charAt(l >> parseInt(s[114], e[78]) & parseInt(a[91], r[88])), m.charAt(l >> u[59] & parseInt(E + o, a[78])), m.charAt(l & parseInt(a[92], u[113])))\n" +
                "            }\n" +
                "            return f.join(e[0])\n" +
                "        }\n" +
                "        for (var m = at(u[115], s[116]), I = {}, y = u[2]; y < parseInt(i + A, e[93]); y++)\n" +
                "            I[m.charAt(y)] = y;\n" +
                "        function O(n) {\n" +
                "            var t, r, e;\n" +
                "            t = r = e = s;\n" +
                "            var o, i, u;\n" +
                "            o = i = u = a;\n" +
                "            for (var c = ot(i[94]), l = e[2], p = n[o[56]], d = []; l < p; ) {\n" +
                "                var h = I[n.charAt(l++)] << parseInt(at(t[117]), u[82]) | I[n.charAt(l++)] << parseInt(v(t[118], u[95], e[119]), o[88]) | I[n.charAt(l++)] << t[59] | I[n.charAt(l++)];\n" +
                "                d.push(h >> parseInt(e[120], t[106]), h >> parseInt(t[121], r[122]) & parseInt(f + b + c, t[106]), h & parseInt(o[96], u[88]))\n" +
                "            }\n" +
                "            return d\n" +
                "        }\n" +
                "        function D(n) {\n" +
                "            var t = O(n);\n" +
                "            if (rn,\n" +
                "                p,\n" +
                "            t[r[52]] != h)\n" +
                "                return error = T + B + l,\n" +
                "                    void 0;\n" +
                "            var a = t[c[0]]\n" +
                "                , o = [];\n" +
                "            return g(t, +parseInt(e[79], c[122]), o, +u[2], a),\n" +
                "                x(o) == a ? o : void 0\n" +
                "        }\n" +
                "        function x(n) {\n" +
                "            var t = o;\n" +
                "            t = Vn;\n" +
                "            for (var e = c[2], i = a[52], u = n[c[111]]; i < u; i++)\n" +
                "                e = (e << s[123]) - e + n[i];\n" +
                "            return e & parseInt(s[124], r[88])\n" +
                "        }\n" +
                "        function N(n) {\n" +
                "            var t = et\n" +
                "                , r = x(n)\n" +
                "                , e = [h, r];\n" +
                "            return g(n, +a[52], e, +a[88], r),\n" +
                "                t = P,\n" +
                "                w(e)\n" +
                "        }\n" +
                "        n[e[97]] = w,\n" +
                "            n[R + k + S] = O,\n" +
                "            n[u[125]] = N,\n" +
                "            n[d + P + M] = D\n" +
                "    }(zn || (zn = {}));\n" +
                "    var Jn;\n" +
                "    !function(n) {\n" +
                "        var t = Fn\n" +
                "            , o = at(c[126], a[98])\n" +
                "            , i = r[99]\n" +
                "            , f = v(U, u[127])\n" +
                "            , l = s[128]\n" +
                "            , p = ot(a[100])\n" +
                "            , d = r[5]\n" +
                "            , h = r[101]\n" +
                "            , g = ot(u[129])\n" +
                "            , w = s[130]\n" +
                "            , m = r[102]\n" +
                "            , C = a[103]\n" +
                "            , E = e[104];\n" +
                "        function A(n) {\n" +
                "            for (var t = (Tn,\n" +
                "                I,\n" +
                "                []), e = r[52]; e < n[c[111]]; e++)\n" +
                "                t.push(n.charCodeAt(e));\n" +
                "            return t\n" +
                "        }\n" +
                "        function b() {\n" +
                "            var n = new e[105];\n" +
                "            try {\n" +
                "                return time = s[52].now(),\n" +
                "                time / parseInt(c[131], a[88]) >>> c[2]\n" +
                "            } catch (t) {\n" +
                "                return time = n.getTime(),\n" +
                "                time / parseInt(s[121], s[84]) >>> r[52]\n" +
                "            }\n" +
                "        }\n" +
                "        function T(n) {\n" +
                "            var t = u[8]\n" +
                "                , o = {}\n" +
                "                , i = function(n, o) {\n" +
                "                var i = c[102], f, l, p, d;\n" +
                "                for (o = o.replace(s[132], u[1]),\n" +
                "                         o = o.substring(u[0], o[e[56]] - c[0]),\n" +
                "                         f = o.split(c[133]),\n" +
                "                         p = c[2]; p < f[i + t + O]; p++)\n" +
                "                    if (l = f[p].split(v(r[106], c[134])),\n" +
                "                    l && !(l[r[56]] < s[122])) {\n" +
                "                        for (d = r[88]; d < l[r[56]]; d++)\n" +
                "                            l[r[54]] = l[r[54]] + r[107] + l[d];\n" +
                "                        l[s[2]] = new r[47](c[135]).test(l[e[52]]) ? l[a[52]].substring(u[0], l[e[52]][D + x] - c[0]) : l[a[52]],\n" +
                "                            l[r[54]] = new a[47](c[135]).test(l[r[54]]) ? l[e[54]].substring(s[0], l[a[54]][a[56]] - u[0]) : l[s[0]],\n" +
                "                            n[l[c[2]]] = l[e[54]]\n" +
                "                    }\n" +
                "                return n\n" +
                "            };\n" +
                "            return new r[47](e[108]).test(n) && (o = i(o, n)),\n" +
                "                o\n" +
                "        }\n" +
                "        function B(n) {\n" +
                "            var t, e, a;\n" +
                "            t = e = a = c;\n" +
                "            var u, s, v;\n" +
                "            if (u = s = v = r,\n" +
                "            typeof n === ot(s[109], sn) && n[Wn(y, a[136], s[110])])\n" +
                "                try {\n" +
                "                    switch (parseInt(n[e[137]])) {\n" +
                "                        case parseInt(a[131], t[122]):\n" +
                "                            break;\n" +
                "                        case parseInt(v[111], s[78]):\n" +
                "                            top[e[138]][v[112]] = n[t[139]];\n" +
                "                            break;\n" +
                "                        case parseInt(u[113], e[122]):\n" +
                "                            top[o + i + N][e[140]] = n[s[114]];\n" +
                "                            break;\n" +
                "                        default:\n" +
                "                            break\n" +
                "                    }\n" +
                "                } catch (f) {}\n" +
                "        }\n" +
                "        function R(n, t, r) {\n" +
                "            var e, a, o;\n" +
                "            e = a = o = u,\n" +
                "                q ? n.addEventListener(t, r) : n.attachEvent(a[25] + t, r)\n" +
                "        }\n" +
                "        function k() {\n" +
                "            return Math.random() * parseInt(u[141], r[78]) >>> r[52]\n" +
                "        }\n" +
                "        function S(n, t) {\n" +
                "            var o = en\n" +
                "                , i = new r[47](e[115],a[116]);\n" +
                "            o = T;\n" +
                "            var s = new u[27](v(p, r[117], m));\n" +
                "            if (n) {\n" +
                "                var f = n.match(i);\n" +
                "                if (f) {\n" +
                "                    var l = f[u[0]];\n" +
                "                    return t && s.test(l) && (l = l.split(r[118]).pop().split(r[107])[c[2]]),\n" +
                "                        l\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        function P(n) {\n" +
                "            var t = mn\n" +
                "                , o = c[142]\n" +
                "                , i = r[119]\n" +
                "                , v = e[120]\n" +
                "                , I = a[121];\n" +
                "            if (!(n > e[122])) {\n" +
                "                n = n || a[52];\n" +
                "                var y = parseInt(u[143], e[78])\n" +
                "                    , _ = a[51].createElement(u[144]);\n" +
                "                t = Q,\n" +
                "                    _[c[145]] = location[r[123]] + a[124] + parseInt((new r[105]).getTime() / y) + (f + o + l),\n" +
                "                    _[e[125]] = function() {\n" +
                "                        Vn = u[0],\n" +
                "                            setTimeout(function() {\n" +
                "                                P(++n)\n" +
                "                            }, n * parseInt(e[126], u[84]))\n" +
                "                    }\n" +
                "                    ,\n" +
                "                    _[p + L + d] = _[s[146]] = function() {\n" +
                "                        var n, t, r;\n" +
                "                        n = t = r = a;\n" +
                "                        var e, o, u;\n" +
                "                        e = o = u = c;\n" +
                "                        var s = e[147];\n" +
                "                        this[t[127]] && this[i + W] !== o[148] && this[u[149]] !== o[150] && this[s + F + h] !== u[151] || (Vn = n[52],\n" +
                "                            _[e[152]] = _[g + w + v] = n[128])\n" +
                "                    }\n" +
                "                    ,\n" +
                "                    c[66][m + I].appendChild(_)\n" +
                "            }\n" +
                "        }\n" +
                "        function M(n) {\n" +
                "            var t, e, a;\n" +
                "            t = e = a = r;\n" +
                "            var o, i, u;\n" +
                "            return o = i = u = s,\n" +
                "                new u[27](t[129]).test(n)\n" +
                "        }\n" +
                "        function X() {\n" +
                "            var n = new s[52];\n" +
                "            return typeof TOKEN_SERVER_TIME == s[153] ? r[52] : (time = parseInt(TOKEN_SERVER_TIME),\n" +
                "                time)\n" +
                "        }\n" +
                "        function G(n) {\n" +
                "            var t, e, a;\n" +
                "            t = e = a = s;\n" +
                "            var o, i, u;\n" +
                "            o = i = u = r;\n" +
                "            for (var c = u[52], v = a[2], f = n[o[56]]; v < f; v++)\n" +
                "                c = (c << a[123]) - c + n.charCodeAt(v),\n" +
                "                    c >>>= o[52];\n" +
                "            return c\n" +
                "        }\n" +
                "        function K(n) {\n" +
                "            var t = new s[27](e[130],s[80]);\n" +
                "            if (n) {\n" +
                "                return n.match(t)\n" +
                "            }\n" +
                "        }\n" +
                "        function Z(n) {\n" +
                "            var t = new u[27](c[154]);\n" +
                "            if (M(n))\n" +
                "                return n;\n" +
                "            var o = t.test(n) ? -a[86] : -parseInt(r[79], e[88]);\n" +
                "            return (tn,\n" +
                "                _,\n" +
                "                n.split(s[155])).slice(o).join(a[131])\n" +
                "        }\n" +
                "        n[Y + C + j] = T,\n" +
                "            t = En,\n" +
                "            n[c[156]] = P,\n" +
                "            n[ot(u[157])] = B,\n" +
                "            n[r[132]] = A,\n" +
                "            n[c[158]] = G,\n" +
                "            n[c[159]] = k,\n" +
                "            n[r[133]] = M,\n" +
                "            n[s[160]] = Z,\n" +
                "            n[E + H] = S,\n" +
                "            n[$ + U] = K,\n" +
                "            n[s[161]] = z,\n" +
                "            n[s[162]] = b,\n" +
                "            n[r[134]] = X;\n" +
                "        var q = !!a[65][a[135]];\n" +
                "        function z(n) {\n" +
                "            for (var t = v(O, u[163]), a = e[136], o = s[2], i = n[V + t + a] - s[0]; i >= r[52]; i--)\n" +
                "                o = o << r[54] | +n[i];\n" +
                "            return o\n" +
                "        }\n" +
                "        n[u[164]] = R\n" +
                "    }(Jn || (Jn = {}));\n" +
                "    var nt;\n" +
                "    var tt;\n" +
                "    var rt;\n" +
                "    var et;\n" +
                "    function at() {\n" +
                "        var n, t, r;\n" +
                "        n = t = r = u;\n" +
                "        var a, o, i;\n" +
                "        a = o = i = e;\n" +
                "        var c = arguments[o[52]];\n" +
                "        if (!c)\n" +
                "            return t[1];\n" +
                "        for (var s = o[0], v = o[1], f = a[52]; f < c.length; f++) {\n" +
                "            var l = c.charCodeAt(f)\n" +
                "                , p = l ^ v;\n" +
                "            v = v * f % n[222] + o[200],\n" +
                "                s += i[2].fromCharCode(p)\n" +
                "        }\n" +
                "        return s\n" +
                "    }\n" +
                "    function ot() {\n" +
                "        var n, t, e;\n" +
                "        n = t = e = c;\n" +
                "        var a, o, i;\n" +
                "        a = o = i = r;\n" +
                "        var u = arguments[a[52]];\n" +
                "        if (!u)\n" +
                "            return o[0];\n" +
                "        for (var s = a[0], v = n[267], f = o[200], l = t[2]; l < u.length; l++) {\n" +
                "            var p = u.charCodeAt(l);\n" +
                "            f = (f + t[0]) % v.length,\n" +
                "                p ^= v.charCodeAt(f),\n" +
                "                s += i[2].fromCharCode(p)\n" +
                "        }\n" +
                "        return s\n" +
                "    }\n" +
                "    var it;\n" +
                "\n" +
                "    var rt;\n" +
                "    !function(n) {\n" +
                "        var t = e[87], o = a[8], i = e[8], f = s[215], l = r[52], p = s[0], d = parseInt(c[216], u[122]), h = e[86], g = u[217], w = u[123], m = e[165], I = parseInt(t + En, c[122]), y = parseInt(a[79], a[82]), _ = c[218], C = parseInt(a[193], e[82]), E = parseInt(o + i, r[78]), A = parseInt(u[219], s[122]), b = parseInt(f + An, s[106]), T = parseInt(r[194], s[106]), B = parseInt(ot(s[220], e[195]), r[82]), R = parseInt(e[196], u[122]), k = parseInt(e[197], a[78]), S;\n" +
                "        function P() {\n" +
                "            var n = s[0]\n" +
                "                , t = r[88]\n" +
                "                , e = parseInt(u[13], c[122])\n" +
                "                , a = s[217];\n" +
                "            S = new qn([a, a, a, a, n, n, n, e, t, t, t, t, t, t, t, a, t, n]),\n" +
                "                S[p] = Jn.serverTimeNow(),\n" +
                "                M(),\n" +
                "                S[B] = Vn,\n" +
                "                S[k] = Un,\n" +
                "                S[R] = c[2],\n" +
                "                S[h] = \"4173916359\",\n" +
                "                S[b] = \"3748\",\n" +
                "                S[g] = \"1\",\n" +
                "                S[w] = \"10\",\n" +
                "                S[m] = \"5\"\n" +
                "        }\n" +
                "        function M() {\n" +
                "            // var n = Qn.getCookie(Fn) || Zn.get(jn);\n" +
                "            // if (n && n[s[111]] == parseInt(c[221], e[93])) {\n" +
                "            //     var t = zn.decode(n);\n" +
                "            //     if (t && (S.decodeBuffer(t),\n" +
                "            //     S[l] != s[2]))\n" +
                "            //         return\n" +
                "            // }\n" +
                "            //模拟第一次游客登陆  ---------------------------------------------------------------------------------------------\n" +
                "            S[l] = Jn.random()\n" +
                "        }\n" +
                "        function O() {\n" +
                "            S[R]++,\n" +
                "                S[p] = Jn.serverTimeNow(),\n" +
                "                S[d] = Jn.timeNow(),\n" +
                "                S[B] = Vn,\n" +
                "                S[I] = \"440\",\n" +
                "                S[y] = \"1\",\n" +
                "                S[_] = \"0\",\n" +
                "                S[C] = \"0\",\n" +
                "                S[E] = \"1011\",\n" +
                "                S[A] = \"25\";\n" +
                "            var n = S.toBuffer();\n" +
                "            return zn.encode(n)\n" +
                "        }\n" +
                "        n[e[57]] = P;\n" +
                "        // ---------------------------------------------------------------------------------------------------------------------------------\n" +
                "        P()\n" +
                "        function D() {\n" +
                "            return O()\n" +
                "        }\n" +
                "        target.num = D\n" +
                "        n[v(an, a[198], r[199])] = D\n" +
                "    }(rt || (rt = {}));\n" +
                "}([\"\", 9527, String, Boolean, \"eh\", \"ad\", \"Bu\", \"ileds\", \"1\", \"\\b\", Array, \"7\", \"base\", \"64De\", \"\\u2543\\u252b\", \"etatS\", \"pa\", \"e\", \"FromUrl\", \"getOrigi\", \"nFromUrl\", \"\\u255b\\u253e\", \"b?\\x18q)\", \"ic\", \"k\", \"sted\", \"he\", \"wser\", \"oNo\", \"ckw\", \"ent\", \"hst\", \"^And\", \"RM\", \"systemL\", 5, \"\\u255f\\u0978\\u095b\\u09f5\", \"TR8\", \"!'\", \"gth\", \"er\", \"TP\", 83, \"r\", !0, \"v\", \"v-nixeh\", RegExp, \"thsi.cn\", 'K\\x19\"]K^xVV', \"KXxAPD?\\x1b[Y\", document, 0, \"allow\", 1, \"; \", \"length\", \"Init\", \"=\", \"; domain=\", \"checkcookie\", !1, \"eikooCled\", \"tnemucod\", \"d\", window, \"\\u2553\\u0972\\u0959\\u09e4\\u09bd\\u0938\\u0980\\u09c5\\u09b1\\u09d1\\u09a7\\u09dc\\u09dd\\u09d3\\u09c2\", \"\\u2556\\u0979\\u095e\\u09d3\\u09b5\\u0935\\u098f\\u09c7\\u099d\\u09d2\\u09b0\", 23, \"l$P$~\", \"frames\", \"ducument\", \"ydob\", \"documentElement\", \"del\", \"@[\\\\]^`{|}~]\", \"base_fileds\", \"255\", 10, \"10\", 39, \"\\u2547\\u2535\\u255a\\u252e\\u2541\\u2535\\u254c\\u253c\\u2559\", 8, \"4\", \"3\", \"de\", 3, \"11\", 2, \"203\", \"22\", \"111111\", \"3f\", 16, \"\\x0f\", \"\\u2506\\u2537\\u2507\\u2537\", \"11111111\", \"base64Encode\", \"v\\x1d\", \"ati\", \"WY\", \"te\", \"bo\", \"rs\", \"getHost\", Date, \"{DF\", \":\", \"^{.*}$\", \"WU<P[C\", 52, \"1001\", \"href\", \"1111101010\", \"redirect_url\", \"^\\\\s*(?:https?:)?\\\\/{2,}([^\\\\/\\\\?\\\\#\\\\\\\\]+)\", \"i\", \"\\u256c\\u252c\\u2516\\u254b\", \"@\", \"ready\", \"change\", \"dy\", 7, \"protocol\", \"//s.thsi.cn/js/chameleon/time.1\", \"onerror\", \"2000\", \"readyState\", null, \"^(\\\\d+\\\\.)+\\\\d+$\", \"^\\\\s*(?:(https?:))?\\\\/{2,}([^\\\\/\\\\?\\\\#\\\\\\\\]+)\", \".\", \"strToBytes\", \"isIPAddr\", \"serverTimeNow\", \"addEventListener\", \"th\", \"wh\", \"Scro\", \"mousemove\", 55, \"evomhcuot\", \"[[?PVC\\x0e\", \"getMouseMove\", '_R\"xWB%Po_3YT', \"getMouseClick\", \"ght\", \"gin\", \"msD\", \"ack\", \"\\u2556\\u096b\\u095f\", \"Nativ\", \"^A\", \"MozSettingsEvent\", \"safari\", \"ActiveXObject\", \"postMessage\", \"Uint8Array\", \"WeakMap\", \"Google Inc.\", \"vendor\", \"chrome\", \"python\", \"sgAppName\", \"JX\", 6, \"me\", \"LBBROWSER\", \"w4\", \"2345Explorer\", \"TheWorld\", \"\\u2544\", 40, \"tTr\", \"\\u2506\", \"navigator\", \"webdriver\", \"languages\", \"taborcA|FDP\", \"\\u2541\\u097c\\u0949\", 95, \"1e0\", \"e Cli\", \"iso-8859-1\", \"defaultCharset\", \"localStorage\", \"^Win64\", \"^Linux armv|Android\", \"^iPhone\", \"^iPad\", \"B_{VV\", \"getPluginNum\", \"getBrowserFeature\", \"12\", \"16\", \"sE\", \"10000\", \"17\", \"\\u2542\\u2532\\u2556\\u2537\\u2543\\u2526\", \"\\x1cx`R\", 2333, \"XMLH\", \"ers\", \"0\", \"lo\", 57, \"ylppa\", \"error\", \"target\", \"click\", \"unload\", \"HE9AWT9Y\", \"\\\\.\", \"c?\", \"$\", \"/\", \"fetch\", \"prototype\", \"url\", \"\\u2556\\u0971\\u0956\\u09fe\\u09a7\", \"headers\", \"\\u256b\\u2554\", 79, \"?\", \"^(.*?):[ \\\\t]*([^\\\\r\\\\n]*)\\\\r?$\", \"gm\", \"s\", \"src\", \"analysisRst\", \"\\u255e\\u0973\\u0949\\u09f4\\u09a2\\u0929\\u09ac\\u09d4\\u0992\\u09d2\\u09b0\\u09d4\", \"appendChild\", \"Y\", \"jsonp_ignore\", \"^\", 70, \"421\", \"XH>a\", \"\\u2574\\u253c\\u257d\\u2530\\u2575\\u2539\\u257c\\u2533\\u257d\\u2522\\u256e\\u2521\\u2560\\u2524\\u2561\\u2525\", \"CHAMELEON_LOADED\"], [1, \"\", 0, \"he\", \"ad\", 29, \"\\x180G\\x1f\", \"?>=<;:\\\\\\\\/,+\", \"ng\", \"to\", \"ff\", Number, Error, \"11\", \"6\", \"er\", \"ro\", \"code\", \"co\", \"_?L\", \"ed\", \"@S\\x15D*\", Object, \"len\", \"gth\", \"on\", \"lo\", RegExp, \"ySta\", 13, \"eel\", \"ee\", \"ouse\", \"ll\", \"\\u2544\\u2530\\u2555\\u2531\", \"FCm-\", \"isTru\", \"getC\", \"Pos\", \"ve\", \"or\", \"ae\", \"^\", \"On\", \"Sho\", \"can\", \"ont\", \"roid\", \"anguage\", \"\\u2502\", \"ta\", \"tna\", Date, \"3\", \"am\", \"e\", \"n+\", \"f80\", \"\\x1dD\", 6, \"\\u255f\\u253a\\u2542\\u252b\\u2545\\u2568\\u251e\", \"KCABLLAC_NOELEMAHC\", \"X-Antispider-Message\", 3, \".baidu.\", Function, document, !0, \"cookie\", \"; \", \"=\", 96, \"\\u255b\\u253e\\u2550\\u2537\\u2543\\u252b\", \"\\u250c\\u252c\\u255c\\u253d\\u2549\\u2521\\u251c\", \";O\", \"; expires=\", \"getCookie\", \"Thu, 01 Jan 1970 00:00:00 GMT\", \"setCookie\", \"Z\\x18|\", \"i\", \"\\u255b\\u2534\\u2557\\u2536\\u255a\\u2509\\u257d\\u2512\\u2560\\u2501\\u2566\\u2503\", 52, window, 10, \"Init\", !1, \"set\", \"v\", \"eliflmth\", '<script>document.w=window<\\/script><iframe src=\"/favicon.icon\"></iframe>', \"iS.p\", \"head\", \"#default#userData\", \"get\", \"[!\\\"#$%&'()*\", \"g\", \"^d\", \"$D\", \"\\u2568\\u2537\\u2568\\u254c\\u256a\", \"]\\\\P\", \"___\", \"le\", \"th\", \"prototype\", \"base_f\", 8, \"\\\\R5Z\\\\R\\x14@^Q3G\", \"ZV%PgQ?Y]S%\", 67, \"r\", \"length\", \"0\", 16, \"12\", \"\\u2576\\u095f\\u0979\\u09d5\\u0995\\u091b\\u09a9\\u09f9\\u09bd\\u09f7\\u0989\\u09fd\\u09f5\\u09f3\\u09f9\\u0a41\\u0a4d\\u098f\\u0999\\u0905\\u0975\\u09cb\\u09a9\\u09a9\\u099d\\u0927\\u0933\\u0913\\u0a6b\\u0999\\u09a3\\u0937\\u098b\\u09f5\\u0933\\u0a7b\\u091b\\u09b1\\u0a63\\u095f\\u09fb\\u094d\\u0993\\u0943\\u092b\\u0949\\u09a3\\u09e7\\u09cb\\u0925\\u0993\\u09ab\\u09f0\\u092c\\u092c\\u0942\\u0950\\u09c8\\u0944\\u09c6\\u0990\\u0944\\u09cb\\u098e\", \"i,\", \"\\u2505\\u092f\", 12, 56, \"20\", \"1000\", 2, 5, \"11111111\", \"encode\", \"\\u255b\\u0972\\u0959\", \"\\u2519\", \"s\", \"WY$PYS\", \"ystate\", \"1111101000\", / /g, \",\", \"\\u250d\", '^\".*\"$', \"edoc_sutats\", \"status_code\", \"location\", \"redirect_url\", \"href\", \"4294967295\", \"j\", \"1200000\", \"script\", \"src\", \"onreadystatechange\", \"read\", \"loaded\", \"readyState\", \"complete\", \"interactive\", \"onload\", \"undefined\", \"\\\\.com\\\\.cn$|\\\\.com\\\\.hk$\", \".\", \"getServerTime\", 'YY7YAD?FjD\"', \"strhash\", \"random\", \"getRootDomain\", \"booleanToDecimal\", \"timeNow\", \"\\u2559\\u253e\", \"eventBind\", \"onwh\", \"\\u255b\", 46, \"DOMM\", \"cl\", \"T^5^\", \"div\", \"onmousewheel\", \"mousewheel\", 51, \"keydown\", \"clientY\", \"getKeyDown\", \"ch\", \"plu\", \"\\u2543\\u252b\", \"ouc\", \"art\", \"^i\", \"Po\", \"callPhantom\", \"max\", \"Hei\", \"ActiveXObject\", \"nd\", \"yG&Y]\\x17\\x15ZUG#A]Ez\\x15qY5\\x1b\", \"\\u2576\\u097e\\u094e\\u09f8\\u09a6\\u0938\\u09b6\\u09fe\\u0996\\u09d7\\u09a7\\u09d2\\u09cc\", \"Maxthon\", \"Q\", \"opr\", \"chrome\", \"BIDUBrowser\", \"QQBro\", \"[_$ZUR\", \"UBrowser\", \"MSGesture\", \"plugins\", \"doNotTrack\", \"ShockwaveFlash.ShockwaveFlash\", \"]C|\\x18\", \"webgl2\", \"platform\", \"name\", \"^Win32\", \"^MacIntel\", \"^Linux [ix]\\\\d+\", \"^BlackBerry\", \"language\", \"getPlatform\", \"getBrowserIndex\", \"1\", \"10\", 4, 9, \"1100\", \"\\t\\0\", \"3c\", 256, \"w\", \"TTP\", \"et\", \"c\", \"al\", \"\\u255e\", \"base\", \"\\u2569\\u0975\\u094e\\u09e5\\u09a0\\u092e\\u09d1\\u09ed\\u09ce\", \"target\", \"fh%PTQr\", \"#\", \"\\u255f\\u097c\\u0949\\u09f9\", 97, \"rg\", \"tnemelEcrs\", \"fn_Ws\", \"parentNode\", \"tagName\", \"A\", \"submit\", \"PX%\", \"me\", \"host\", \"\\\\.?\", \"d\\x19\", \"Fri, 01 Feb 2050 00:00:00 GMT\", \"]E%\", \"toString\", \"[object Request]\", \"headers\", 83, \"&\", encodeURIComponent, \"open\", \"getAllResponseHeaders\", \"4\", \"tseuqeRpttHLMX\", \"Window\", \"\\u2564\\u095e\", \"RI\", \"\\u2550\\u0953\", \"(YaZ\", \"_\", \"_str\", \"V587\"])\n" +
                "\n" +
                "    return target.num();\n" +
                "}"; // 简单哈希示例，实际逻辑需替换
        engine.eval(jsCode);

        // 生成时间戳和随机数
        long timestamp = System.currentTimeMillis();
        double random = Math.random();

        // 调用JavaScript函数生成hexin-v
        Object hexinV = engine.eval("generateHexinV(" + timestamp + ", " + random + ")");

        // 返回hexin-v
        return hexinV.toString();
    }
}