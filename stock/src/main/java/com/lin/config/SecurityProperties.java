package com.lin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 安全配置属性
 * 
 * @author lin
 */
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * 重放攻击防护配置
     */
    private ReplayAttack replayAttack = new ReplayAttack();

    /**
     * 限流配置
     */
    private RateLimit rateLimit = new RateLimit();

    /**
     * XSS防护配置
     */
    private Xss xss = new Xss();

    /**
     * SQL注入防护配置
     */
    private SqlInjection sqlInjection = new SqlInjection();

    /**
     * CSRF防护配置
     */
    private Csrf csrf = new Csrf();

    public static class Jwt {
        private String secret = "MySecretKey123456789012345678901234567890";
        private Long expiration = 86400L; // 24小时
        private Long refreshExpiration = 604800L; // 7天

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Long getExpiration() {
            return expiration;
        }

        public void setExpiration(Long expiration) {
            this.expiration = expiration;
        }

        public Long getRefreshExpiration() {
            return refreshExpiration;
        }

        public void setRefreshExpiration(Long refreshExpiration) {
            this.refreshExpiration = refreshExpiration;
        }
    }

    public static class ReplayAttack {
        private String secretKey = "defaultSecretKey";
        private Long window = 300000L; // 5分钟
        private Long expireTime = 600000L; // 10分钟

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public Long getWindow() {
            return window;
        }

        public void setWindow(Long window) {
            this.window = window;
        }

        public Long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Long expireTime) {
            this.expireTime = expireTime;
        }
    }

    public static class RateLimit {
        private Integer defaultCapacity = 100;
        private Integer defaultRefillTokens = 10;
        private Long defaultRefillDuration = 60000L; // 1分钟
        private Integer apiCapacity = 1000;
        private Integer apiRefillTokens = 100;
        private Long apiRefillDuration = 60000L; // 1分钟
        private Integer loginCapacity = 5;
        private Integer loginRefillTokens = 1;
        private Long loginRefillDuration = 60000L; // 1分钟

        public Integer getDefaultCapacity() {
            return defaultCapacity;
        }

        public void setDefaultCapacity(Integer defaultCapacity) {
            this.defaultCapacity = defaultCapacity;
        }

        public Integer getDefaultRefillTokens() {
            return defaultRefillTokens;
        }

        public void setDefaultRefillTokens(Integer defaultRefillTokens) {
            this.defaultRefillTokens = defaultRefillTokens;
        }

        public Long getDefaultRefillDuration() {
            return defaultRefillDuration;
        }

        public void setDefaultRefillDuration(Long defaultRefillDuration) {
            this.defaultRefillDuration = defaultRefillDuration;
        }

        public Integer getApiCapacity() {
            return apiCapacity;
        }

        public void setApiCapacity(Integer apiCapacity) {
            this.apiCapacity = apiCapacity;
        }

        public Integer getApiRefillTokens() {
            return apiRefillTokens;
        }

        public void setApiRefillTokens(Integer apiRefillTokens) {
            this.apiRefillTokens = apiRefillTokens;
        }

        public Long getApiRefillDuration() {
            return apiRefillDuration;
        }

        public void setApiRefillDuration(Long apiRefillDuration) {
            this.apiRefillDuration = apiRefillDuration;
        }

        public Integer getLoginCapacity() {
            return loginCapacity;
        }

        public void setLoginCapacity(Integer loginCapacity) {
            this.loginCapacity = loginCapacity;
        }

        public Integer getLoginRefillTokens() {
            return loginRefillTokens;
        }

        public void setLoginRefillTokens(Integer loginRefillTokens) {
            this.loginRefillTokens = loginRefillTokens;
        }

        public Long getLoginRefillDuration() {
            return loginRefillDuration;
        }

        public void setLoginRefillDuration(Long loginRefillDuration) {
            this.loginRefillDuration = loginRefillDuration;
        }
    }

    public static class Xss {
        private Boolean enabled = true;
        private Boolean logAttacks = true;
        private Boolean blockAttacks = true;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Boolean getLogAttacks() {
            return logAttacks;
        }

        public void setLogAttacks(Boolean logAttacks) {
            this.logAttacks = logAttacks;
        }

        public Boolean getBlockAttacks() {
            return blockAttacks;
        }

        public void setBlockAttacks(Boolean blockAttacks) {
            this.blockAttacks = blockAttacks;
        }
    }

    public static class SqlInjection {
        private Boolean enabled = true;
        private Boolean logAttacks = true;
        private Boolean blockAttacks = true;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Boolean getLogAttacks() {
            return logAttacks;
        }

        public void setLogAttacks(Boolean logAttacks) {
            this.logAttacks = logAttacks;
        }

        public Boolean getBlockAttacks() {
            return blockAttacks;
        }

        public void setBlockAttacks(Boolean blockAttacks) {
            this.blockAttacks = blockAttacks;
        }
    }

    public static class Csrf {
        private Boolean enabled = true;
        private Long tokenExpireTime = 1800000L; // 30分钟

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Long getTokenExpireTime() {
            return tokenExpireTime;
        }

        public void setTokenExpireTime(Long tokenExpireTime) {
            this.tokenExpireTime = tokenExpireTime;
        }
    }

    // Getters and Setters
    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public ReplayAttack getReplayAttack() {
        return replayAttack;
    }

    public void setReplayAttack(ReplayAttack replayAttack) {
        this.replayAttack = replayAttack;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public Xss getXss() {
        return xss;
    }

    public void setXss(Xss xss) {
        this.xss = xss;
    }

    public SqlInjection getSqlInjection() {
        return sqlInjection;
    }

    public void setSqlInjection(SqlInjection sqlInjection) {
        this.sqlInjection = sqlInjection;
    }

    public Csrf getCsrf() {
        return csrf;
    }

    public void setCsrf(Csrf csrf) {
        this.csrf = csrf;
    }
}
