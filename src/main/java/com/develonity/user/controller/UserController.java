package com.develonity.user.controller;

import com.develonity.common.jwt.JwtUtil;
import com.develonity.common.security.users.UserDetailsImpl;
import com.develonity.user.dto.LoginRequest;
import com.develonity.user.dto.LoginResponse;
import com.develonity.user.dto.RegisterRequest;
import com.develonity.user.dto.WithdrawalRequest;
import com.develonity.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  @PostMapping("/register") //회원가입
  public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
    userService.register(registerRequest);
    return new ResponseEntity<>("회원가입성공", HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest,
      HttpServletResponse httpServletResponse) {
    LoginResponse loginResponse = userService.login(loginRequest);
    httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, loginResponse.getAccessToken());
    httpServletResponse.addHeader(JwtUtil.REFRESH_HEADER, loginResponse.getRefreshToken());
    return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {
    userService.logout(JwtUtil.resolveRefreshToken(httpServletRequest));

    // response에 덮어씌우고자 하는 의도로 아래 두 라인을 작성하였음...
    httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, "clear");
    httpServletResponse.addHeader(JwtUtil.REFRESH_HEADER, "clear");

    return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
  }

  @PatchMapping("/withdrawal")
  public ResponseEntity<String> withdrawal(@RequestBody WithdrawalRequest withdrawalRequest,
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    String refreshToken = JwtUtil.resolveRefreshToken(httpServletRequest);
    userService.withdrawal(refreshToken, userDetails.getUsername(),
        withdrawalRequest.getPassword());
    // response에 덮어씌우고자 하는 의도로 아래 두 라인을 작성하였음...
    httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, "clear");
    httpServletResponse.addHeader(JwtUtil.REFRESH_HEADER, "clear");
    return new ResponseEntity<>("회원탈퇴 성공", HttpStatus.OK);
  }

//  // 내 프로필조회
//  @GetMapping("user/me/profile")
//  public
//
//  //  타인 프로필조회
//  @GetMapping("users/{id}/profile")
//
//  //프로필 정보 수정 (닉네임, 프로필사진)
//  @PutMapping("user/me/profile")
//
//  // 개인정보 조회 (이름, 비밀번호, 이메일, 핸드폰번호, 주소)
//  @GetMapping("user/me/personal-information")
//
//  // 개인정보 수정 (이름, 비밀번호, 이메일, 핸드폰번호, 주소)
//  @PutMapping("user/me/personal-information")

  // ----아래부터는 애매한 부분 ---
  //게시글 스크랩 저장 ?-? (이거는 애매함)
  // 스크랩 게시물 전체 조회
  // 내 주문 정보 확인(이거는 오더에서?)
  // 본인 글 전체조회 (그럼 이건 보드에서)
  // 본인 댓글 전체조회


}
