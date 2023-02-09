package com.develonity.common.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecurityExceptionResponse {

  private int statusCode;
  private String msg;

  public SecurityExceptionResponse(int statusCode, String msg) {
    this.statusCode = statusCode;
    this.msg = msg;
  }
}
