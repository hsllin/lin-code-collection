package com.lin.service;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.pluginking.LiveStreamingBean;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-07-30 11:29
 */
@Service
public class PluginKingService {
    public static void main(String[] args) {
        getLiveStreamingData();
    }

    public static String getLiveStreamingData() {
        String url = "https://apphq.longhuvip.com/w1/api/index.php?a=ZhiBoContent&apiv=w21&c=ConceptionPoint&_=" + System.currentTimeMillis();


//        20250121
        Map<String, Object> param = new HashMap<>();
        param.put("_", System.currentTimeMillis());

        String result = HttpUtil.get(url, param);
//        result = UnicodeUtil.toString(result);
        JSONObject json = new JSONObject(result);
        decodeUnicodeInJson(json);
        LiveStreamingBean bean = JSONUtil.toBean(JSONUtil.toJsonStr(json), LiveStreamingBean.class);
        return result;
    }

    /**
     * 递归解码JSON对象中的Unicode字符串
     */
    private static void decodeUnicodeInJson(JSONObject jsonObj) {
        for (String key : jsonObj.keySet()) {
            Object value = jsonObj.get(key);
            if (value instanceof String) {
                jsonObj.put(key, decodeUnicode((String) value));
            } else if (value instanceof JSONObject) {
                decodeUnicodeInJson((JSONObject) value);
            } else if (value instanceof JSONArray) {
                decodeUnicodeInArray((JSONArray) value);
            }
        }
    }
    /**
     * Unicode转中文核心方法
     */
    private static String decodeUnicode(String unicodeStr) {
        StringBuilder sb = new StringBuilder();
        char[] chars = unicodeStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\\' && i + 1 < chars.length && chars[i + 1] == 'u') {
                // 解析4位十六进制编码
                String hex = new String(new char[]{chars[i+2], chars[i+3], chars[i+4], chars[i+5]});
                sb.append((char) Integer.parseInt(hex, 16));
                i += 5;  // 跳过已处理的Unicode序列
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }
    /**
     * 递归解码JSON数组中的Unicode字符串
     */
    private static void decodeUnicodeInArray(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object item = jsonArray.get(i);
            if (item instanceof String) {
                jsonArray.put(i, decodeUnicode((String) item));
            } else if (item instanceof JSONObject) {
                decodeUnicodeInJson((JSONObject) item);
            } else if (item instanceof JSONArray) {
                decodeUnicodeInArray((JSONArray) item);
            }
        }
    }
}