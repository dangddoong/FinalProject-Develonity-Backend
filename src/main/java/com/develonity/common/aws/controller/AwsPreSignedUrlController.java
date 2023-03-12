package com.develonity.common.aws.controller;

import com.develonity.board.dto.ImageNameRequest;
import com.develonity.common.aws.dto.PreSignedUrlResponse;
import com.develonity.common.aws.service.AwsPreSignedUrlService;
import com.develonity.common.security.users.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AwsPreSignedUrlController {

  private final AwsPreSignedUrlService awsPreSignedUrlService;
  
  //업로드용 preSignedURL
  @PostMapping("/preSignedUrl/upload")
  public PreSignedUrlResponse createPutPreSignedUrl(
      @RequestBody ImageNameRequest imageNameRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam String path/*user, order ,board..디렉토리*/) {

    String imageName = imageNameRequest.getImageName();
    return awsPreSignedUrlService.requestPreSignedUrl(path,
        imageName);
  }

  //조회용 preSignedUrl
  @GetMapping("/preSignedUrl/get")
  public String createGetPreSignedUrl(
      @RequestParam("fileName") String fileName,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return awsPreSignedUrlService.getPreSignedUrl(fileName);

  }

}
