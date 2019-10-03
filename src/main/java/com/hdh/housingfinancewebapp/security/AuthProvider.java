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

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    User user =userService.getUser(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
    if(user != null){
      JwtAuthToken jwtAuthToken = new JwtAuthToken(user.getId(), user.getPassword(), createAuthorityList("ROLE_USER"));
      jwtAuthToken.setDetails(new AuthResult(jwtTokenProvider.createToken(user.getId()), jwtTokenProvider.createRefreshToken(user.getId()), user));

      return jwtAuthToken;
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return aClass.equals(JwtAuthToken.class);
  }
}
