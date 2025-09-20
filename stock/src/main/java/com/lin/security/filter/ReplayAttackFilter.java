package com.lin.security.filter;

import com.lin.security.util.ReplayAttackUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 重放攻击防护过滤器
 * 
 * @author lin
 */
@Component
@Order(5)
public class ReplayAttackFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ReplayAttackFilter.class);

    @Autowired
    private ReplayAttackUtil replayAttackUtil;

    @Value("${security.replay-attack.secret-key:defaultSecretKey}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 检查请求是否需要重放攻击防护
        if (requiresReplayProtection(request)) {
            // 验证请求签名
            if (!validateRequestSignature(request)) {
                String clientIp = getClientIpAddress(request);
                logger.warn("重放攻击检测，IP: {}, User-Agent: {}, URI: {}", 
                           clientIp, 
                           request.getHeader("User-Agent"), 
                           request.getRequestURI());
                
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"请求签名验证失败\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 检查请求是否需要重放攻击防护
     */
    private boolean requiresReplayProtection(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        
        // GET、HEAD、OPTIONS请求通常不需要重放攻击防护
        if ("GET".equals(method) || "HEAD".equals(method) || "OPTIONS".equals(method)) {
            return false;
        }
        
        // 静态资源不需要重放攻击防护
        if (uri.startsWith("/static/") || uri.startsWith("/css/") || 
            uri.startsWith("/js/") || uri.startsWith("/images/")) {
            return false;
        }
        
        // API接口需要重放攻击防护
        if (uri.startsWith("/api/")) {
            return true;
        }
        
        return false;
    }

    /**
     * 验证请求签名
     */
    private boolean validateRequestSignature(HttpServletRequest request) {
        try {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String body = getRequestBody(request);
            
            // 从请求头获取签名信息
            String timestamp = request.getHeader("X-Timestamp");
            String nonce = request.getHeader("X-Nonce");
            String signature = request.getHeader("X-Signature");
            
            if (timestamp == null || nonce == null || signature == null) {
                logger.warn("缺少必要的签名信息");
                return false;
            }
            
            // 验证签名
            return !replayAttackUtil.isReplayAttack(method, uri, body, timestamp, nonce, signature, secretKey);
            
        } catch (Exception e) {
            logger.error("验证请求签名时发生错误", e);
            return false;
        }
    }

    /**
     * 获取请求体内容
     */
    private String getRequestBody(HttpServletRequest request) {
        try {
            // 这里需要根据实际情况获取请求体内容
            // 由于HttpServletRequest的输入流只能读取一次，这里返回空字符串
            // 在实际应用中，可能需要使用自定义的RequestWrapper来缓存请求体
            return "";
        } catch (Exception e) {
            logger.error("获取请求体内容时发生错误", e);
            return "";
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
