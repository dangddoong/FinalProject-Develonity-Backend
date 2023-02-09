package com.develonity.user.service;

import com.develonity.user.dto.LoginRequest;
import com.develonity.user.dto.RegisterRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

  void register(RegisterRequest registerRequest);

  void login(LoginRequest loginRequest, HttpServletResponse httpServletResponse);

  void withdrawal(String loginId, String password);
}
