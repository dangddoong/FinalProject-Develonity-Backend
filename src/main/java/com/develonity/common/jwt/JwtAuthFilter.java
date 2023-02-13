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
      TokenInfo accessTokenInfo = jwtUtil.getInfoFromToken(accessToken);
      // accessToken에 기간만료가 아닌 다른 예외가 있다면 401만 리턴해주도록
      // 아니면 이 로직을 getInfoFromToken에 넣는 것은 어떨지..? (param으로 response 넘겨주면 가능)
//      if (accessTokenInfo.getJwtStatus() == JwtStatus.DENIED) {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        return;
//      }
      if (accessTokenInfo.getJwtStatus() == JwtStatus.EXPIRED) {
        String refreshToken = JwtUtil.resolveRefreshToken(request);
        jwtUtil.checkBlackList(refreshToken);
// 만약 기간만료가 아닌 다른 예외들이 핸들링 안된다면 29-32 라인처럼 처리로직 추가하기
        jwtUtil.validateToken(refreshToken, response);
        accessToken = jwtUtil.createAccessToken(accessTokenInfo.getLoginId(),
            accessTokenInfo.getUserRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
      }
      Authentication authentication = jwtUtil.createAuthentication(accessTokenInfo.getLoginId());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

}
