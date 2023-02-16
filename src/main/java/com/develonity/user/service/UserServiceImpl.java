package com.develonity.user.service;

import com.develonity.common.jwt.JwtUtil;
import com.develonity.common.redis.RedisDao;
import com.develonity.user.dto.LoginRequest;
import com.develonity.user.dto.LoginResponse;
import com.develonity.user.dto.ProfileResponse;
import com.develonity.user.dto.RegisterRequest;
import com.develonity.user.dto.ReissueResponse;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final RedisDao redisDao;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Override
  @Transactional
  public void register(RegisterRequest registerRequest) {
    if (userRepository.existsByLoginId(registerRequest.getLoginId())) {
      throw new IllegalArgumentException("회원 중복");
    }
    String encodingPassword = passwordEncoder.encode(registerRequest.getPassword());
    User user = registerRequest.toEntity(encodingPassword);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public LoginResponse login(LoginRequest loginRequest) {
    User user = userRepository.findByLoginId(loginRequest.getLoginId())
        .orElseThrow(IllegalArgumentException::new);
    if (user.isWithdrawal()) {
      throw new IllegalArgumentException("탈퇴한 회원입니다.");
    }
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("비밀번호 불일치");
    }

    String accessToken = jwtUtil.createAccessToken(user.getLoginId(), user.getUserRole());
    String refreshToken = jwtUtil.createRefreshToken(user.getLoginId(), user.getUserRole());
    return new LoginResponse(accessToken, refreshToken);
  }

  @Override
  @Transactional
  public void logout(String refreshToken) {
    Long validMilliSeconds = jwtUtil.getValidMilliSeconds(refreshToken);
    redisDao.setValues(refreshToken, "", Duration.ofMillis(validMilliSeconds));
  }

  @Override
  @Transactional
  public void withdrawal(String refreshToken, String loginId, String password) {
    User user = userRepository.findByLoginId(loginId)
        .orElseThrow(IllegalArgumentException::new);
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("비밀번호 불일치");
    }
    user.withdraw();
    logout(refreshToken);
  }

  @Override
  public ProfileResponse getProfile(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    return new ProfileResponse(user.getProfileImageUrl(), user.getNickname());
  }

  //   RTR(refresh token rotation)을 적용하려면 현재 블랙리스트로 운영중인 refresh token 운영방식을
//   발급시 등록하는 체제로 수정해야한다.  그럼 모든 로그인 유저의 refresh token을 redis에 등록하고 관리해야한다....'
//   근데 RTR을 안하고 지금 방식으로 가면 refreshToken으로 무한히 많은 access token을 발급할 수 있다는 문제가 있다.
//   RTR을 적용하면 refresh token이 탈취당하고 사용됐을 때 사용자가 서비스를 사용중이라면 알아차릴 수 있다.
  @Override
  public ReissueResponse reissue(String refreshToken) {
    jwtUtil.checkBlackList(refreshToken);
    String username = jwtUtil.getUsernameFromTokenIfValid(refreshToken);
    User user = userRepository.findByLoginId(username).orElseThrow(IllegalArgumentException::new);
    String accessToken = jwtUtil.createAccessToken(user.getLoginId(), user.getUserRole());
    return new ReissueResponse(accessToken);
  }
}
