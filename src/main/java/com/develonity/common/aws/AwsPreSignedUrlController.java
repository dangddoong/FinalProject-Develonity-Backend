package com.develonity.common.aws;

import com.develonity.board.dto.ImageNameRequest;
import com.develonity.common.security.users.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  private final TempImageRepository tempImageRepository;

  //preSignedURL 받아오기
  @PostMapping("/users/preSigned")
  public String createPreSigned(
      @RequestBody ImageNameRequest imageNameRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam String path/*user, order ,board..디렉토리*/) {

    String imageName = imageNameRequest.getImageName();
    return awsPreSignedUrlService.getPreSignedUrl(path, imageName);
  }

}
