package com.lin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.config.EncryptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 优化版数据加密工具类
 * 使用AES-256-GCM算法，通过缓存和优化提升性能
 */
@Component
public class OptimizedEncryptionUtil {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    
    private final ObjectMapper objectMapper;
    private final EncryptionProperties encryptionProperties;
    
    // 缓存Cipher实例，避免重复创建
    private final Map<String, Cipher> encryptCipherCache = new ConcurrentHashMap<>();
    private final Map<String, Cipher> decryptCipherCache = new ConcurrentHashMap<>();
    
    // 缓存SecretKeySpec，避免重复创建
    private SecretKeySpec cachedKeySpec;
    
    // 预分配的SecureRandom实例
    private final SecureRandom secureRandom = new SecureRandom();
    
    @Autowired
    public OptimizedEncryptionUtil(EncryptionProperties encryptionProperties) {
        this.objectMapper = new ObjectMapper();
        this.encryptionProperties = encryptionProperties;
        
        // 预创建密钥规范
        this.cachedKeySpec = new SecretKeySpec(
            encryptionProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), 
            ALGORITHM
        );
    }
    
    /**
     * 优化版加密数据
     * @param data 要加密的数据
     * @return 加密后的Base64字符串
     */
    public String encrypt(String data) {
        try {
            if (!encryptionProperties.isEnabled()) {
                return data;
            }
            
            // 使用缓存的Cipher实例
            Cipher cipher = getOrCreateEncryptCipher();
            
            // 生成随机IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, cachedKeySpec, parameterSpec);
            
            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // 将IV和加密数据组合
            byte[] encryptedWithIv = new byte[GCM_IV_LENGTH + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedData, 0, encryptedWithIv, GCM_IV_LENGTH, encryptedData.length);
            
            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }
    
    /**
     * 优化版解密数据
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
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[encryptedWithIv.length - GCM_IV_LENGTH];
            System.arraycopy(encryptedWithIv, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedWithIv, GCM_IV_LENGTH, encrypted, 0, encrypted.length);
            
            // 使用缓存的Cipher实例
            Cipher cipher = getOrCreateDecryptCipher();
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, cachedKeySpec, parameterSpec);
            
            byte[] decryptedData = cipher.doFinal(encrypted);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }
    
    /**
     * 优化版加密对象
     * @param obj 要加密的对象
     * @return 加密后的Base64字符串
     */
    public String encryptObject(Object obj) {
        try {
            // 使用更快的JSON序列化配置
            String json = objectMapper.writeValueAsString(obj);
            return encrypt(json);
        } catch (Exception e) {
            throw new RuntimeException("对象加密失败", e);
        }
    }
    
    /**
     * 优化版解密对象
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
     * 优化版生成加密响应包装器
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
     * 获取或创建加密Cipher实例
     */
    private Cipher getOrCreateEncryptCipher() {
        return encryptCipherCache.computeIfAbsent("encrypt", k -> {
            try {
                return Cipher.getInstance(TRANSFORMATION);
            } catch (Exception e) {
                throw new RuntimeException("创建加密Cipher失败", e);
            }
        });
    }
    
    /**
     * 获取或创建解密Cipher实例
     */
    private Cipher getOrCreateDecryptCipher() {
        return decryptCipherCache.computeIfAbsent("decrypt", k -> {
            try {
                return Cipher.getInstance(TRANSFORMATION);
            } catch (Exception e) {
                throw new RuntimeException("创建解密Cipher失败", e);
            }
        });
    }
    
    /**
     * 清理缓存（在密钥更新时调用）
     */
    public void clearCache() {
        encryptCipherCache.clear();
        decryptCipherCache.clear();
    }
    
    /**
     * 生成密钥信息
     * @return 密钥信息
     */
    public Map<String, String> generateKeyInfo() {
        Map<String, String> keyInfo = new HashMap<>();
        keyInfo.put("key", encryptionProperties.getSecretKey());
        keyInfo.put("algorithm", encryptionProperties.getAlgorithm());
        keyInfo.put("mode", encryptionProperties.getMode());
        return keyInfo;
    }
}

