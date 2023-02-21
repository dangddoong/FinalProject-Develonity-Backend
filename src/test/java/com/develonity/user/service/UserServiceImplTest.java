//package com.develonity.user.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.develonity.common.jwt.JwtUtil;
//import com.develonity.common.redis.RedisDao;
//import com.develonity.user.dto.LoginRequest;
//import com.develonity.user.dto.RegisterRequest;
//import com.develonity.user.dto.TokenResponse;
//import com.develonity.user.entity.User;
//import com.develonity.user.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@SpringBootTest
//class UserServiceImplTest {
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Autowired
//  private UserServiceImpl userService;
//  @Autowired
//  private PasswordEncoder passwordEncoder;
//  //  @Spy
//  @Autowired
//  private JwtUtil jwtUtil;
//
//  @Autowired
//  private RedisDao redisDao;
//
//
//  @Test
//  @DisplayName("회원 가입")
//  void register() {
//    //given
//
//    RegisterRequest request = new RegisterRequest("user1", passwordEncoder.encode("pass12!@"),
//        "nickname1", "aa@a");
//
//    //when
//    userService.register(request);
//    String encodingPassword = passwordEncoder.encode(request.getPassword());
//    User user = request.toEntity(encodingPassword);
//
//    //then 주로 assert문, 결과값 비교
//
//    assertThat(user.getLoginId()).isEqualTo("user1");
//    assertThat(user.getPassword()).isEqualTo((encodingPassword));
//    assertThat(user.getNickname()).isEqualTo("nickname1");
//    assertThat(user.getEmail()).isEqualTo("aa@a");
//  }
//
//  @Test
//  @DisplayName("로그인")
//  void login() {
//    //given
//    LoginRequest request = new LoginRequest("user2", "pass12!@");
//
//    User user = new User("user2", passwordEncoder.encode("pass12!@"),
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
//}