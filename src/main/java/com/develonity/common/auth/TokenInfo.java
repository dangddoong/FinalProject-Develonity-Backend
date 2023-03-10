package com.develonity.common.auth;

import com.develonity.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenInfo {

  private String loginId;
  private JwtStatus jwtStatus;
  private UserRole userRole;
}
