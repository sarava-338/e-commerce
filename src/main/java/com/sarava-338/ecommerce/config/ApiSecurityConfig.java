package com.fresco.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fresco.ecommerce.service.UserAuthService;

@Configuration
@EnableWebSecurity()
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserAuthService userAuthService;
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userAuthService)
        .passwordEncoder(NoOpPasswordEncoder.getInstance());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.exceptionHandling().authenticationEntryPoint(
        (req, res, ex) -> res.sendError(401));
    http.authorizeRequests()
        .antMatchers("/api/public/**").permitAll()
        .antMatchers("/api/auth/consumer/**").hasAnyAuthority("CONSUMER")
        .antMatchers("/api/auth/seller/**").hasAnyAuthority("SELLER");
  }
}
