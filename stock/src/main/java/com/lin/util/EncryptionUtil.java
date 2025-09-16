package com.lin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.config.EncryptionProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据加密工具类
 * 使用AES-256-GCM算法进行数据加密
 */
@Component
public class EncryptionUtil {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    
    private final ObjectMapper objectMapper;
    private final EncryptionProperties encryptionProperties;
    
    static {
        // 添加BouncyCastle提供者
        Security.addProvider(new BouncyCastleProvider());
    }
    
    @Autowired
    public EncryptionUtil(EncryptionProperties encryptionProperties) {
        this.objectMapper = new ObjectMapper();
        this.encryptionProperties = encryptionProperties;
    }
    
    /**
     * 加密数据
     * @param data 要加密的数据
     * @return 加密后的Base64字符串
     */
    public String encrypt(String data) {
        try {
            if (!encryptionProperties.isEnabled()) {
                return data;
            }
            SecretKeySpec keySpec = new SecretKeySpec(encryptionProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            
            // 生成随机IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
            
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
     * 解密数据
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
            
            SecretKeySpec keySpec = new SecretKeySpec(encryptionProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
            
            byte[] decryptedData = cipher.doFinal(encrypted);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }
    
    /**
     * 加密对象为JSON字符串
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
     * 解密JSON字符串为对象
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
     * 生成加密响应包装器
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
     * 生成密钥（用于前端）
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
