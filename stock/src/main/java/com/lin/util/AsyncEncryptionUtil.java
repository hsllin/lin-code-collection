package com.lin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.config.EncryptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 异步加密工具类
 * 通过异步处理提升响应速度
 */
@Component
public class AsyncEncryptionUtil {
    
    private final ObjectMapper objectMapper;
    private final EncryptionProperties encryptionProperties;
    private final FastEncryptionUtil fastEncryptionUtil;
    
    @Autowired
    public AsyncEncryptionUtil(EncryptionProperties encryptionProperties, FastEncryptionUtil fastEncryptionUtil) {
        this.objectMapper = new ObjectMapper();
        this.encryptionProperties = encryptionProperties;
        this.fastEncryptionUtil = fastEncryptionUtil;
    }
    
    /**
     * 异步加密数据
     * @param data 要加密的数据
     * @return 异步加密结果
     */
    @Async
    public CompletableFuture<String> encryptAsync(String data) {
        return CompletableFuture.completedFuture(fastEncryptionUtil.encrypt(data));
    }
    
    /**
     * 异步解密数据
     * @param encryptedData 加密的Base64字符串
     * @return 异步解密结果
     */
    @Async
    public CompletableFuture<String> decryptAsync(String encryptedData) {
        return CompletableFuture.completedFuture(fastEncryptionUtil.decrypt(encryptedData));
    }
    
    /**
     * 异步加密对象
     * @param obj 要加密的对象
     * @return 异步加密结果
     */
    @Async
    public CompletableFuture<String> encryptObjectAsync(Object obj) {
        return CompletableFuture.completedFuture(fastEncryptionUtil.encryptObject(obj));
    }
    
    /**
     * 异步解密对象
     * @param encryptedData 加密的Base64字符串
     * @param clazz 目标类型
     * @return 异步解密结果
     */
    @Async
    public <T> CompletableFuture<T> decryptObjectAsync(String encryptedData, Class<T> clazz) {
        return CompletableFuture.completedFuture(fastEncryptionUtil.decryptObject(encryptedData, clazz));
    }
    
    /**
     * 异步生成加密响应包装器
     * @param data 原始数据
     * @return 异步加密响应结果
     */
    @Async
    public CompletableFuture<Map<String, Object>> createEncryptedResponseAsync(Object data) {
        return CompletableFuture.completedFuture(fastEncryptionUtil.createEncryptedResponse(data));
    }
    
    /**
     * 同步加密（用于需要立即返回的场景）
     * @param data 要加密的数据
     * @return 加密后的Base64字符串
     */
    public String encrypt(String data) {
        return fastEncryptionUtil.encrypt(data);
    }
    
    /**
     * 同步解密（用于需要立即返回的场景）
     * @param encryptedData 加密的Base64字符串
     * @return 解密后的原始数据
     */
    public String decrypt(String encryptedData) {
        return fastEncryptionUtil.decrypt(encryptedData);
    }
    
    /**
     * 同步加密对象
     * @param obj 要加密的对象
     * @return 加密后的Base64字符串
     */
    public String encryptObject(Object obj) {
        return fastEncryptionUtil.encryptObject(obj);
    }
    
    /**
     * 同步解密对象
     * @param encryptedData 加密的Base64字符串
     * @param clazz 目标类型
     * @return 解密后的对象
     */
    public <T> T decryptObject(String encryptedData, Class<T> clazz) {
        return fastEncryptionUtil.decryptObject(encryptedData, clazz);
    }
    
    /**
     * 同步生成加密响应包装器
     * @param data 原始数据
     * @return 包含加密数据的响应包装器
     */
    public Map<String, Object> createEncryptedResponse(Object data) {
        return fastEncryptionUtil.createEncryptedResponse(data);
    }
    
    /**
     * 生成密钥信息
     * @return 密钥信息
     */
    public Map<String, String> generateKeyInfo() {
        return fastEncryptionUtil.generateKeyInfo();
    }
}

