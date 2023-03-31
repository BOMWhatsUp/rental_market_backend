package com.bom.rentalmarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS", "PUT", "PATCH","DELETE")
                .maxAge(3600);
    }
}

// 정후님
// 회원가입 -> 중복체크

// 정후님
// 로그인 ->

//
// 마이페이지 -> 프로필 수정,

//