package com.develonity.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {

  private final String accessToken;
  private final String refreshToken;

}
