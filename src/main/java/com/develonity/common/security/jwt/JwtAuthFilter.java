package com.develonity.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = jwtUtil.resolveToken(request);
    if (token != null) {
      if (request.getSession().getAttribute("SECURITY_CONTEXT") != null) {
        Claims info = jwtUtil.getUserInfoFromToken(token);
        HttpSession session = request.getSession(false);
        SecurityContext context = (SecurityContext) session.getAttribute("SECURITY_CONTEXT");
        if (!jwtUtil.validateToken(token)) {
          jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
          return;
        }
        if (context.getAuthentication().getName().equals(info.getSubject())) {
          jwtUtil.setAuthentication(info.getSubject(), request);
        }
      } else {
        throw new IllegalArgumentException("로그인을 다시 시도해 주세요.");
      }
    }
    filterChain.doFilter(request, response);
  }


  public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
    response.setStatus(statusCode);
    response.setContentType("application/json");
    try {
      String json = new ObjectMapper().writeValueAsString(
          new SecurityExceptionResponse(statusCode, msg));
      response.getWriter().write(json);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

}
