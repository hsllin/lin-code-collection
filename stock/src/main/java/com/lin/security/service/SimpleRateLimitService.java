package com.lin.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 简化限流服务
 * 
 * @author lin
 */
@Service
public class SimpleRateLimitService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRateLimitService.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 限流配置
    private static final int DEFAULT_LIMIT = 100; // 默认限制
    private static final int API_LIMIT = 1000; // API限制
    private static final int LOGIN_LIMIT = 5; // 登录限制
    private static final int UPLOAD_LIMIT = 10; // 上传限制

    /**
     * 检查是否允许请求
     */
    public boolean isAllowed(String clientIp, String requestUri, String method) {
        try {
            String key = generateKey(clientIp, requestUri, method);
            int limit = getLimit(requestUri);
            
            String countStr = redisTemplate.opsForValue().get(key);
            int count = countStr != null ? Integer.parseInt(countStr) : 0;
            
            if (count >= limit) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            logger.error("检查限流时发生错误", e);
            return true; // 发生错误时允许请求通过
        }
    }

    /**
     * 记录请求
     */
    public void recordRequest(String clientIp, String requestUri, String method) {
        try {
            String key = generateKey(clientIp, requestUri, method);
            
            // 使用Redis的INCR命令原子性增加计数
            Long count = redisTemplate.opsForValue().increment(key);
            
            // 设置过期时间（1分钟）
            if (count == 1) {
                redisTemplate.expire(key, 1, TimeUnit.MINUTES);
            }
            
            logger.debug("记录请求: IP={}, URI={}, Method={}, Count={}", clientIp, requestUri, method, count);
            
        } catch (Exception e) {
            logger.error("记录请求时发生错误", e);
        }
    }

    /**
     * 获取剩余请求次数
     */
    public long getAvailableRequests(String clientIp, String requestUri, String method) {
        try {
            String key = generateKey(clientIp, requestUri, method);
            int limit = getLimit(requestUri);
            
            String countStr = redisTemplate.opsForValue().get(key);
            int count = countStr != null ? Integer.parseInt(countStr) : 0;
            
            return Math.max(0, limit - count);
        } catch (Exception e) {
            logger.error("获取剩余请求次数时发生错误", e);
            return 0;
        }
    }

    /**
     * 重置限流计数
     */
    public void resetLimit(String clientIp, String requestUri, String method) {
        try {
            String key = generateKey(clientIp, requestUri, method);
            redisTemplate.delete(key);
            logger.info("重置限流计数: {}", key);
        } catch (Exception e) {
            logger.error("重置限流计数时发生错误", e);
        }
    }

    /**
     * 检查IP是否在黑名单中
     */
    public boolean isIpBlacklisted(String clientIp) {
        try {
            String blacklistKey = "blacklist:" + clientIp;
            return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey));
        } catch (Exception e) {
            logger.error("检查IP黑名单时发生错误", e);
            return false;
        }
    }

    /**
     * 将IP添加到黑名单
     */
    public void addToBlacklist(String clientIp, long durationMinutes) {
        try {
            String blacklistKey = "blacklist:" + clientIp;
            redisTemplate.opsForValue().set(blacklistKey, "1", durationMinutes, TimeUnit.MINUTES);
            logger.warn("IP已添加到黑名单: {}, 持续时间: {}分钟", clientIp, durationMinutes);
        } catch (Exception e) {
            logger.error("添加IP到黑名单时发生错误", e);
        }
    }

    /**
     * 从黑名单中移除IP
     */
    public void removeFromBlacklist(String clientIp) {
        try {
            String blacklistKey = "blacklist:" + clientIp;
            redisTemplate.delete(blacklistKey);
            logger.info("IP已从黑名单中移除: {}", clientIp);
        } catch (Exception e) {
            logger.error("从黑名单中移除IP时发生错误", e);
        }
    }

    /**
     * 生成限流键
     */
    private String generateKey(String clientIp, String requestUri, String method) {
        return "rate_limit:" + clientIp + ":" + method + ":" + requestUri;
    }

    /**
     * 根据请求URI获取限制
     */
    private int getLimit(String requestUri) {
        if (requestUri.contains("/api/auth/login")) {
            return LOGIN_LIMIT;
        } else if (requestUri.contains("/api/upload")) {
            return UPLOAD_LIMIT;
        } else if (requestUri.startsWith("/api/")) {
            return API_LIMIT;
        } else {
            return DEFAULT_LIMIT;
        }
    }
}
