package com.develonity.common.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String accessToken = jwtUtil.resolveAccessToken(request);
    if (accessToken != null) {
      // accessToken에서 필요한 값(loginId, Role)과 토큰의 상태를 꺼냄
      TokenInfo tokenInfo = jwtUtil.getInfoFromToken(accessToken);
      if (tokenInfo.getJwtStatus() == JwtStatus.EXPIRED) {
        String refreshToken = jwtUtil.resolveRefreshToken(request);
        jwtUtil.validateRefreshToken(tokenInfo.getLoginId(), refreshToken);
        accessToken = jwtUtil.createToken(tokenInfo.getLoginId(), tokenInfo.getUserRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
      }
      Authentication authentication = jwtUtil.createAuthentication(tokenInfo.getLoginId());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

}
