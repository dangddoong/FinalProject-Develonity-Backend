package com.develonity.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.common.aws.AwsS3Service;
import com.develonity.common.jwt.JwtUtil;
import com.develonity.common.redis.RedisDao;
import com.develonity.user.dto.LoginRequest;
import com.develonity.user.dto.ProfileRequest;
import com.develonity.user.dto.ProfileResponse;
import com.develonity.user.dto.RegisterRequest;
import com.develonity.user.dto.TokenResponse;
import com.develonity.user.entity.ProfileImage;
import com.develonity.user.entity.User;
import com.develonity.user.repository.ProfileImageRepository;
import com.develonity.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@SpringBootTest
class UserServiceImplTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  ProfileImageRepository profileImageRepository;
  @Autowired
  private UserServiceImpl userService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private RedisDao redisDao;

  @Autowired
  private AwsS3Service awsS3Service;

  @Test
  @DisplayName("회원 가입")
  void register() {
    //given

    RegisterRequest request = new RegisterRequest("newUser", "pass12!@",
        "newUser", "newnew@a");

//    String encodingPassword = passwordEncoder.encode(request.getPassword());

    //when
    User user = userService.register(request);


    //then 주로 assert문, 결과값 비교

    assertThat(user.getLoginId()).isEqualTo(request.getLoginId());
//    assertThat(user.get().getPassword()).isEqualTo((encodingPassword));
    assertThat(user.getNickname()).isEqualTo(request.getNickname());
    assertThat(user.getEmail()).isEqualTo(request.getEmail());
  }

  @Test
  @DisplayName("로그인")
  void login() {
    //given
    LoginRequest request = new LoginRequest("user2", "Pass12!@");

    User user = new User("user2", passwordEncoder.encode("Pass12!@"),
        "nickname2", "aa@b.com");

    userRepository.save(user);

    //when
    TokenResponse tokenResponse = userService.login(request);

    //then
    assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    assertThat(tokenResponse.getRefreshToken()).isNotEmpty();

  }

  @Test
  @DisplayName("프로필 수정 & 조회")
  void updateProfile() throws IOException {
    //회원가입
    RegisterRequest request = new RegisterRequest("user1", "pass12!@",
        "테스트유저", "aa@a");
    User savedUser = userService.register(request);

    //기존 프로필 조회

    ProfileResponse profileResponse = userService.getProfile(savedUser.getId());
    //기본 프로필 이미지
    assertThat(profileResponse.getProfileImageUrl()).isEqualTo("https://dthezntil550i.cloudfront.net/p6/latest/p62007150224230440002288000/ed8e25a2-d8dd-43ac-8d18-77712b287dc6.png");
    assertThat(profileResponse.getNickname()).isEqualTo(request.getNickname());

    //프로필 수정
    ProfileRequest profileRequest = new ProfileRequest("수정닉네임");

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    userService.updateProfile(profileRequest, multipartFile, savedUser);
    Optional<ProfileImage> updatedProfileImage = profileImageRepository.findByUserId(
        savedUser.getId());

    // 수정 후 프로필 조회
    ProfileResponse updatedProfileResponse = userService.getProfile(savedUser.getId());
    assertThat(updatedProfileResponse.getProfileImageUrl()).isNotEqualTo("https://dthezntil550i.cloudfront.net/p6/latest/p62007150224230440002288000/ed8e25a2-d8dd-43ac-8d18-77712b287dc6.png");
    assertThat(updatedProfileResponse.getProfileImageUrl()).isEqualTo(updatedProfileImage.get().getImagePath());
    assertThat(updatedProfileResponse.getNickname()).isEqualTo(profileRequest.getNickname());

  }
}