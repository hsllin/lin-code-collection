package com.lin.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 
 * @author lin
 */
//@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret:MySecretKey123456789012345678901234567890}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    @Value("${jwt.refresh-expiration:604800}")
    private Long refreshExpiration;

    /**
     * 生成JWT令牌
     */
    public String generateToken(String username, Map<String, Object> claims) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration * 1000);

            Map<String, Object> headerClaims = new HashMap<>();
            headerClaims.put("typ", "JWT");
            headerClaims.put("alg", "HS256");

            return Jwts.builder()
                    .setHeader(headerClaims)
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("生成JWT令牌时发生错误", e);
            return null;
        }
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(String username) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + refreshExpiration * 1000);

            Map<String, Object> claims = new HashMap<>();
            claims.put("type", "refresh");

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("生成刷新令牌时发生错误", e);
            return null;
        }
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            logger.error("从令牌中获取用户名时发生错误", e);
            return null;
        }
    }

    /**
     * 从令牌中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            logger.error("从令牌中获取过期时间时发生错误", e);
            return null;
        }
    }

    /**
     * 从令牌中获取声明
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("从令牌中获取声明时发生错误", e);
            return null;
        }
    }

    /**
     * 检查令牌是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.error("检查令牌是否过期时发生错误", e);
            return true;
        }
    }

    /**
     * 验证令牌
     */
    public Boolean validateToken(String token, String username) {
        try {
            String tokenUsername = getUsernameFromToken(token);
            return (username.equals(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            logger.error("验证令牌时发生错误", e);
            return false;
        }
    }

    /**
     * 验证令牌格式
     */
    public Boolean validateTokenFormat(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("令牌格式错误", e);
            return false;
        } catch (ExpiredJwtException e) {
            logger.error("令牌已过期", e);
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的令牌", e);
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("令牌参数错误", e);
            return false;
        } catch (Exception e) {
            logger.error("验证令牌格式时发生错误", e);
            return false;
        }
    }

    /**
     * 刷新令牌
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String username = claims.getSubject();
            
            // 移除过期时间声明
            claims.remove(Claims.EXPIRATION);
            claims.remove(Claims.ISSUED_AT);
            
            return generateToken(username, claims);
        } catch (Exception e) {
            logger.error("刷新令牌时发生错误", e);
            return null;
        }
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从请求头中获取令牌
     */
    public String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 检查令牌是否为刷新令牌
     */
    public Boolean isRefreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return "refresh".equals(claims.get("type"));
        } catch (Exception e) {
            logger.error("检查令牌类型时发生错误", e);
            return false;
        }
    }

    /**
     * 获取令牌剩余有效时间（秒）
     */
    public Long getTokenRemainingTime(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            if (expiration != null) {
                long remainingTime = (expiration.getTime() - System.currentTimeMillis()) / 1000;
                return Math.max(0, remainingTime);
            }
        } catch (Exception e) {
            logger.error("获取令牌剩余时间时发生错误", e);
        }
        return 0L;
    }

    /**
     * 检查令牌是否即将过期（剩余时间少于1小时）
     */
    public Boolean isTokenExpiringSoon(String token) {
        Long remainingTime = getTokenRemainingTime(token);
        return remainingTime < 3600; // 1小时
    }
}
