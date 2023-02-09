package com.develonity.user.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRole userRole;
  @Column(nullable = false, unique = true)
  // name들이 너무 많아서 username보다 그냥 loginId라고 쓰는게 이해하기 좋지 않을까 생각했습니다.
  private String loginId;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String realName;
  @Column(nullable = false, unique = true)
  private String nickName;
  @Column(nullable = false)
  private String profileImageUrl;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false, unique = true)
  private String phoneNumber;
  @Column(nullable = false)
  @Embedded
  private Address address;
  private boolean withdrawal = false;
  private int giftPoint = 0;
  private int respectPoint = 0;

  @Builder
  public User(String loginId, String password, String realName, String nickName,
      String profileImageUrl, String email, String phoneNumber, Address address) {
    this.loginId = loginId;
    this.password = password;
    this.realName = realName;
    this.nickName = nickName;
    this.profileImageUrl = profileImageUrl;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  @Embeddable
  public static class Address {

    private String detailAddress;
    @Column(length = 5)
    private String zipcode;

    public Address() {
    }
  }
}
