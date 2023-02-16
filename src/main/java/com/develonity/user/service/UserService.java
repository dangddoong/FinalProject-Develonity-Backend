package com.develonity.user.service;

import com.develonity.user.dto.LoginRequest;
import com.develonity.user.dto.ProfileResponse;
import com.develonity.user.dto.RegisterRequest;
import com.develonity.user.dto.TokenResponse;

public interface UserService {

  void register(RegisterRequest registerRequest);

  TokenResponse login(LoginRequest loginRequest);

  void withdrawal(String loginId, String password);

  void logout(String loginId);

  ProfileResponse getProfile(Long userId);

  TokenResponse reissue(String refreshToken);
}
