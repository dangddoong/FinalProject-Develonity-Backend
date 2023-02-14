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
    try {
      String accessToken = jwtUtil.resolveAccessToken(request);
      String a = request.getRequestURI();
      if (accessToken != null) {
        // 이쯤에서 어드민 토큰 꺼내고 검증하는 로직이 있어야 할 것 같음(아래 로직 안타고 바로 .doFilter로 넘어가도록?)
        // accessToken에서 필요한 값(loginId, Role)과 토큰의 상태를 꺼냄
        TokenInfo accessTokenInfo = jwtUtil.getInfoFromToken(accessToken);
        if (accessTokenInfo.getJwtStatus() == JwtStatus.EXPIRED) {
          String refreshToken = JwtUtil.resolveRefreshToken(request);
          jwtUtil.checkBlackList(refreshToken);
          jwtUtil.validateToken(refreshToken);
          if (!request.getRequestURI().equals("/api/logout")) {
            accessToken = jwtUtil.createAccessToken(accessTokenInfo.getLoginId(),
                accessTokenInfo.getUserRole());
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
          }
        }
        Authentication authentication = jwtUtil.createAuthentication(accessTokenInfo.getLoginId());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
      return;
    }
    filterChain.doFilter(request, response);
  }

}
