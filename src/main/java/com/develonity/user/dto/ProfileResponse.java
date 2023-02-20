package com.develonity.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileResponse {

  private final String profileImageUrl;
  private final String nickname;

  // 나중에 프로필에 '등급점수' 추가돼도 괜찮을듯 합니다.

}
