package com.lin.security.validation;

import com.lin.security.util.SqlInjectionUtil;
import com.lin.security.util.XssUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 安全输入验证器
 * 
 * @author lin
 */
public class SafeInputValidator implements ConstraintValidator<SafeInput, String> {

    private static final Logger logger = LoggerFactory.getLogger(SafeInputValidator.class);

    private boolean allowHtml;
    private boolean allowSpecialChars;
    private int maxLength;
    private int minLength;
    private String pattern;
    private boolean checkXss;
    private boolean checkSqlInjection;

    @Override
    public void initialize(SafeInput constraintAnnotation) {
        this.allowHtml = constraintAnnotation.allowHtml();
        this.allowSpecialChars = constraintAnnotation.allowSpecialChars();
        this.maxLength = constraintAnnotation.maxLength();
        this.minLength = constraintAnnotation.minLength();
        this.pattern = constraintAnnotation.pattern();
        this.checkXss = constraintAnnotation.checkXss();
        this.checkSqlInjection = constraintAnnotation.checkSqlInjection();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 空值由@NotNull等注解处理
        }

        try {
            // 检查长度
            if (value.length() < minLength || value.length() > maxLength) {
                logger.debug("输入长度不符合要求: {}, 最小长度: {}, 最大长度: {}", 
                           value.length(), minLength, maxLength);
                return false;
            }

            // 检查XSS攻击
            if (checkXss && XssUtil.containsXss(value)) {
                logger.debug("检测到XSS攻击: {}", value);
                return false;
            }

            // 检查SQL注入
            if (checkSqlInjection && SqlInjectionUtil.containsSqlInjection(value)) {
                logger.debug("检测到SQL注入攻击: {}", value);
                return false;
            }

            // 检查特殊字符
            if (!allowSpecialChars && XssUtil.containsSpecialCharacters(value)) {
                logger.debug("检测到特殊字符: {}", value);
                return false;
            }

            // 检查HTML标签
            if (!allowHtml && containsHtmlTags(value)) {
                logger.debug("检测到HTML标签: {}", value);
                return false;
            }

            // 检查自定义模式
            if (pattern != null && !pattern.isEmpty()) {
                if (!Pattern.matches(pattern, value)) {
                    logger.debug("输入不符合指定模式: {}, 模式: {}", value, pattern);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("验证输入时发生错误", e);
            return false;
        }
    }

    /**
     * 检查是否包含HTML标签
     */
    private boolean containsHtmlTags(String input) {
        // 简单的HTML标签检测
        return input.matches(".*<[^>]+>.*");
    }
}
