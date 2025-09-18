package com.lin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 缓存配置类
 * 用于管理Redis缓存的全局配置
 */
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheConfig {
    
    /**
     * 是否启用缓存
     */
    private boolean enabled = true;
    
    /**
     * 默认缓存时间（秒）
     */
    private long defaultTtl = 100; // 10秒
    
    /**
     * 股票数据缓存时间（秒）
     */
    private long stockDataTtl = 100; // 10秒
    
    /**
     * 热门数据缓存时间（秒）
     */
    private long hotDataTtl = 180; // 3分钟
    
    /**
     * 实时数据缓存时间（秒）
     */
    private long realtimeDataTtl = 60; // 1分钟
    
    /**
     * 统计数据缓存时间（秒）
     */
    private long statisticsDataTtl = 600; // 10分钟
    
    /**
     * 缓存键前缀
     */
    private String keyPrefix = "stock:";
    
    /**
     * 是否启用缓存预热
     */
    private boolean preloadEnabled = false;
    
    /**
     * 缓存预热时间间隔（秒）
     */
    private long preloadInterval = 3600; // 1小时

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getDefaultTtl() {
        return defaultTtl;
    }

    public void setDefaultTtl(long defaultTtl) {
        this.defaultTtl = defaultTtl;
    }

    public long getStockDataTtl() {
        return stockDataTtl;
    }

    public void setStockDataTtl(long stockDataTtl) {
        this.stockDataTtl = stockDataTtl;
    }

    public long getHotDataTtl() {
        return hotDataTtl;
    }

    public void setHotDataTtl(long hotDataTtl) {
        this.hotDataTtl = hotDataTtl;
    }

    public long getRealtimeDataTtl() {
        return realtimeDataTtl;
    }

    public void setRealtimeDataTtl(long realtimeDataTtl) {
        this.realtimeDataTtl = realtimeDataTtl;
    }

    public long getStatisticsDataTtl() {
        return statisticsDataTtl;
    }

    public void setStatisticsDataTtl(long statisticsDataTtl) {
        this.statisticsDataTtl = statisticsDataTtl;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public boolean isPreloadEnabled() {
        return preloadEnabled;
    }

    public void setPreloadEnabled(boolean preloadEnabled) {
        this.preloadEnabled = preloadEnabled;
    }

    public long getPreloadInterval() {
        return preloadInterval;
    }

    public void setPreloadInterval(long preloadInterval) {
        this.preloadInterval = preloadInterval;
    }
}
