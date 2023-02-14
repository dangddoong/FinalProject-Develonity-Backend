package com.develonity.admin.entity;

import com.develonity.user.entity.UserRole;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Admin {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRole role = UserRole.ADMIN;
  @Column(nullable = false, unique = true)
  private String loginId;
  @Column(nullable = false)
  private String password;

  public Admin(String loginId, String password) {
    this.loginId = loginId;
    this.password = password;
  }
}
