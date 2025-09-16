package com.lin.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.annotation.EncryptResponse;
import com.lin.util.EncryptionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 加密响应切面
 * 自动处理标记了@EncryptResponse注解的方法返回值
 */
@Aspect
@Component
public class EncryptAspect {
    
    @Autowired
    private EncryptionUtil encryptionUtil;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Around("@annotation(encryptResponse)")
    public Object around(ProceedingJoinPoint joinPoint, EncryptResponse encryptResponse) throws Throwable {
        // 执行原方法
        Object result = joinPoint.proceed();
        
        // 如果返回的是ResponseEntity，需要特殊处理
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();
            
            if (body != null) {
                if (encryptResponse.encryptAll()) {
                    // 加密整个响应体
                    Map<String, Object> encryptedResponse = encryptionUtil.createEncryptedResponse(body);
                    return ResponseEntity.ok(encryptedResponse);
                } else {
                    // 只加密指定字段
                    Object encryptedBody = encryptSpecificFields(body, encryptResponse.fields());
                    return ResponseEntity.ok(encryptedBody);
                }
            }
        } else if (result != null) {
            // 处理普通返回值
            if (encryptResponse.encryptAll()) {
                return encryptionUtil.createEncryptedResponse(result);
            } else {
                return encryptSpecificFields(result, encryptResponse.fields());
            }
        }
        
        return result;
    }
    
    /**
     * 加密指定字段
     */
    private Object encryptSpecificFields(Object data, String[] fields) {
        try {
            if (fields.length == 0) {
                return data;
            }
            
            // 将对象转换为Map进行字段级加密
            String json = objectMapper.writeValueAsString(data);
            Map<String, Object> dataMap = objectMapper.readValue(json, Map.class);
            
            Map<String, Object> result = new HashMap<>();
            result.put("encrypted", true);
            result.put("timestamp", System.currentTimeMillis());
            
            // 加密指定字段
            Map<String, Object> encryptedFields = new HashMap<>();
            for (String field : fields) {
                if (dataMap.containsKey(field)) {
                    Object fieldValue = dataMap.get(field);
                    if (fieldValue != null) {
                        encryptedFields.put(field, encryptionUtil.encryptObject(fieldValue));
                    }
                }
            }
            
            result.put("encryptedFields", encryptedFields);
            
            // 保留未加密字段
            Map<String, Object> plainFields = new HashMap<>();
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                boolean shouldEncrypt = false;
                for (String field : fields) {
                    if (field.equals(entry.getKey())) {
                        shouldEncrypt = true;
                        break;
                    }
                }
                if (!shouldEncrypt) {
                    plainFields.put(entry.getKey(), entry.getValue());
                }
            }
            
            if (!plainFields.isEmpty()) {
                result.put("plainFields", plainFields);
            }
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("字段加密失败", e);
        }
    }
}

