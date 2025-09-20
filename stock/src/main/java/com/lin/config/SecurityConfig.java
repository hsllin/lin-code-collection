package com.lin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security配置 - 禁用默认安全配置
 * 
 * @author lin
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护
            .csrf().disable()
            // 禁用默认的登录页面
            .formLogin().disable()
            // 禁用HTTP Basic认证
            .httpBasic().disable()
            // 允许所有请求
            .authorizeRequests()
                .anyRequest().permitAll();
    }
}
