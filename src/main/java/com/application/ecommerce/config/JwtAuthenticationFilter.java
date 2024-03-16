package com.application.ecommerce.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.application.ecommerce.models.User;
import com.application.ecommerce.repo.UserRepo;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader("JWT");
		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}
		if (jwt.isEmpty()) {
			filterChain.doFilter(request, response);
			return;
		}
		User user = jwtUtil.getUser(jwt);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null,
				user.getAuthorities());
		token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(token);
		filterChain.doFilter(request, response);

	}
}
