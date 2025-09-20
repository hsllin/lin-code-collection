package com.lin.security.filter;

import com.lin.security.util.SqlInjectionUtil;
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
import java.util.Enumeration;
import java.util.Map;

/**
 * SQL注入防护过滤器
 * 
 * @author lin
 */
@Component
@Order(3)
public class SqlInjectionFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SqlInjectionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 检查请求是否包含SQL注入攻击
        if (containsSqlInjection(request)) {
            String clientIp = getClientIpAddress(request);
            logger.warn("检测到SQL注入攻击尝试，IP: {}, User-Agent: {}, URI: {}", 
                       clientIp, 
                       request.getHeader("User-Agent"), 
                       request.getRequestURI());
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"检测到恶意请求，请求被拒绝\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 检查请求是否包含SQL注入攻击
     */
    private boolean containsSqlInjection(HttpServletRequest request) {
        // 检查请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            // 检查参数名
            if (SqlInjectionUtil.containsSqlInjection(entry.getKey())) {
                return true;
            }
            
            // 检查参数值
            for (String value : entry.getValue()) {
                if (SqlInjectionUtil.containsSqlInjection(value)) {
                    return true;
                }
            }
        }

        // 检查请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            
            // 检查请求头名
            if (SqlInjectionUtil.containsSqlInjection(headerName)) {
                return true;
            }
            
            // 检查请求头值
            if (SqlInjectionUtil.containsSqlInjection(headerValue)) {
                return true;
            }
        }

        // 检查URI
        String requestURI = request.getRequestURI();
        if (SqlInjectionUtil.containsSqlInjection(requestURI)) {
            return true;
        }

        // 检查查询字符串
        String queryString = request.getQueryString();
        if (queryString != null && SqlInjectionUtil.containsSqlInjection(queryString)) {
            return true;
        }

        return false;
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
