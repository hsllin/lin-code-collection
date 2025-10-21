package com.lin.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lin.bean.ShengKaoBean;
import com.lin.bean.cailianshe.CaiLianSheLimitUp;
import com.lin.bean.cailianshe.Telegraph;
import com.lin.bean.taoguba.TaoGuBaHotStock;
import com.lin.util.DateUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import static com.lin.bean.DateFormatEnum.DATE_WITH_OUT_LINE;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description 财联社相关service
 * @create 2025-10-15 18:07
 */
@Service
public class CaiLianSheService {
    public static void main(String[] args) {
//      String params = "app=CailianpressWeb&category=watch&last_time=1760522817&os=web&refresh_type=1&rn=20&sv=8.4.6";
//      Map<String, String> paramsMap = new HashMap<>();
//      paramsMap.put("app", "CailianpressWeb");
//      paramsMap.put("category", "watch");
//      paramsMap.put("last_time", "1760522817");
//      paramsMap.put("os", "web");
//      paramsMap.put("refresh_type", "1");
//      paramsMap.put("rn", "20");
//      paramsMap.put("sv", "8.4.6");
//
//
//      String result = sha1ThenMd5(params);
//      System.out.println("SHA1+MD5结果: " + result);
//      System.out.println("长度: " + result.length());
//        getTelegraph();
//        getShengKaoData();
    }

    /**
     * 财联社电报看板
     *
     * @return
     */
    public static Telegraph getTelegraph() {

        String params = "app=CailianpressWeb&category=watch&last_time=" + System.currentTimeMillis() + "&os=web&refresh_type=1&rn=20&sv=8.4.6";
        String sign = sha1ThenMd5(params);
        String url = "https://www.cls.cn/v1/roll/get_roll_list?" + params + "&sign=" + sign;
        Map<String, Object> param = new HashMap<>();
        String result = HttpUtil.get(url, param);
        Telegraph bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), Telegraph.class);

        for (int i = 0; i < 3; i++) {
            Integer lastTime = bean.getData().getRollData().get(0).getCtime();
            params = "app=CailianpressWeb&category=watch&last_time=" + lastTime + "&os=web&refresh_type=1&rn=20&sv=8.4.6";
            sign = sha1ThenMd5(params);
            String tempUrl = "https://www.cls.cn/v1/roll/get_roll_list?" + params + "&sign=" + sign;
            result = HttpUtil.get(tempUrl, param);
            Telegraph tempBean = JSONUtil.toBean(JSONUtil.toJsonStr(result), Telegraph.class);
            bean.getData().getRollData().addAll(tempBean.getData().getRollData());
        }
        return bean;
    }

    public static CaiLianSheLimitUp getCaiLianSheLimitUp() {

        String params = "up_limit=0&date=" + DateUtils.getDate(DATE_WITH_OUT_LINE);
        String sign = sha1ThenMd5(params);
        String url = "https://x-quote.cls.cn/v2/quote/a/plate/up_down_analysis?" + params + "&sign=" + sign;
        Map<String, Object> param = new HashMap<>();
        String result = HttpUtil.get(url, param);
        CaiLianSheLimitUp bean = JSONUtil.toBean(JSONUtil.toJsonStr(result), CaiLianSheLimitUp.class);
        return bean;
    }


    public static void generateSign() {
        String params = "app=CailianpressWeb&category=watch&last_time=1760522817&os=web&refresh_type=1&rn=20&sv=8.4.6";

    }

    /**
     * 财联社sign加密方案：https://blog.csdn.net/m0_60082046/article/details/144774637
     * 先进行SHA1加密，然后对结果进行32位MD5哈希
     */
    public static String sha1ThenMd5(String input) {
        try {
            // 1. SHA1加密（40位十六进制）
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] sha1Bytes = sha1.digest(input.getBytes(StandardCharsets.UTF_8));
            String sha1Hex = bytesToHex(sha1Bytes);
            System.out.println("SHA1结果(40位): " + sha1Hex);

            // 2. 对SHA1结果进行MD5哈希（32位十六进制）
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(sha1Hex.getBytes(StandardCharsets.UTF_8));
            String md5Hex = bytesToHex(md5Bytes);

            return md5Hex; // 32位MD5结果
        } catch (Exception e) {
            throw new RuntimeException("哈希处理失败", e);
        }
    }

    /**
     * 字节数组转十六进制字符串（32位MD5格式）
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


}