package com.lin.controller;

import com.lin.annotation.EncryptResponse;
import com.lin.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存管理Controller
 */
@RestController
@RequestMapping("/cache")
public class CacheManageController {
    
    @Autowired
    private CacheService cacheService;
    
    /**
     * 获取缓存统计信息
     */
    @GetMapping("/stats")
    @EncryptResponse(encryptAll = true)
    public Map<String, Object> getCacheStats() {
        Map<String, Object> result = new HashMap<>();
        result.put("stats", cacheService.getCacheStats());
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
    
    /**
     * 清空所有缓存
     */
    @PostMapping("/clear")
    @EncryptResponse(encryptAll = true)
    public Map<String, Object> clearAllCache() {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.clearAll();
            result.put("success", true);
            result.put("message", "所有缓存已清空");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "清空缓存失败: " + e.getMessage());
        }
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
    
    /**
     * 删除指定缓存
     */
    @DeleteMapping("/{key}")
    @EncryptResponse(encryptAll = true)
    public Map<String, Object> deleteCache(@PathVariable String key) {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.delete(key);
            result.put("success", true);
            result.put("message", "缓存已删除: " + key);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除缓存失败: " + e.getMessage());
        }
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
    
    /**
     * 检查缓存是否存在
     */
    @GetMapping("/exists/{key}")
    @EncryptResponse(encryptAll = true)
    public Map<String, Object> checkCacheExists(@PathVariable String key) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = cacheService.exists(key);
            result.put("exists", exists);
            result.put("key", key);
            if (exists) {
                result.put("ttl", cacheService.getTtl(key));
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "检查缓存失败: " + e.getMessage());
        }
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
    
    /**
     * 删除匹配模式的缓存
     */
    @DeleteMapping("/pattern/{pattern}")
    @EncryptResponse(encryptAll = true)
    public Map<String, Object> deleteCachePattern(@PathVariable String pattern) {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.deletePattern(pattern);
            result.put("success", true);
            result.put("message", "匹配模式的缓存已删除: " + pattern);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除缓存失败: " + e.getMessage());
        }
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}
