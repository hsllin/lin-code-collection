package com.lin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.config.EncryptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 高性能加密工具类
 * 使用AES-128-CBC算法，性能比GCM快3-5倍
 */
@Component
public class FastEncryptionUtil {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;
    
    private final ObjectMapper objectMapper;
    private final EncryptionProperties encryptionProperties;
    
    // 缓存Cipher实例
    private final Map<String, Cipher> cipherCache = new ConcurrentHashMap<>();
    
    // 预分配的SecureRandom实例
    private final SecureRandom secureRandom = new SecureRandom();
    
    // 缓存密钥规范
    private SecretKeySpec cachedKeySpec;
    
    @Autowired
    public FastEncryptionUtil(EncryptionProperties encryptionProperties) {
        this.objectMapper = new ObjectMapper();
        this.encryptionProperties = encryptionProperties;
        
        // 使用128位密钥（性能更好）
        String key = encryptionProperties.getSecretKey();
        if (key.length() > 16) {
            key = key.substring(0, 16); // 截取前16位作为128位密钥
        } else {
            // 如果密钥不足16位，用0填充
            while (key.length() < 16) {
                key += "0";
            }
        }
        
        this.cachedKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }
    
    /**
     * 快速加密数据
     * @param data 要加密的数据
     * @return 加密后的Base64字符串
     */
    public String encrypt(String data) {
        try {
            if (!encryptionProperties.isEnabled()) {
                return data;
            }
            
            Cipher cipher = getOrCreateCipher("encrypt");
            
            // 生成随机IV
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            cipher.init(Cipher.ENCRYPT_MODE, cachedKeySpec, new javax.crypto.spec.IvParameterSpec(iv));
            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // 将IV和加密数据组合
            byte[] encryptedWithIv = new byte[IV_LENGTH + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, IV_LENGTH);
            System.arraycopy(encryptedData, 0, encryptedWithIv, IV_LENGTH, encryptedData.length);
            
            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }
    
    /**
     * 快速解密数据
     * @param encryptedData 加密的Base64字符串
     * @return 解密后的原始数据
     */
    public String decrypt(String encryptedData) {
        try {
            if (!encryptionProperties.isEnabled()) {
                return encryptedData;
            }
            
            byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedData);
            
            // 分离IV和加密数据
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[encryptedWithIv.length - IV_LENGTH];
            System.arraycopy(encryptedWithIv, 0, iv, 0, IV_LENGTH);
            System.arraycopy(encryptedWithIv, IV_LENGTH, encrypted, 0, encrypted.length);
            
            Cipher cipher = getOrCreateCipher("decrypt");
            cipher.init(Cipher.DECRYPT_MODE, cachedKeySpec, new javax.crypto.spec.IvParameterSpec(iv));
            
            byte[] decryptedData = cipher.doFinal(encrypted);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }
    
    /**
     * 快速加密对象
     * @param obj 要加密的对象
     * @return 加密后的Base64字符串
     */
    public String encryptObject(Object obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            return encrypt(json);
        } catch (Exception e) {
            throw new RuntimeException("对象加密失败", e);
        }
    }
    
    /**
     * 快速解密对象
     * @param encryptedData 加密的Base64字符串
     * @param clazz 目标类型
     * @return 解密后的对象
     */
    public <T> T decryptObject(String encryptedData, Class<T> clazz) {
        try {
            String json = decrypt(encryptedData);
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("对象解密失败", e);
        }
    }
    
    /**
     * 快速生成加密响应包装器
     * @param data 原始数据
     * @return 包含加密数据的响应包装器
     */
    public Map<String, Object> createEncryptedResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("encrypted", true);
        response.put("data", encryptObject(data));
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    /**
     * 获取或创建Cipher实例
     */
    private Cipher getOrCreateCipher(String type) {
        return cipherCache.computeIfAbsent(type, k -> {
            try {
                return Cipher.getInstance(TRANSFORMATION);
            } catch (Exception e) {
                throw new RuntimeException("创建Cipher失败", e);
            }
        });
    }
    
    /**
     * 清理缓存
     */
    public void clearCache() {
        cipherCache.clear();
    }
    
    /**
     * 生成密钥信息
     * @return 密钥信息
     */
    public Map<String, String> generateKeyInfo() {
        Map<String, String> keyInfo = new HashMap<>();
        keyInfo.put("key", encryptionProperties.getSecretKey());
        keyInfo.put("algorithm", "AES-128-CBC");
        keyInfo.put("mode", "CBC");
        return keyInfo;
    }
}

