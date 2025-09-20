package com.lin.config;

import com.lin.security.filter.XssFilter;
import com.lin.security.filter.SqlInjectionFilter;
import com.lin.security.filter.RateLimitFilter;
import com.lin.security.filter.CsrfFilter;
import com.lin.security.filter.ReplayAttackFilter;
import com.lin.security.filter.SecurityHeadersFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 安全过滤器配置
 * 
 * @author lin
 */
//@Configuration
public class SecurityFilterConfig {

//    @Bean
    public FilterRegistrationBean<XssFilter> xssFilter() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        registration.setName("xssFilter");
        return registration;
    }

//    @Bean
    public FilterRegistrationBean<SqlInjectionFilter> sqlInjectionFilter() {
        FilterRegistrationBean<SqlInjectionFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SqlInjectionFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
        registration.setName("sqlInjectionFilter");
        return registration;
    }

//    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 3);
        registration.setName("rateLimitFilter");
        return registration;
    }

//    @Bean
    public FilterRegistrationBean<CsrfFilter> csrfFilter() {
        FilterRegistrationBean<CsrfFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CsrfFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 4);
        registration.setName("csrfFilter");
        return registration;
    }

//    @Bean
    public FilterRegistrationBean<ReplayAttackFilter> replayAttackFilter() {
        FilterRegistrationBean<ReplayAttackFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ReplayAttackFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 5);
        registration.setName("replayAttackFilter");
        return registration;
    }

//    @Bean
    public FilterRegistrationBean<SecurityHeadersFilter> securityHeadersFilter() {
        FilterRegistrationBean<SecurityHeadersFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SecurityHeadersFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 6);
        registration.setName("securityHeadersFilter");
        return registration;
    }
}
