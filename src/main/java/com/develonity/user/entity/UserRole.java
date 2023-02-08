package com.develonity.user.entity;

public enum UserRole {
  AMATEUR(Authority.AMATEUR),  // 사용자 권한
  EXPERT(Authority.EXPERT), // 전문가 권한
  ADMIN(Authority.ADMIN);  // 관리자 권한
  private final String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return authority;
  }

  public static class Authority {

    public static final String AMATEUR = "ROLE_AMATEUR";
    public static final String EXPERT = "ROLE_EXPERT";
    public static final String ADMIN = "ROLE_ADMIN";
  }

}
