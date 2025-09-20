package com.lin.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * 输入验证工具类
 * 
 * @author lin
 */
public class InputValidationUtil {

    private static final Logger logger = LoggerFactory.getLogger(InputValidationUtil.class);

    // 常用正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );
    
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
    );
    
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
    );
    
    private static final Pattern IP_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证身份证号格式
     */
    public static boolean isValidIdCard(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            return false;
        }
        return ID_CARD_PATTERN.matcher(idCard).matches();
    }

    /**
     * 验证URL格式
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }

    /**
     * 验证IP地址格式
     */
    public static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        return IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 验证用户名格式
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * 验证密码强度
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * 验证输入长度
     */
    public static boolean isValidLength(String input, int minLength, int maxLength) {
        if (input == null) {
            return minLength == 0;
        }
        int length = input.length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * 验证输入是否为空
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * 验证输入是否为数字
     */
    public static boolean isNumeric(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证输入是否为整数
     */
    public static boolean isInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证输入是否为长整数
     */
    public static boolean isLong(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证输入是否为浮点数
     */
    public static boolean isFloat(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证输入是否为双精度浮点数
     */
    public static boolean isDouble(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证输入是否为布尔值
     */
    public static boolean isBoolean(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return "true".equalsIgnoreCase(input) || "false".equalsIgnoreCase(input);
    }

    /**
     * 验证输入是否为日期格式
     */
    public static boolean isDate(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        // 简单的日期格式验证 (YYYY-MM-DD)
        return input.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    /**
     * 验证输入是否为时间格式
     */
    public static boolean isTime(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        // 简单的时间格式验证 (HH:MM:SS)
        return input.matches("^\\d{2}:\\d{2}:\\d{2}$");
    }

    /**
     * 验证输入是否为日期时间格式
     */
    public static boolean isDateTime(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        // 简单的日期时间格式验证 (YYYY-MM-DD HH:MM:SS)
        return input.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
    }

    /**
     * 验证输入是否只包含字母
     */
    public static boolean isAlpha(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("^[a-zA-Z]+$");
    }

    /**
     * 验证输入是否只包含数字
     */
    public static boolean isDigit(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("^\\d+$");
    }

    /**
     * 验证输入是否只包含字母和数字
     */
    public static boolean isAlphaNumeric(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * 验证输入是否只包含字母、数字和下划线
     */
    public static boolean isAlphaNumericUnderscore(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * 验证输入是否只包含字母、数字、下划线和连字符
     */
    public static boolean isAlphaNumericUnderscoreHyphen(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("^[a-zA-Z0-9_-]+$");
    }

    /**
     * 验证输入是否包含特殊字符
     */
    public static boolean containsSpecialCharacters(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches(".*[^a-zA-Z0-9_\\s].*");
    }

    /**
     * 验证输入是否包含HTML标签
     */
    public static boolean containsHtmlTags(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches(".*<[^>]+>.*");
    }

    /**
     * 验证输入是否包含SQL关键字
     */
    public static boolean containsSqlKeywords(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        String lowerInput = input.toLowerCase();
        String[] sqlKeywords = {
            "select", "insert", "update", "delete", "drop", "create", "alter",
            "union", "exec", "execute", "sp_", "xp_", "load_file", "into outfile"
        };
        
        for (String keyword : sqlKeywords) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 验证输入是否包含JavaScript代码
     */
    public static boolean containsJavaScript(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        String lowerInput = input.toLowerCase();
        String[] jsKeywords = {
            "javascript:", "vbscript:", "onload", "onerror", "onclick", "onmouseover",
            "document.cookie", "document.write", "window.location", "eval(", "alert("
        };
        
        for (String keyword : jsKeywords) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 清理输入内容
     */
    public static String cleanInput(String input) {
        if (input == null) {
            return null;
        }
        
        // 去除首尾空格
        String cleaned = input.trim();
        
        // 移除HTML标签
        cleaned = cleaned.replaceAll("<[^>]+>", "");
        
        // 转义特殊字符
        cleaned = cleaned.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;")
                        .replace("'", "&#x27;")
                        .replace("/", "&#x2F;");
        
        return cleaned;
    }
}
