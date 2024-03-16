package com.fresco.ecommerce.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.repo.UserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	@Autowired
	private UserRepo userRepo;
	@Value("${jwt.token.validity}")
	private Integer validity;
	@Value("${jwt.secret}")
	private String secret;

	public String generateToken(User user) {
		return Jwts.builder()
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + validity))
				.setSubject(user.getUsername())
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();

	}

	public String getUsername(String jwt) {
		return Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();
	}

	public User getUser(String jwt) {
		String username = getUsername(jwt);
		return userRepo.findByUsername(username).get();

	}
}