package com.develonity.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.common.aws.AwsS3Service;
import com.develonity.common.jwt.JwtUtil;
import com.develonity.common.redis.RedisDao;
import com.develonity.user.dto.LoginRequest;
import com.develonity.user.dto.RegisterRequest;
import com.develonity.user.dto.TokenResponse;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootTest
class UserServiceImplTest {

  @Autowired
  private UserRepository userRepository;

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

    RegisterRequest request = new RegisterRequest("user1", "pass12!@",
        "nickname1", "aa@a");

//    String encodingPassword = passwordEncoder.encode(request.getPassword());

    //when
    userService.register(request);
    Optional<User> user = userRepository.findById(3L);

    //then 주로 assert문, 결과값 비교

    assertThat(user.get().getLoginId()).isEqualTo(request.getLoginId());
//    assertThat(user.get().getPassword()).isEqualTo((encodingPassword));
    assertThat(user.get().getNickname()).isEqualTo(request.getNickname());
    assertThat(user.get().getEmail()).isEqualTo(request.getEmail());
  }

//  @Test
//  @DisplayName("로그인")
//  void login() {
//    //given
//    LoginRequest request = new LoginRequest("user2", "Pass12!@");
//
//    User user = new User("user2", passwordEncoder.encode("Pass12!@"),
//        "nickname2", "aa@b.com");
//
//    userRepository.save(user);
//
//    //when
//    TokenResponse tokenResponse = userService.login(request);
//
//    //then
//    assertThat(tokenResponse.getAccessToken()).isNotEmpty();
//    assertThat(tokenResponse.getRefreshToken()).isNotEmpty();
//
//  }
}