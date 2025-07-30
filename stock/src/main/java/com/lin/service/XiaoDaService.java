package com.lin.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.*;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-07-21 11:49
 */
@Service
public class XiaoDaService {
    private static final List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    );

    public static void main(String[] args) throws ScriptException, IOException {
        getData();
    }

    public static Map<String, Object> getData() throws IOException, ScriptException {
        Map<String, Object> params = new HashMap<>();
        String targetUrl = "https://wenda.tdx.com.cn/TQL?Entry=NLPSE.NLPQuery&RI=6C07";
        String userAgent = USER_AGENTS.get(RandomUtil.randomInt(USER_AGENTS.size()));
        params.put("nlpse_id", "7529350816923139132");
        params.put("POS", 0);
        params.put("COUNT", 30);
        params.put("order_field", "");
        params.put("dynamic_order", "");
        params.put("timestamps", 0);
        params.put("op_flag", 1);
        params.put("RANG", "AG");
        params.put("screen_type", 1);
        params.put("forward", 1);
        params.put("order_flag", "");

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(params);
        String result = HttpRequest.post(targetUrl)
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")// token鉴权
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "keep-alive")
                .header("cookie", "Hm_lvt_5c4c948b141e4d66943a8430c3d600d0=1751443540,1751466420; LST=10; ASPSessionID=1809852297270730514")
                .header("host", "https://wenda.tdx.com.cn")
                .header("content-length", "176")
                .header("origin", "https://wenda.tdx.com.cn")
                .header("pragma", "no-cache")
                .header("referer", "https://wenda.tdx.com.cn/")
                .form("[{\"nlpse_id\":\"7529242283099616505\",\"POS\":0,\"COUNT\":30,\"order_field\":\"\",\"dynamic_order\":\"\",\"order_flag\":\"\",\"timestamps\":0,\"op_flag\":1,\"screen_type\":1,\"RANG\":\"AG\",\"forward\":\"1\"}]")
                .execute().body();
//        result = JSONUtil.toJsonStr(result);
        ObjectMapper objectMapper = new ObjectMapper();
//        String output = StringEscapeUtils.unescapeJava(result);
        Map<String, Object> temp = objectMapper.readValue(result, Map.class);
//        result = result.replace("\\\\u", "\\u");
//        String output = StringEscapeUtils.unescapeJava(result);
        return temp;
    }

}