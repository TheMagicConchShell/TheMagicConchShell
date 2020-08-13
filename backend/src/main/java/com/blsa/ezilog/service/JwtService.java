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
        String issure = "마싸고";
        String subject ="로그인토큰";
        Date exDate = new Date(System.currentTimeMillis()+(1000*60*60));
        final JwtBuilder builder = Jwts.builder();
        builder.setIssuer(issure)
                .setExpiration(exDate)
                .setSubject(subject)
                .setAudience(user.getNickname())
                .setIssuedAt(new Date());
       
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
