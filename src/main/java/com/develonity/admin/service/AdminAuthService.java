package com.develonity.admin.service;

import com.develonity.admin.entity.Admin;
import com.develonity.admin.repository.AdminRepository;
import com.develonity.common.jwt.JwtUtil;
import com.develonity.user.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Transactional
  public String login(LoginRequest loginRequest) {
    Admin admin = adminRepository.findByLoginId(loginRequest.getLoginId())
        .orElseThrow(IllegalArgumentException::new);
    if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
      throw new IllegalArgumentException("비밀번호 불일치");
    }
    return jwtUtil.createAdminToken(admin.getLoginId(), admin.getRole());
  }

  @Transactional
  public void register(LoginRequest loginRequest) {
    String encodingPassword = passwordEncoder.encode(loginRequest.getPassword());
    adminRepository.save(new Admin(loginRequest.getLoginId(), encodingPassword));
  }
}
