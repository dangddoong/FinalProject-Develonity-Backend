package com.develonity.common.aws.dto;

import lombok.Builder;
import lombok.Getter;

@Getter

public class PreSignedUrlResponse {

  private final String objectKey;
  private final String preSignedURL;

  @Builder
  public PreSignedUrlResponse(String objectKey, String preSignedURL) {
    this.objectKey = objectKey;
    this.preSignedURL = preSignedURL;
  }
}
