package com.lin.security.filter;

import com.lin.security.util.CsrfUtil;
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
 * CSRF防护过滤器
 * 
 * @author lin
 */
@Component
@Order(4)
public class CsrfFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CsrfFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 检查请求是否需要CSRF保护
        if (CsrfUtil.requiresCsrfProtection(request)) {
            // 验证CSRF令牌
            if (!CsrfUtil.validateTokenFromRequest(request)) {
                String clientIp = getClientIpAddress(request);
                logger.warn("CSRF攻击检测，IP: {}, User-Agent: {}, URI: {}", 
                           clientIp, 
                           request.getHeader("User-Agent"), 
                           request.getRequestURI());
                
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"CSRF令牌验证失败\"}");
                return;
            }
        }

        // 为GET请求生成CSRF令牌
        if ("GET".equals(request.getMethod())) {
            String token = CsrfUtil.getTokenFromSession(request);
            if (token == null) {
                token = CsrfUtil.generateAndStoreToken(request);
            }
            
            // 将令牌添加到响应头
            if (token != null) {
                response.setHeader("X-CSRF-TOKEN", token);
            }
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
