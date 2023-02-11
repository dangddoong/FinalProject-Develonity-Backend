package com.develonity.common.jwt;

import com.develonity.user.entity.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TokenInfo {

  private final String loginId;
  private final JwtStatus jwtStatus;
  private final UserRole userRole;
}
