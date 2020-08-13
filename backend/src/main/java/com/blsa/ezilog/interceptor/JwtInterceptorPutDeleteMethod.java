package com.blsa.ezilog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.blsa.ezilog.exception.auth.JwtException;
import com.blsa.ezilog.service.JwtService;

@Component
public class JwtInterceptorPutDeleteMethod implements HandlerInterceptor {
    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        if (request.getMethod().equals("PUT")||request.getMethod().equals("DELETE")) {
            String token = request.getHeader("jwt-auth-token");
            String nickname = request.getHeader("nickname");
            if (token != null && token.length() > 0) {
                try {
                    jwtService.checkValid(token, nickname);
                } catch (Exception e) {
                    throw new JwtException("닉네임이 맞지 않습니다.");
                }
                return true;
            } else {
                throw new JwtException("인증 토큰이 없습니다.");
            }
        } else {
            return true;
        }
    }

}
