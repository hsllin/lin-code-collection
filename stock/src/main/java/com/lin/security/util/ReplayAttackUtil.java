package com.lin.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * 重放攻击防护工具类
 * 
 * @author lin
 */
@Component
public class ReplayAttackUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReplayAttackUtil.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    // 重放攻击检测时间窗口（5分钟）
    private static final long REPLAY_WINDOW = TimeUnit.MINUTES.toMillis(5);
    
    // 请求签名过期时间（10分钟）
    private static final long SIGNATURE_EXPIRE_TIME = TimeUnit.MINUTES.toMillis(10);

    /**
     * 生成请求签名
     */
    public String generateSignature(String method, String uri, String body, String timestamp, String nonce, String secretKey) {
        try {
            // 构建签名字符串
            StringBuilder signString = new StringBuilder();
            signString.append(method.toUpperCase()).append("\n");
            signString.append(uri).append("\n");
            signString.append(body != null ? body : "").append("\n");
            signString.append(timestamp).append("\n");
            signString.append(nonce).append("\n");
            signString.append(secretKey);

            // 计算SHA256哈希
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(signString.toString().getBytes("UTF-8"));
            
            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            logger.error("生成请求签名时发生错误", e);
            return null;
        }
    }

    /**
     * 验证请求签名
     */
    public boolean validateSignature(String method, String uri, String body, String timestamp, 
                                   String nonce, String signature, String secretKey) {
        try {
            // 生成期望的签名
            String expectedSignature = generateSignature(method, uri, body, timestamp, nonce, secretKey);
            
            if (expectedSignature == null) {
                return false;
            }
            
            // 比较签名
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            logger.error("验证请求签名时发生错误", e);
            return false;
        }
    }

    /**
     * 检查时间戳是否在有效范围内
     */
    public boolean isTimestampValid(String timestamp) {
        try {
            long requestTime = Long.parseLong(timestamp);
            long currentTime = System.currentTimeMillis();
            
            // 检查时间戳是否在有效范围内（前后5分钟）
            long timeDiff = Math.abs(currentTime - requestTime);
            return timeDiff <= REPLAY_WINDOW;
        } catch (NumberFormatException e) {
            logger.error("时间戳格式错误: {}", timestamp);
            return false;
        }
    }

    /**
     * 检查Nonce是否已被使用
     */
    public boolean isNonceUsed(String nonce) {
        try {
            String key = "replay_nonce:" + nonce;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            logger.error("检查Nonce时发生错误", e);
            return true; // 发生错误时认为已被使用，拒绝请求
        }
    }

    /**
     * 标记Nonce为已使用
     */
    public void markNonceAsUsed(String nonce) {
        try {
            String key = "replay_nonce:" + nonce;
            redisTemplate.opsForValue().set(key, "1", SIGNATURE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
            logger.debug("Nonce已标记为已使用: {}", nonce);
        } catch (Exception e) {
            logger.error("标记Nonce为已使用时发生错误", e);
        }
    }

    /**
     * 检查请求是否重放攻击
     */
    public boolean isReplayAttack(String method, String uri, String body, String timestamp, 
                                String nonce, String signature, String secretKey) {
        try {
            // 检查时间戳
            if (!isTimestampValid(timestamp)) {
                logger.warn("时间戳无效: {}", timestamp);
                return true;
            }

            // 检查Nonce是否已被使用
            if (isNonceUsed(nonce)) {
                logger.warn("Nonce已被使用: {}", nonce);
                return true;
            }

            // 验证签名
            if (!validateSignature(method, uri, body, timestamp, nonce, signature, secretKey)) {
                logger.warn("签名验证失败");
                return true;
            }

            // 标记Nonce为已使用
            markNonceAsUsed(nonce);

            return false;
        } catch (Exception e) {
            logger.error("检查重放攻击时发生错误", e);
            return true; // 发生错误时认为可能是重放攻击
        }
    }

    /**
     * 生成随机Nonce
     */
    public String generateNonce() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = System.currentTimeMillis() + "" + Math.random();
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            logger.error("生成Nonce时发生错误", e);
            return null;
        }
    }

    /**
     * 生成当前时间戳
     */
    public String generateTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 清理过期的Nonce
     */
    public void cleanExpiredNonces() {
        try {
            // Redis会自动清理过期的key，这里可以添加额外的清理逻辑
            logger.debug("清理过期的Nonce");
        } catch (Exception e) {
            logger.error("清理过期Nonce时发生错误", e);
        }
    }

    /**
     * 验证请求头中的签名信息
     */
    public boolean validateRequestHeaders(String method, String uri, String body, String secretKey) {
        try {
            // 从请求头获取签名信息
            // 这里需要根据实际的请求头名称进行调整
            String timestamp = null; // 从请求头获取
            String nonce = null; // 从请求头获取
            String signature = null; // 从请求头获取

            if (timestamp == null || nonce == null || signature == null) {
                logger.warn("缺少必要的签名信息");
                return false;
            }

            return !isReplayAttack(method, uri, body, timestamp, nonce, signature, secretKey);
        } catch (Exception e) {
            logger.error("验证请求头签名时发生错误", e);
            return false;
        }
    }
}
