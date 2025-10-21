package com.lin.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 安全响应头过滤器
 * 
 * @author lin
 */
//@Component
//@Order(6)
public class SecurityHeadersFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityHeadersFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 添加安全响应头
        addSecurityHeaders(response);
        
        filterChain.doFilter(request, response);
    }

    /**
     * 添加安全响应头
     */
    private void addSecurityHeaders(HttpServletResponse response) {
        try {
            // 防止点击劫持
            response.setHeader("X-Frame-Options", "DENY");
            
            // 防止MIME类型嗅探
            response.setHeader("X-Content-Type-Options", "nosniff");
            
            // XSS保护
            response.setHeader("X-XSS-Protection", "1; mode=block");
            
            // 引用者策略
            response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
            
            // 内容安全策略
            response.setHeader("Content-Security-Policy", 
                "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com; " +
                "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com; " +
                "img-src 'self' data: https:; " +
                "font-src 'self' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com; " +
                "connect-src 'self'; " +
                "frame-ancestors 'none'; " +
                "base-uri 'self'; " +
                "form-action 'self'");
            
            // 权限策略
            response.setHeader("Permissions-Policy", 
                "geolocation=(), " +
                "microphone=(), " +
                "camera=(), " +
                "payment=(), " +
                "usb=(), " +
                "magnetometer=(), " +
                "gyroscope=(), " +
                "accelerometer=(), " +
                "ambient-light-sensor=(), " +
                "autoplay=(), " +
                "battery=(), " +
                "bluetooth=(), " +
                "clipboard-read=(), " +
                "clipboard-write=(), " +
                "display-capture=(), " +
                "fullscreen=(), " +
                "gamepad=(), " +
                "hid=(), " +
                "idle-detection=(), " +
                "local-fonts=(), " +
                "midi=(), " +
                "notifications=(), " +
                "persistent-storage=(), " +
                "push=(), " +
                "screen-wake-lock=(), " +
                "serial=(), " +
                "speaker-selection=(), " +
                "storage-access=(), " +
                "sync-xhr=(), " +
                "unoptimized-images=(), " +
                "vertical-scroll=(), " +
                "web-share=(), " +
                "xr-spatial-tracking=()");
            
            // 严格传输安全
            response.setHeader("Strict-Transport-Security", 
                "max-age=31536000; includeSubDomains; preload");
            
            // 跨域资源共享
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", 
                "Content-Type, Authorization, X-Requested-With, X-CSRF-TOKEN, X-Timestamp, X-Nonce, X-Signature");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "3600");
            
            // 缓存控制
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            // 服务器信息隐藏
            response.setHeader("Server", "Apache/2.4.1");
            
            // 防止信息泄露
            response.setHeader("X-Powered-By", "");
            
            logger.debug("安全响应头已添加");
            
        } catch (Exception e) {
            logger.error("添加安全响应头时发生错误", e);
        }
    }
}
