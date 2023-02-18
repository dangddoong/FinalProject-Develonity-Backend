package com.develonity.user.service;

import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.common.jwt.JwtUtil;
import com.develonity.common.redis.RedisDao;
import com.develonity.user.dto.LoginRequest;
import com.develonity.user.dto.ProfileResponse;
import com.develonity.user.dto.RegisterRequest;
import com.develonity.user.dto.TokenResponse;
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
  public TokenResponse login(LoginRequest loginRequest) {
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
    saveRefreshTokenToRedis(user.getLoginId(), refreshToken.substring(7));
    return new TokenResponse(accessToken, refreshToken);
  }

  @Override
  @Transactional
  public void logout(String loginId) {
    deleteRefreshTokenFromRedis(loginId);
  }

  @Override
  @Transactional
  public void withdrawal(String loginId, String password) {
    User user = userRepository.findByLoginId(loginId)
        .orElseThrow(IllegalArgumentException::new);
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("비밀번호 불일치");
    }
    user.withdraw();
    deleteRefreshTokenFromRedis(loginId);
  }

  //   RTR(refresh token rotation)을 적용하려면 현재 블랙리스트로 운영중인 refresh token 운영방식을
//   발급시 등록하는 체제로 수정해야한다.  그럼 모든 로그인 유저의 refresh token을 redis에 등록하고 관리해야한다....'
//   근데 RTR을 안하고 지금 방식으로 가면 refreshToken으로 무한히 많은 access token을 발급할 수 있다는 문제가 있다.
// 생각해보니 로그인 할 때마다 유효한 refreshToken이 계속 생긴다는 문제도 있다.
//   RTR을 적용하면 refresh token이 탈취당하고 사용됐을 때 사용자가 서비스를 사용중이라면 알아차릴 수 있다.
  @Override
  @Transactional
  public TokenResponse reissue(String refreshToken) {
    String loginId = jwtUtil.getLoginIdFromTokenIfValid(refreshToken);
    // 로그인, 리이슈 할 때마다 유효한 refresh token이 생겨나므로, redis에 있는 token과 동일한지 비교함으로써 가장 최근에 발급된 refresh token이 맞는지 확인한다.
    if (!isSameRefreshTokenInRedis(loginId, refreshToken)) {
      throw new IllegalArgumentException("token error");
    }
    User user = userRepository.findByLoginId(loginId).orElseThrow(IllegalArgumentException::new);
    String accessToken = jwtUtil.createAccessToken(user.getLoginId(), user.getUserRole());
    String createdRefreshToken = jwtUtil.createRefreshToken(user.getLoginId(), user.getUserRole());
    saveRefreshTokenToRedis(user.getLoginId(), createdRefreshToken.substring(7));
    return new TokenResponse(accessToken, createdRefreshToken);
  }

  @Override
  @Transactional(readOnly = true)
  public ProfileResponse getProfile(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    return new ProfileResponse(user.getProfileImageUrl(), user.getNickname());
  }

  private void deleteRefreshTokenFromRedis(String loginId) {
    redisDao.deleteValues(loginId);
  }

  private void saveRefreshTokenToRedis(String loginId, String refreshToken) {
    redisDao.setValues(loginId, refreshToken,
        Duration.ofMillis(JwtUtil.REFRESH_TOKEN_TIME));
  }

  private boolean isSameRefreshTokenInRedis(String loginId, String refreshToken) {
    Object redisRefreshToken = redisDao.getValues(loginId);
    if (redisRefreshToken != null) {
      return redisRefreshToken.toString().equals(refreshToken);
    }
    return false;
  }

  @Override
  @Transactional
  public void subtractGiftPoint(int giftPoint, User user) {
    user.subtractGiftPoint(giftPoint);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void addGiftPoint(int giftPoint, User user) {
    user.addGiftPoint(giftPoint);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void addRespectPoint(int respectPoint, User user) {
    user.addRespectPoint(respectPoint);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void upgradeGrade(Long userId) {
    User user = getUserAndCheck(userId);
    user.upgradeGrade();
    userRepository.save(user);
  }

  @Override
  public boolean isLackedRespectPoint(Long userId) {
    User user = getUserAndCheck(userId);
    return user.isLackedRespectPoint();
  }

  @Override
  public User getUserAndCheck(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new CustomException(
        ExceptionStatus.USER_IS_NOT_EXIST));
  }
}
