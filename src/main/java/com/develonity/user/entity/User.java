package com.develonity.user.entity;

import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
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
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor
@Entity(name = "users")
@DynamicInsert
public class User extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRole userRole = UserRole.AMATEUR;
  @Column(nullable = false, unique = true)
  // name들이 너무 많아서 username보다 그냥 loginId라고 쓰는게 이해하기 좋지 않을까 생각했습니다.
  private String loginId;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String realName;
  @Column(nullable = false, unique = true)
  private String nickname;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false, unique = true)
  private String phoneNumber;
  @Column(nullable = false)
  @Embedded
  private Address address;
  private boolean withdrawal = false;
  private int giftPoint = 300;
  private int respectPoint = 0;

  @Builder
  public User(String loginId, String password, String realName, String nickname,
      String email, String phoneNumber, String detailAddress,
      String zipcode) {
    this.loginId = loginId;
    this.password = password;
    this.realName = realName;
    this.nickname = nickname;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = new Address(detailAddress, zipcode);
  }

  public void withdraw() {
    this.withdrawal = true;
  }

  public void upgradeGrade() {
    this.userRole = UserRole.EXPERT;
  }

  public void subtractGiftPoint(int giftPoint) {
    if (this.giftPoint < giftPoint) {
      throw new CustomException(ExceptionStatus.POINTS_IS_LACKING);
    }
    this.giftPoint = this.giftPoint - giftPoint;
  }

  public void addGiftPoint(int giftPoint) {
    this.giftPoint = this.giftPoint + giftPoint;
  }

  public void addRespectPoint(int respectPoint) {
    this.respectPoint = this.respectPoint + respectPoint;
  }

  public boolean isLackedRespectPoint() {
    return this.respectPoint < 10;
  }

  //프로필 수정(닉네임 변경)
  public void updateProfile(String nickname) {
    this.nickname = nickname;
  }

  @Embeddable
  @Getter
  public static class Address {

    private String detailAddress;
    @Column(length = 5)
    private String zipcode;

    public Address() {
    }

    public Address(String detailAddress, String zipcode) {
      this.detailAddress = detailAddress;
      this.zipcode = zipcode;
    }
  }
}
