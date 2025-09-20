package com.lin.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 简化的令牌桶限流服务
 * 
 * @author lin
 */
@Service
public class SimpleBucketRateLimitService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBucketRateLimitService.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 本地缓存，用于快速访问
    private final ConcurrentHashMap<String, TokenBucket> localBuckets = new ConcurrentHashMap<>();

    // 限流配置
    private static final int DEFAULT_CAPACITY = 100; // 默认容量
    private static final int DEFAULT_REFILL_RATE = 10; // 默认补充速率（每秒）
    private static final long DEFAULT_REFILL_INTERVAL = 1000; // 默认补充间隔（毫秒）

    private static final int API_CAPACITY = 1000;
    private static final int API_REFILL_RATE = 100;
    private static final long API_REFILL_INTERVAL = 1000;

    private static final int LOGIN_CAPACITY = 5;
    private static final int LOGIN_REFILL_RATE = 1;
    private static final long LOGIN_REFILL_INTERVAL = 60000; // 1分钟

    private static final int UPLOAD_CAPACITY = 10;
    private static final int UPLOAD_REFILL_RATE = 1;
    private static final long UPLOAD_REFILL_INTERVAL = 60000; // 1分钟

    /**
     * 检查是否允许请求
     */
    public boolean isAllowed(String clientIp, String requestUri, String method) {
        try {
            String key = generateKey(clientIp, requestUri, method);
            TokenBucket bucket = getBucket(key, requestUri);
            
            return bucket.tryConsume(1);
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
            
            // 记录到Redis
            String redisKey = "rate_limit:" + key;
            redisTemplate.opsForValue().increment(redisKey);
            redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
            
            logger.debug("记录请求: IP={}, URI={}, Method={}", clientIp, requestUri, method);
            
        } catch (Exception e) {
            logger.error("记录请求时发生错误", e);
        }
    }

    /**
     * 获取或创建限流桶
     */
    private TokenBucket getBucket(String key, String requestUri) {
        return localBuckets.computeIfAbsent(key, k -> createBucket(requestUri));
    }

    /**
     * 根据请求URI创建相应的限流桶
     */
    private TokenBucket createBucket(String requestUri) {
        if (requestUri.contains("/api/auth/login")) {
            return new TokenBucket(LOGIN_CAPACITY, LOGIN_REFILL_RATE, LOGIN_REFILL_INTERVAL);
        } else if (requestUri.contains("/api/upload")) {
            return new TokenBucket(UPLOAD_CAPACITY, UPLOAD_REFILL_RATE, UPLOAD_REFILL_INTERVAL);
        } else if (requestUri.startsWith("/api/")) {
            return new TokenBucket(API_CAPACITY, API_REFILL_RATE, API_REFILL_INTERVAL);
        } else {
            return new TokenBucket(DEFAULT_CAPACITY, DEFAULT_REFILL_RATE, DEFAULT_REFILL_INTERVAL);
        }
    }

    /**
     * 生成限流键
     */
    private String generateKey(String clientIp, String requestUri, String method) {
        return clientIp + ":" + method + ":" + requestUri;
    }

    /**
     * 获取剩余令牌数
     */
    public long getAvailableTokens(String clientIp, String requestUri, String method) {
        try {
            String key = generateKey(clientIp, requestUri, method);
            TokenBucket bucket = getBucket(key, requestUri);
            return bucket.getAvailableTokens();
        } catch (Exception e) {
            logger.error("获取剩余令牌数时发生错误", e);
            return 0;
        }
    }

    /**
     * 重置限流桶
     */
    public void resetBucket(String clientIp, String requestUri, String method) {
        try {
            String key = generateKey(clientIp, requestUri, method);
            localBuckets.remove(key);
            
            // 清除Redis中的记录
            String redisKey = "rate_limit:" + key;
            redisTemplate.delete(redisKey);
            
            logger.info("重置限流桶: {}", key);
        } catch (Exception e) {
            logger.error("重置限流桶时发生错误", e);
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
     * 简化的令牌桶实现
     */
    private static class TokenBucket {
        private final int capacity;
        private final int refillRate;
        private final long refillInterval;
        private final AtomicLong tokens;
        private volatile long lastRefillTime;

        public TokenBucket(int capacity, int refillRate, long refillInterval) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.refillInterval = refillInterval;
            this.tokens = new AtomicLong(capacity);
            this.lastRefillTime = System.currentTimeMillis();
        }

        public boolean tryConsume(int requestedTokens) {
            refill();
            long currentTokens = tokens.get();
            if (currentTokens >= requestedTokens) {
                return tokens.compareAndSet(currentTokens, currentTokens - requestedTokens);
            }
            return false;
        }

        public long getAvailableTokens() {
            refill();
            return tokens.get();
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long timePassed = now - lastRefillTime;
            
            if (timePassed >= refillInterval) {
                long tokensToAdd = (timePassed / refillInterval) * refillRate;
                long currentTokens = tokens.get();
                long newTokens = Math.min(capacity, currentTokens + tokensToAdd);
                tokens.set(newTokens);
                lastRefillTime = now;
            }
        }
    }
}

