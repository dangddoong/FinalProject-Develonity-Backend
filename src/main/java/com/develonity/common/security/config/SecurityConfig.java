package com.develonity.common.security.config;

import com.develonity.common.jwt.JwtAuthFilter;
import com.develonity.common.jwt.JwtUtil;
import com.develonity.common.security.exceptionHandler.AccessDeniedHandlerImpl;
import com.develonity.common.security.exceptionHandler.AuthenticationEntryPointImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

  private final JwtUtil jwtUtil;


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.httpBasic().disable()
        .csrf().disable()
        .formLogin().disable();

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션이 필요하면 생성하도록 셋팅

    http.authorizeHttpRequests()
        .antMatchers("/h2-console/**").permitAll()
//        .antMatchers("/api/users/signin").permitAll()
//        .antMatchers("/api/users/signup").permitAll()
//        .antMatchers("/api/users/signout").permitAll()
//        .antMatchers("/api/users/**").hasRole("CUSTOMER")
//        .antMatchers("/api/sellers/**").hasRole("SELLER")
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
        .and()
        .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPointImpl())
        .and()
        .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
