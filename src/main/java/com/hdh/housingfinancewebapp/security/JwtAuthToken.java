package com.hdh.housingfinancewebapp.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

// 인증 요청을 한 사람에 대한 인증정보를 꾸리는 객체임과 동시에
// 인증처리가 완료되면, 반환하는 객체가 됨
// 즉, 인증 요청시, 완료 시 전달되고 반환되는 객체임
public class JwtAuthToken extends AbstractAuthenticationToken {
  private Object principal;
  private String credentials;

  public JwtAuthToken(String principal, String credentials) {
    super(null);
    super.setAuthenticated(false);

    this.principal = principal;
    this.credentials = credentials;
  }

  // 빈 객체로 사용하지 않기 때문에, 딱히 public을 붙이진 않았음. 의도한건 아님
  public JwtAuthToken(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    super.setAuthenticated(true);

    this.principal = principal;
    this.credentials = credentials;
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }
}
