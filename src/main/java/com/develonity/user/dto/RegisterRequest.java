package com.develonity.user.dto;

import com.develonity.user.entity.User;
import com.develonity.user.entity.User.Address;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class RegisterRequest {

  @Size(min = 6, max = 15)
  @Pattern(regexp = "^[a-z0-9]*$")
  private final String loginId;
  @Size(min = 6, max = 15)
  @Pattern(regexp = "^[a-zA-Z0-9]*$")
  private final String password;
  private final String realName;
  @Size(min = 2, max = 10)
  private final String nickName;
  private final String profileImageUrl;
  private final String email;
  private final String phoneNumber;
  private final Address address;

  public User toEntity(String encodingPassword) {
    return User.builder()
        .loginId(this.loginId)
        .password(encodingPassword)
        .realName(this.realName)
        .nickName(this.nickName)
        .profileImageUrl(this.profileImageUrl)
        .email(this.email)
        .phoneNumber(this.phoneNumber)
        .address(this.address)
        .build();
  }


}
