package com.blsa.ezilog;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.blsa.ezilog.interceptor.JwtInterceptorGetMethod;
import com.blsa.ezilog.interceptor.JwtInterceptorPostMethod;
import com.blsa.ezilog.interceptor.JwtInterceptorPutDeleteMethod;

@EnableScheduling
@SpringBootApplication
public class BackendApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "email");
        SpringApplication.run(BackendApplication.class, args);
    }

    @Autowired
    private JwtInterceptorGetMethod jwtInterceptorGet;
    
    @Autowired
    private JwtInterceptorPostMethod jwtInterceptorPost;
    
    @Autowired
    private JwtInterceptorPutDeleteMethod jwtInterceptorPD;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> getMethodAdd = Arrays.asList("/user/*", "/spot", "/post/recommend", "/point/*","/point");
        List<String> getMethodExclude = Arrays.asList("/user/authentication","", "/spot/banner");
        registry.addInterceptor(jwtInterceptorGet).addPathPatterns(getMethodAdd).excludePathPatterns(getMethodExclude); // 적용
        
        List<String> postMethodAdd = Arrays.asList("/spot","/selection/post","/qna/*","/support/notice","/counsel/*","/category");
        List<String> postMethodExclude = Arrays.asList("");
        registry.addInterceptor(jwtInterceptorPost).addPathPatterns(postMethodAdd).excludePathPatterns(postMethodExclude);
        
        List<String> putDeleteMethodAdd = Arrays.asList("/user/*","/spot","/selection/post","/qna/*","/point/level-up","/support/notice","/counsel/*","/category");
        List<String> putDeleteMethodExclude = Arrays.asList("");
        registry.addInterceptor(jwtInterceptorPD).addPathPatterns(putDeleteMethodAdd).excludePathPatterns(putDeleteMethodExclude);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
                .exposedHeaders("jwt-auth-token", "nickname");
    }
}
