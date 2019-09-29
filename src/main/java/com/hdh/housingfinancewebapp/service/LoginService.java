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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    // 유저가 존재하면, 비밀번호 일치하는지 확인함
    // [개선점] 아래 if문으로 해서, 맞지 않으면 SigninFailedException 던지는것이 좀더 명시적인것 같음
    // 현재는 authenticate 내부에서 호출되는 userService에서 수행하고 있음
    if(passwordEncoder.matches(password, user.getPassword())){
      // [개선점] 그래서 애초에 여기에 들어가는 pw 조차, 해싱된 pw가 들어갔음 좋겠음
      JwtAuthToken token = new JwtAuthToken(id, password);
      // 위에서 만든 인증요청한 사람의 인증정보를 가지고, 인증을 수행함 -> authProvider
      Authentication authentication = authenticationManager.authenticate(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      AuthResult result = (AuthResult) authentication.getDetails();
      user.setRefreshToken(result.getRefreshToken());
      // refreshToken 저장하기
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
        // accessToken 재발급
        String accessToken = jwtTokenProvider.createToken(id);
        AuthResult authResult = new AuthResult(accessToken, refreshToken);
        return responseComponent.getSuccessObjectResult(authResult);
      }else{
        throw new InvalidTokenException("RefreshToken이 유효하지 않습니다.");
      }
    }else{
      throw new InvalidTokenException("RefreshToken이 유효하지 않습니다.");
    }
  }
}
