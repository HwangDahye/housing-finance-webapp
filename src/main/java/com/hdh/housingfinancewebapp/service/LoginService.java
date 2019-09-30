package com.hdh.housingfinancewebapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.component.ResponseComponent;
import com.hdh.housingfinancewebapp.dto.request.SignUpReq;
import com.hdh.housingfinancewebapp.dto.response.AuthResult;
import com.hdh.housingfinancewebapp.dto.response.CommonResult;
import com.hdh.housingfinancewebapp.dto.response.ObjectResult;
import com.hdh.housingfinancewebapp.entity.User;
import com.hdh.housingfinancewebapp.exception.auth.AuthRequestException;
import com.hdh.housingfinancewebapp.exception.auth.DuplicatedUserException;
import com.hdh.housingfinancewebapp.exception.auth.InvalidTokenException;
import com.hdh.housingfinancewebapp.exception.auth.SigninFailedException;
import com.hdh.housingfinancewebapp.exception.auth.UserNotFoundException;
import com.hdh.housingfinancewebapp.security.JwtAuthToken;
import com.hdh.housingfinancewebapp.security.JwtTokenProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LoginService {
  @Autowired
  private UserService userService;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  ResponseComponent responseComponent;
  @Autowired
  ObjectMapper objectMapper;

  public CommonResult doSignUp(SignUpReq signUpReq) throws DuplicatedUserException{
    if(userService.isExistUser(signUpReq.getId()) == false){
      userService.saveUser(signUpReq);
    }
    else{
      throw new DuplicatedUserException();
    }
    return responseComponent.getSuccessResult();
  }

  public ObjectResult<AuthResult> doSignIn(String id, String password) throws UserNotFoundException, SigninFailedException, IllegalArgumentException{
    User user = userService.getUser(id);
    if(passwordEncoder.matches(password, user.getPassword())){
      JwtAuthToken token = new JwtAuthToken(id, password);
      Authentication authentication = authenticationManager.authenticate(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      AuthResult result = (AuthResult) authentication.getDetails();
      user.setRefreshToken(result.getRefreshToken());
      userService.saveUser(user);
      return responseComponent.getSuccessObjectResult(result);
    }else {
      throw new SigninFailedException("password Matches Fail");
    }
  }

  public ObjectResult<AuthResult> reissueAccessToken(String id,  String refreshToken){
    if(refreshToken == null)
      throw new AuthRequestException("Authorization 헤더값이 타당하지 않습니다");

    if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
      User user = userService.getUser(id);
      if(user.getRefreshToken().equals(refreshToken)){
        // accessToken reissue
        String accessToken = jwtTokenProvider.createToken(id);
        // refreshToken check expire time
        if(jwtTokenProvider.isReissueRefreshToken(refreshToken)){
          log.info("refreshToken reissue! request id : {}", id);
          refreshToken = jwtTokenProvider.createRefreshToken(id);
          user.setRefreshToken(refreshToken);
          userService.saveUser(user);
        }
        AuthResult authResult = new AuthResult(accessToken, refreshToken, user);
        return responseComponent.getSuccessObjectResult(authResult);
      }else{
        throw new InvalidTokenException("RefreshToken이 유효하지 않습니다.");
      }
    }else{
      throw new InvalidTokenException("RefreshToken이 유효하지 않습니다.");
    }
  }
}
