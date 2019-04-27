package com.linkinghack.criminalquerybase;

import com.linkinghack.criminalquerybase.inteceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private LoginInterceptor loginInterceptor;
    @Autowired
    public WebConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 需要登录权限校验
        registry.addInterceptor(this.loginInterceptor)
                .addPathPatterns("/index", "/criminal/**", "/departments/department/**",
                        "/departments/membersCount", "/wanted/**");

    }
}
