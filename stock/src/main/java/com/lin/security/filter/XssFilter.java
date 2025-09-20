package com.lin.security.filter;

import com.lin.security.util.XssUtil;
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
import java.util.HashMap;
import java.util.Map;

/**
 * XSS攻击防护过滤器
 * 
 * @author lin
 */
@Component
@Order(1)
public class XssFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(XssFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 检查请求是否包含XSS攻击
        if (containsXssAttack(request)) {
            logger.warn("检测到XSS攻击尝试，IP: {}, User-Agent: {}, URI: {}", 
                       getClientIpAddress(request), 
                       request.getHeader("User-Agent"), 
                       request.getRequestURI());
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"检测到恶意请求，请求被拒绝\"}");
            return;
        }

        // 包装请求对象以过滤XSS内容
        XssHttpServletRequestWrapper wrappedRequest = new XssHttpServletRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);
    }

    /**
     * 检查请求是否包含XSS攻击
     */
    private boolean containsXssAttack(HttpServletRequest request) {
        // 检查请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            for (String value : entry.getValue()) {
                if (XssUtil.containsXss(value)) {
                    return true;
                }
            }
        }

        // 检查请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (XssUtil.containsXss(headerValue)) {
                return true;
            }
        }

        // 检查URI
        String requestURI = request.getRequestURI();
//        if (XssUtil.containsXss(requestURI)) {
//            return true;
//        }

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

    /**
     * XSS防护的请求包装器
     */
    private static class XssHttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
        
        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return XssUtil.cleanXss(value);
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    values[i] = XssUtil.cleanXss(values[i]);
                }
            }
            return values;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = super.getParameterMap();
            Map<String, String[]> cleanParameterMap = new HashMap<>();
            
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String[] values = entry.getValue();
                String[] cleanValues = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    cleanValues[i] = XssUtil.cleanXss(values[i]);
                }
                cleanParameterMap.put(entry.getKey(), cleanValues);
            }
            
            return cleanParameterMap;
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return XssUtil.cleanXss(value);
        }
    }
}
