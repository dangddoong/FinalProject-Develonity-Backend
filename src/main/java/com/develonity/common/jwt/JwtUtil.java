package com.develonity.common.jwt;

import com.develonity.common.redis.RedisDao;
import com.develonity.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

  private final RedisDao redisDao;
  private final UserDetailsService userDetailsService;
  public static final String AUTHORIZATION_HEADER = "Authorization"; // access token 헤더에 들어가는 키값
  public static final String REFRESH_HEADER = "Refresh";
  public static final String AUTHORIZATION_KEY = "auth"; // 사용자 권한 키값. 사용자 권한도 토큰안에 넣어주기 때문에 그때 사용하는 키값
  private static final String BEARER_PREFIX = "Bearer "; // Token 식별자
  private static final long ACCESS_TOKEN_TIME = 30 * 1000L;  // 토큰 만료시간. (60 * 1000L 이 1분)
  // refreshToken 테스트용으로 30초로 설정
  private static final long REFRESH_TOKEN_TIME = 2 * 60 * 1000L;  // 토큰 만료시간. (60 * 1000L 이 1분)


  @Value("${jwt.secret.key}")
  private String secretKey;
  private Key key; // 토큰을 만들 때 넣어줄 키값

  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 이 알고리즘을 사용해서 키 객체를 암호화할 것이다.

  @PostConstruct // 클래스가 service(로직을 탈 때)를 수행하기 전에 발생한다. 이 메서드는 다른 리소스에서 호출되지 않는다해도 실행된다.
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(
        secretKey); // secretKey는 Base64로 인코딩되어 있기 때문에, 값을 가지고와서 디코드(getDecoder().decode()) 하는 과정이다. 반환값은 byte[]이다.
    key = Keys.hmacShaKeyFor(bytes); // key 객체에 넣어줄 때는 hmacShaKeyFor() 메서드를 사용해야한다.
  }

  // header 토큰을 가져오기
  public String resolveAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public static String resolveRefreshToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(REFRESH_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // 토큰 생성
  public String createAccessToken(String username, UserRole role) {
    Date date = new Date();
    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username)
            .claim(AUTHORIZATION_KEY, role)
            .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  public String createRefreshToken(String username, UserRole role) {
    Date date = new Date();
    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username)
            .claim(AUTHORIZATION_KEY, role)
            .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  // 토큰 검증
  public void validateToken(String token, HttpServletResponse response) {
//    try {
    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//    } catch (Exception e) {
//      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }
  }

  // 단일책임원칙에는 위배되지만 jwt 디코딩 비용이 비싸므로 한큐에 모든결 해결하고 싶은 마음에 부득불 원칙을 어겼습니다.
  public TokenInfo getInfoFromToken(String token) {
    try {
      Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
          .getBody();
      return new TokenInfo(claims.getSubject(), JwtStatus.ACCESS,
          UserRole.valueOf(claims.get("auth").toString()));

    } catch (ExpiredJwtException e) {
      return new TokenInfo(e.getClaims().getSubject(), JwtStatus.EXPIRED,
          UserRole.valueOf(e.getClaims().get("auth").toString()));
    } catch (Exception e) {
      throw new AccessDeniedException("token error");
    }
  }

  // 인증 객체 생성
  public Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }


  public void validateRefreshToken(String loginId, String refreshToken) {
    String redisRefreshToken = redisDao.getValues(loginId).toString();
    if (!redisRefreshToken.equals(refreshToken)) {
      throw new IllegalArgumentException("Refresh Token Inconsistency");
    }
  }

  public Long getVaildSeconds(String refreshToken) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken)
        .getBody();
    Long vaildSeconds = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
    return vaildSeconds;
  }

  public void checkBlackList(String refreshToken) {
    if (redisDao.existByKey(refreshToken)) {
      throw new IllegalArgumentException("로그아웃 된 토큰입니다.");
    }
  }
}
