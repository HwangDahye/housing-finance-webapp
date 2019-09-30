package com.hdh.housingfinancewebapp.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.hdh.housingfinancewebapp.entity.User;
import com.hdh.housingfinancewebapp.exception.auth.AuthRequestException;
import com.hdh.housingfinancewebapp.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {   // JWT 토큰 생성 및 검증 모듈

  @Value("${spring.jwt.secret}")
  private String secretKey;

  @Value("${spring.jwt.refresh.secret}")
  private String refreshSecretKey;

  private final String BEARER_TYPE = "Bearer";

  private long tokenValidMillisecond = 1000L * 60 * 60; // 만료시간 1시간

  private long refreshTokenValidMillisecond = 1000L * 60 * 60 * 24 * 14; // 만료시간 14일

  @Autowired
  private UserService userService;

  @PostConstruct
  protected void init(){
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  // Jwt 토큰 생성
  public String createToken(String userId){
    Claims claims = Jwts.claims().setSubject(userId);   // 회원을 구분할 수 있는 정보

    Date now = new Date();

    return Jwts.builder()
              .setHeaderParam("typ", "JWT")
              .setClaims(claims)
              .setIssuedAt(now)   // 토큰 발행일자
              .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
              .signWith(SignatureAlgorithm.HS256, secretKey)
              .compact();
  }

  // refresh Token 생성
  public String createRefreshToken(String userId){
    Claims claims = Jwts.claims().setSubject(userId);   // 회원을 구분할 수 있는 정보

    Date now = new Date();

    return Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setClaims(claims)
        .setIssuedAt(now)   // 토큰 발행일자
        .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
        .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
        .compact();
  }

  // Jwt 토큰으로 인증 정보를 조회
  public Authentication getAuthentication(String token) {
    User user = userService.getUser(this.getUserId(token));
    return new JwtAuthToken(user.getId(), user.getPassword(), createAuthorityList("ROLE_USER"));
  }

  // Jwt 토큰에서 회원 구별 정보 추출
  public String getUserId(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  // Request의 Header에서 token 파싱 : "Authorization: Bearer jwt토큰"
  public String resolveToken(HttpServletRequest req) throws AuthRequestException{
    String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
    if(authHeader == null){
      return null;
    }
    String[] authHeaders = authHeader.split(" ");
    if(authHeaders.length != 2){
      return null;
    }
    if(!authHeaders[0].equals(BEARER_TYPE)){
      return null;
    }
    return authHeaders[1];
  }

  // Jwt 토큰의 유효성 + 만료일자 확인
  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  // Jwt Refresh 토큰의 유효성 + 만료일자 확인
  public boolean validateRefreshToken(String refreshToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  // Jwt Refresh 토큰 갱신여부 체크
  public boolean isReissueRefreshToken(String refreshToken){
    try {
      Date now = new Date();
      Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken);
      return claims.getBody().getExpiration().before(new Date(now.getTime() + refreshTokenValidMillisecond/2));
    } catch (Exception e) {
      return true;
    }
  }
}
