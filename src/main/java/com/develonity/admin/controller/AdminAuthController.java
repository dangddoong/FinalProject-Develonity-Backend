package com.develonity.admin.controller;

import com.develonity.admin.service.AdminAuthService;
import com.develonity.common.auth.JwtUtil;
import com.develonity.user.dto.LoginRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminAuthController {

  private final AdminAuthService adminAuthService;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest,
      HttpServletResponse httpServletResponse) {
    String adminAccessToken = adminAuthService.login(loginRequest);
    httpServletResponse.addHeader(JwtUtil.ADMIN_HEADER, adminAccessToken);
    return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody LoginRequest loginRequest) {
    adminAuthService.register(loginRequest);
    return new ResponseEntity<>("등록 성공", HttpStatus.CREATED);

  }

}
