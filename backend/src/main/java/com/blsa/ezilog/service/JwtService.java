package com.blsa.ezilog.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.blsa.ezilog.model.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {
    @Value("${jwt.expmin}")
    private Long expireMin;

    public String create(final User user) {
        final JwtBuilder builder = Jwts.builder();

        builder.setHeaderParam("typ", "JWT");

        builder.setSubject("로그인 토큰").setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expireMin))
                .claim("User", user);

        builder.signWith(SignatureAlgorithm.HS256, user.getNickname().getBytes());

        final String jwt = builder.compact();

        return jwt;
    }

    public void checkValid(final String jwt, final String nickname) throws Exception{
        Jwts.parser().setSigningKey(nickname.getBytes()).parseClaimsJws(jwt);
    }

    public Map<String, Object> get(final String jwt, final String nickname) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(nickname.getBytes()).parseClaimsJws(jwt);
        } catch (final Exception e) {
            throw new RuntimeException();
        }

        return claims.getBody();
    }
}
