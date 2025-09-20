package com.lin.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * CSRF防护工具类
 * 
 * @author lin
 */
public class CsrfUtil {

    private static final Logger logger = LoggerFactory.getLogger(CsrfUtil.class);

    // CSRF令牌存储
    private static final ConcurrentHashMap<String, CsrfToken> tokenStore = new ConcurrentHashMap<>();
    
    // 令牌过期时间（30分钟）
    private static final long TOKEN_EXPIRE_TIME = TimeUnit.MINUTES.toMillis(30);
    
    // 随机数生成器
    private static final SecureRandom secureRandom = new SecureRandom();
    
    // 密码编码器
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 生成CSRF令牌
     */
    public static String generateToken() {
        try {
            // 生成随机字节
            byte[] randomBytes = new byte[32];
            secureRandom.nextBytes(randomBytes);
            
            // 编码为Base64字符串
            String token = Base64.getEncoder().encodeToString(randomBytes);
            
            // 存储令牌信息
            CsrfToken csrfToken = new CsrfToken(token, System.currentTimeMillis());
            tokenStore.put(token, csrfToken);
            
            logger.debug("生成CSRF令牌: {}", token);
            return token;
        } catch (Exception e) {
            logger.error("生成CSRF令牌时发生错误", e);
            return null;
        }
    }

    /**
     * 验证CSRF令牌
     */
    public static boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            CsrfToken csrfToken = tokenStore.get(token);
            if (csrfToken == null) {
                logger.debug("CSRF令牌不存在: {}", token);
                return false;
            }

            // 检查令牌是否过期
            if (System.currentTimeMillis() - csrfToken.getCreateTime() > TOKEN_EXPIRE_TIME) {
                tokenStore.remove(token);
                logger.debug("CSRF令牌已过期: {}", token);
                return false;
            }

            return true;
        } catch (Exception e) {
            logger.error("验证CSRF令牌时发生错误", e);
            return false;
        }
    }

    /**
     * 验证CSRF令牌（从请求中获取）
     */
    public static boolean validateTokenFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return validateToken(token);
    }

    /**
     * 从请求中获取CSRF令牌
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        // 优先从请求头获取
        String token = request.getHeader("X-CSRF-TOKEN");
        if (token != null && !token.isEmpty()) {
            return token;
        }

        // 从请求参数获取
        token = request.getParameter("_csrf");
        if (token != null && !token.isEmpty()) {
            return token;
        }

        // 从会话中获取
        HttpSession session = request.getSession(false);
        if (session != null) {
            token = (String) session.getAttribute("_csrf");
            if (token != null && !token.isEmpty()) {
                return token;
            }
        }

        return null;
    }

    /**
     * 将CSRF令牌存储到会话中
     */
    public static void storeTokenInSession(HttpServletRequest request, String token) {
        HttpSession session = request.getSession(true);
        session.setAttribute("_csrf", token);
        logger.debug("CSRF令牌已存储到会话中: {}", token);
    }

    /**
     * 从会话中获取CSRF令牌
     */
    public static String getTokenFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("_csrf");
        }
        return null;
    }

    /**
     * 移除CSRF令牌
     */
    public static void removeToken(String token) {
        if (token != null) {
            tokenStore.remove(token);
            logger.debug("CSRF令牌已移除: {}", token);
        }
    }

    /**
     * 清理过期的CSRF令牌
     */
    public static void cleanExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        tokenStore.entrySet().removeIf(entry -> {
            CsrfToken token = entry.getValue();
            boolean expired = currentTime - token.getCreateTime() > TOKEN_EXPIRE_TIME;
            if (expired) {
                logger.debug("清理过期CSRF令牌: {}", entry.getKey());
            }
            return expired;
        });
    }

    /**
     * 生成CSRF令牌并存储到会话中
     */
    public static String generateAndStoreToken(HttpServletRequest request) {
        String token = generateToken();
        if (token != null) {
            storeTokenInSession(request, token);
        }
        return token;
    }

    /**
     * 检查请求是否需要CSRF保护
     */
    public static boolean requiresCsrfProtection(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        
        // GET、HEAD、OPTIONS请求通常不需要CSRF保护
        if ("GET".equals(method) || "HEAD".equals(method) || "OPTIONS".equals(method)) {
            return false;
        }
        
        // 静态资源不需要CSRF保护
        if (uri.startsWith("/static/") || uri.startsWith("/css/") || 
            uri.startsWith("/js/") || uri.startsWith("/images/")) {
            return false;
        }
        
        // API接口需要CSRF保护
        if (uri.startsWith("/api/")) {
            return true;
        }
        
        return true;
    }

    /**
     * 生成CSRF令牌的HTML隐藏字段
     */
    public static String generateCsrfTokenField(String token) {
        if (token == null || token.isEmpty()) {
            return "";
        }
        return "<input type=\"hidden\" name=\"_csrf\" value=\"" + token + "\"/>";
    }

    /**
     * 生成CSRF令牌的Meta标签
     */
    public static String generateCsrfTokenMeta(String token) {
        if (token == null || token.isEmpty()) {
            return "";
        }
        return "<meta name=\"_csrf\" content=\"" + token + "\"/>";
    }

    /**
     * CSRF令牌内部类
     */
    private static class CsrfToken {
        private final String token;
        private final long createTime;

        public CsrfToken(String token, long createTime) {
            this.token = token;
            this.createTime = createTime;
        }

        public String getToken() {
            return token;
        }

        public long getCreateTime() {
            return createTime;
        }
    }
}
