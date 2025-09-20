package com.lin.security.util;

import org.apache.commons.text.StringEscapeUtils;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * XSS防护工具类
 * 
 * @author lin
 */
public class XssUtil {

    private static final Logger logger = LoggerFactory.getLogger(XssUtil.class);

    // XSS攻击模式
    private static final Pattern[] XSS_PATTERNS = {
        // 脚本标签
        Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script[^>]*>", Pattern.CASE_INSENSITIVE),
        
        // 事件处理器
//        Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE),
        
        // JavaScript协议
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("data:", Pattern.CASE_INSENSITIVE),
        
        // 表达式
        Pattern.compile("expression\\s*\\(", Pattern.CASE_INSENSITIVE),
        
        // 样式
        Pattern.compile("<style[^>]*>.*?</style>", Pattern.CASE_INSENSITIVE),
        
        // 链接
//        Pattern.compile("<link[^>]*>", Pattern.CASE_INSENSITIVE),
        
        // 对象
        Pattern.compile("<object[^>]*>.*?</object>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<embed[^>]*>", Pattern.CASE_INSENSITIVE),
        
        // 表单
        Pattern.compile("<form[^>]*>", Pattern.CASE_INSENSITIVE),
        
        // 框架
        Pattern.compile("<iframe[^>]*>.*?</iframe>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<frame[^>]*>", Pattern.CASE_INSENSITIVE),
        
        // 其他危险标签
        Pattern.compile("<meta[^>]*>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<base[^>]*>", Pattern.CASE_INSENSITIVE),
        
        // SQL注入相关
        Pattern.compile("(union|select|insert|update|delete|drop|create|alter|exec|execute)\\s+", Pattern.CASE_INSENSITIVE),
        
        // 特殊字符
//        Pattern.compile("[<>\"'&]", Pattern.CASE_INSENSITIVE)
    };

    // 危险关键词
    private static final String[] DANGEROUS_KEYWORDS = {
        "javascript:", "vbscript:", "data:", "expression(", "eval(", "alert(",
        "confirm(", "prompt(", "document.cookie", "document.write", "window.location",
        "document.location", "innerHTML", "outerHTML", "insertAdjacentHTML"
    };

    /**
     * 检查字符串是否包含XSS攻击
     */
    public static boolean containsXss(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // 检查XSS模式
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(input).find()) {
                logger.debug("检测到XSS模式: {}", pattern.pattern());
                return true;
            }
        }

        // 检查危险关键词
        String lowerInput = input.toLowerCase();
        for (String keyword : DANGEROUS_KEYWORDS) {
            if (lowerInput.contains(keyword.toLowerCase())) {
                logger.debug("检测到危险关键词: {}", keyword);
                return true;
            }
        }

        return false;
    }

    /**
     * 清理XSS内容
     */
    public static String cleanXss(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        try {
            // 使用OWASP Encoder进行HTML编码
            String cleaned = Encode.forHtml(input);
            
            // 使用Apache Commons Text进行额外的转义
            cleaned = StringEscapeUtils.escapeHtml4(cleaned);
            
            return cleaned;
        } catch (Exception e) {
            logger.error("清理XSS内容时发生错误", e);
            // 如果清理失败，返回空字符串
            return "";
        }
    }

    /**
     * 清理JavaScript内容
     */
    public static String cleanJavaScript(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        try {
            return Encode.forJavaScript(input);
        } catch (Exception e) {
            logger.error("清理JavaScript内容时发生错误", e);
            return "";
        }
    }

    /**
     * 清理URL内容
     */
    public static String cleanUrl(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        try {
            return Encode.forUriComponent(input);
        } catch (Exception e) {
            logger.error("清理URL内容时发生错误", e);
            return "";
        }
    }

    /**
     * 清理CSS内容
     */
    public static String cleanCss(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        try {
            return Encode.forCssString(input);
        } catch (Exception e) {
            logger.error("清理CSS内容时发生错误", e);
            return "";
        }
    }

    /**
     * 清理XML内容
     */
    public static String cleanXml(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        try {
            return Encode.forXml(input);
        } catch (Exception e) {
            logger.error("清理XML内容时发生错误", e);
            return "";
        }
    }

    /**
     * 验证输入长度
     */
    public static boolean isValidLength(String input, int maxLength) {
        if (input == null) {
            return true;
        }
        return input.length() <= maxLength;
    }

    /**
     * 验证输入格式（只允许字母、数字、下划线、连字符）
     */
    public static boolean isValidFormat(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("^[a-zA-Z0-9_-]+$");
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    /**
     * 验证URL格式
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        String urlPattern = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return url.matches(urlPattern);
    }

    /**
     * 验证输入是否包含特殊字符
     */
    public static boolean containsSpecialCharacters(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        
        // 检查是否包含SQL特殊字符
        return input.matches(".*['\";\\\\<>].*");
    }
}
