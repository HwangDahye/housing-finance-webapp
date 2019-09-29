package com.hdh.housingfinancewebapp.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.hdh.housingfinancewebapp.dto.response.AuthResult;
import com.hdh.housingfinancewebapp.entity.User;
import com.hdh.housingfinancewebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {

  @Autowired
  UserService userService;

  @Autowired
  JwtTokenProvider jwtTokenProvider;

  // 로그인 과정에서, 요청한 인증정보를 가지고 인증 처리함
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 인증 정보에 담긴, principal(에 id담겨있음), credential(에 pw담겨있음)로 user 조회해봄
    // user 조회 했을 때, 비밀번호가 안맞으면, 여기서 SigninFailException 발생함
    User user =userService.getUser(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
    if(user != null){
      // user 정보가 모두 유효하면, 토큰을 만든다
      // role 은 USER로 준다
      JwtAuthToken jwtAuthToken = new JwtAuthToken(user.getId(), user.getPassword(), user.getUserSeq(), createAuthorityList("ROLE_USER"));
      // detail에는 반환될 정보를 담아서 준다
      jwtAuthToken.setDetails(new AuthResult(jwtTokenProvider.createToken(user.getId()), jwtTokenProvider.createRefreshToken(user.getId())));

      return jwtAuthToken;
    }
    // [개선점] null 일때에도, 예외 하나 날려줘야 하지 않았을까
    return null;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return aClass.equals(JwtAuthToken.class);
  }
}
