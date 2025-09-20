package com.lin.security.filter;

import com.lin.security.service.SimpleBucketRateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求频率限制过滤器
 * 
 * @author lin
 */
@Component
@Order(2)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    @Autowired
    private SimpleBucketRateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String clientIp = getClientIpAddress(request);
        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        try {
            // 检查是否超过频率限制
            if (!rateLimitService.isAllowed(clientIp, requestUri, method)) {
                logger.warn("请求频率超限，IP: {}, URI: {}, Method: {}", clientIp, requestUri, method);
                
                response.setStatus(429); // Too Many Requests
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"请求过于频繁，请稍后再试\"}");
                return;
            }

            // 记录请求
            rateLimitService.recordRequest(clientIp, requestUri, method);
            
        } catch (Exception e) {
            logger.error("限流检查时发生错误", e);
            // 发生错误时允许请求通过，避免影响正常业务
        }

        filterChain.doFilter(request, response);
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
