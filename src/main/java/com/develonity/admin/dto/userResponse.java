package com.develonity.admin.dto;


import com.develonity.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
// user전체 회원을 조회에 사용되는 dto (로그인id, 실명, 별명, 유저역할)
public class userResponse {

  private final String loginid;

  private final String realName;

  private final String nickName;

  private final UserRole userRole;

}


