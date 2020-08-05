package com.blsa.ezilog;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.blsa.ezilog.interceptor.JwtInterceptor;

@EnableScheduling
@SpringBootApplication
public class BackendApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "email");
        SpringApplication.run(BackendApplication.class, args);
    }

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/user/test/test"); // 적용
        // "/user/update","/user/detail","/user/delete","/user/extendJWT" 예상 적용 경로 // 경로
        // .excludePathPatterns(Arrays.asList());// 적용 제외 경로
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
                .exposedHeaders("jwt-auth-token", "nickname");
    }
}
