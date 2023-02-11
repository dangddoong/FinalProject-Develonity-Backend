package com.develonity.user.service;

import com.develonity.user.dto.LoginRequest;
import com.develonity.user.dto.LoginResponse;
import com.develonity.user.dto.RegisterRequest;

public interface UserService {

  void register(RegisterRequest registerRequest);

  LoginResponse login(LoginRequest loginRequest);

  void withdrawal(String loginId, String password);

  void logout(String loginId);
}
