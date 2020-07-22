package com.blsa.ezilog.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.blsa.ezilog.model.user.User;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {
	@Value("${jwt.salt}")
	private String salt;
	
	@Value("${jwt.expmin}")
	private Long expireMin;
	
	public String create(final User user) {
		final JwtBuilder builder = Jwts.builder();
		
		builder.setHeaderParam("typ", "JWT");
		
		builder.setSubject("로그인 토큰")
		    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expireMin))
		    .claim("User", user);
		
		builder.signWith(SignatureAlgorithm.HS256, salt.getBytes());
		
		final String jwt = builder.compact();
		
		return jwt;
	}
	
	public void checkValid(final String jwt) {
		Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);
	}
	
	
}
