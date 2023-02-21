package com.develonity.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
//@RequiredArgsConstructor
@Builder
public class ProfileResponse {

  private final String profileImageUrl;
  private final String nickname;
  private final int giftPoint;
  private final int respectPoint;

  public ProfileResponse(String profileImageUrl, String nickname, int giftPoint, int respectPoint) {
    this.profileImageUrl = profileImageUrl;
    this.nickname = nickname;
    this.giftPoint = giftPoint;
    this.respectPoint = respectPoint;
  }
}
