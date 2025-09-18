package com.lin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.config.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务类
 * 使用Redis实现分布式缓存
 */
@Service
public class CacheService {
    
    @Autowired
    private CacheConfig cacheConfig;
    
    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 获取缓存数据
     * @param key 缓存键
     * @param clazz 返回类型
     * @return 缓存数据
     */
    public <T> T get(String key, Class<T> clazz) {
        if (!cacheConfig.isEnabled()) {
            return null;
        }
        
        try {
            String jsonValue = stringRedisTemplate.opsForValue().get(key);
            if (jsonValue == null) {
                return null;
            }
            
            // 手动反序列化JSON
            return objectMapper.readValue(jsonValue, clazz);
        } catch (Exception e) {
            System.err.println("获取缓存失败: " + key + ", 错误: " + e.getMessage());
            e.printStackTrace(); // 添加详细错误信息
            return null;
        }
    }
    
    /**
     * 设置缓存数据
     * @param key 缓存键
     * @param value 缓存值
     * @param ttl 过期时间（秒）
     */
    public void set(String key, Object value, long ttl) {
        if (!cacheConfig.isEnabled()) {
            return;
        }
        
        try {
            if (value == null) {
                return;
            }
            
            // 手动序列化为JSON字符串
            String jsonValue = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("设置缓存失败: " + key + ", 错误: " + e.getMessage());
            e.printStackTrace(); // 添加详细错误信息
        }
    }
    
    /**
     * 设置缓存数据（使用默认TTL）
     * @param key 缓存键
     * @param value 缓存值
     */
    public void set(String key, Object value) {
        set(key, value, cacheConfig.getDefaultTtl());
    }
    
    /**
     * 删除缓存
     * @param key 缓存键
     */
    public void delete(String key) {
        if (!cacheConfig.isEnabled()) {
            return;
        }

        stringRedisTemplate.delete(key);
    }
    
    /**
     * 删除匹配的缓存
     * @param pattern 匹配模式
     */
    public void deletePattern(String pattern) {
        if (!cacheConfig.isEnabled()) {
            return;
        }

        stringRedisTemplate.delete(stringRedisTemplate.keys(pattern));
    }
    
    /**
     * 检查缓存是否存在
     * @param key 缓存键
     * @return 是否存在
     */
    public boolean exists(String key) {
        if (!cacheConfig.isEnabled()) {
            return false;
        }
        
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
    
    /**
     * 获取缓存TTL
     * @param key 缓存键
     * @return TTL（秒）
     */
    public long getTtl(String key) {
        if (!cacheConfig.isEnabled()) {
            return -1;
        }
        
        Long ttl = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl != null ? ttl : -1;
    }
    
    /**
     * 根据缓存类型获取TTL
     * @param type 缓存类型
     * @return TTL（秒）
     */
    public long getTtlByType(com.lin.annotation.Cacheable.CacheType type) {
        switch (type) {
            case STOCK_DATA:
                return cacheConfig.getStockDataTtl();
            case HOT_DATA:
                return cacheConfig.getHotDataTtl();
            case REALTIME_DATA:
                return cacheConfig.getRealtimeDataTtl();
            case STATISTICS_DATA:
                return cacheConfig.getStatisticsDataTtl();
            default:
                return cacheConfig.getDefaultTtl();
        }
    }
    
    /**
     * 生成完整的缓存键
     * @param key 原始键
     * @return 完整键
     */
    public String generateKey(String key) {
        return cacheConfig.getKeyPrefix() + key;
    }
    
    /**
     * 清空所有缓存
     */
    public void clearAll() {
        if (!cacheConfig.isEnabled()) {
            return;
        }

        stringRedisTemplate.delete(stringRedisTemplate.keys(cacheConfig.getKeyPrefix() + "*"));
    }
    
    /**
     * 获取缓存统计信息
     * @return 缓存统计信息
     */
    public String getCacheStats() {
        if (!cacheConfig.isEnabled()) {
            return "缓存已禁用";
        }
        
        try {
            long totalKeys = stringRedisTemplate.keys(cacheConfig.getKeyPrefix() + "*").size();
            return String.format("Redis缓存统计 - 总键数: %d, 前缀: %s, 默认TTL: %d秒", 
                    totalKeys, cacheConfig.getKeyPrefix(), cacheConfig.getDefaultTtl());
        } catch (Exception e) {
            return "获取缓存统计失败: " + e.getMessage();
        }
    }
}
