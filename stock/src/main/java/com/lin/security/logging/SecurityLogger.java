package com.lin.security.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 安全日志记录器
 * 
 * @author lin
 */
@Component
public class SecurityLogger {

    private static final Logger logger = LoggerFactory.getLogger(SecurityLogger.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 记录登录成功
     */
    public void logLoginSuccess(String username, String ip, String userAgent) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "LOGIN_SUCCESS");
        logData.put("username", username);
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.info("登录成功: {}", logData);
    }

    /**
     * 记录登录失败
     */
    public void logLoginFailure(String username, String ip, String userAgent, String reason) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "LOGIN_FAILURE");
        logData.put("username", username);
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("reason", reason);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.warn("登录失败: {}", logData);
    }

    /**
     * 记录登出
     */
    public void logLogout(String username, String ip) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "LOGOUT");
        logData.put("username", username);
        logData.put("ip", ip);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.info("用户登出: {}", logData);
    }

    /**
     * 记录XSS攻击
     */
    public void logXssAttack(String ip, String userAgent, String uri, String payload) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "XSS_ATTACK");
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("uri", uri);
        logData.put("payload", payload);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.error("XSS攻击检测: {}", logData);
    }

    /**
     * 记录SQL注入攻击
     */
    public void logSqlInjectionAttack(String ip, String userAgent, String uri, String payload) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "SQL_INJECTION_ATTACK");
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("uri", uri);
        logData.put("payload", payload);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.error("SQL注入攻击检测: {}", logData);
    }

    /**
     * 记录CSRF攻击
     */
    public void logCsrfAttack(String ip, String userAgent, String uri) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "CSRF_ATTACK");
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("uri", uri);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.error("CSRF攻击检测: {}", logData);
    }

    /**
     * 记录重放攻击
     */
    public void logReplayAttack(String ip, String userAgent, String uri) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "REPLAY_ATTACK");
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("uri", uri);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.error("重放攻击检测: {}", logData);
    }

    /**
     * 记录限流触发
     */
    public void logRateLimitExceeded(String ip, String userAgent, String uri, String method) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "RATE_LIMIT_EXCEEDED");
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("uri", uri);
        logData.put("method", method);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.warn("请求频率超限: {}", logData);
    }

    /**
     * 记录权限拒绝
     */
    public void logAccessDenied(String username, String ip, String uri, String method) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "ACCESS_DENIED");
        logData.put("username", username);
        logData.put("ip", ip);
        logData.put("uri", uri);
        logData.put("method", method);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.warn("访问被拒绝: {}", logData);
    }

    /**
     * 记录认证失败
     */
    public void logAuthenticationFailure(String ip, String userAgent, String uri, String reason) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "AUTHENTICATION_FAILURE");
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("uri", uri);
        logData.put("reason", reason);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.warn("认证失败: {}", logData);
    }

    /**
     * 记录可疑活动
     */
    public void logSuspiciousActivity(String ip, String userAgent, String uri, String activity, String details) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "SUSPICIOUS_ACTIVITY");
        logData.put("ip", ip);
        logData.put("userAgent", userAgent);
        logData.put("uri", uri);
        logData.put("activity", activity);
        logData.put("details", details);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.warn("可疑活动检测: {}", logData);
    }

    /**
     * 记录IP封禁
     */
    public void logIpBlocked(String ip, String reason, long duration) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "IP_BLOCKED");
        logData.put("ip", ip);
        logData.put("reason", reason);
        logData.put("duration", duration);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.error("IP被封禁: {}", logData);
    }

    /**
     * 记录IP解封
     */
    public void logIpUnblocked(String ip) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "IP_UNBLOCKED");
        logData.put("ip", ip);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.info("IP已解封: {}", logData);
    }

    /**
     * 记录系统异常
     */
    public void logSystemException(String component, String operation, Exception exception) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "SYSTEM_EXCEPTION");
        logData.put("component", component);
        logData.put("operation", operation);
        logData.put("exception", exception.getClass().getSimpleName());
        logData.put("message", exception.getMessage());
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.error("系统异常: {}", logData, exception);
    }

    /**
     * 记录安全配置变更
     */
    public void logSecurityConfigChange(String config, String oldValue, String newValue, String operator) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "SECURITY_CONFIG_CHANGE");
        logData.put("config", config);
        logData.put("oldValue", oldValue);
        logData.put("newValue", newValue);
        logData.put("operator", operator);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.info("安全配置变更: {}", logData);
    }

    /**
     * 记录数据访问
     */
    public void logDataAccess(String username, String ip, String operation, String resource) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "DATA_ACCESS");
        logData.put("username", username);
        logData.put("ip", ip);
        logData.put("operation", operation);
        logData.put("resource", resource);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.info("数据访问: {}", logData);
    }

    /**
     * 记录文件操作
     */
    public void logFileOperation(String username, String ip, String operation, String filePath) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "FILE_OPERATION");
        logData.put("username", username);
        logData.put("ip", ip);
        logData.put("operation", operation);
        logData.put("filePath", filePath);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.info("文件操作: {}", logData);
    }

    /**
     * 记录API调用
     */
    public void logApiCall(String username, String ip, String method, String uri, int statusCode, long duration) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "API_CALL");
        logData.put("username", username);
        logData.put("ip", ip);
        logData.put("method", method);
        logData.put("uri", uri);
        logData.put("statusCode", statusCode);
        logData.put("duration", duration);
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        
        logger.info("API调用: {}", logData);
    }
}
