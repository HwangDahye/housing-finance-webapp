package com.hdh.housingfinancewebapp.controller;

import com.hdh.housingfinancewebapp.dto.request.SignUpReq;
import com.hdh.housingfinancewebapp.dto.response.AuthResult;
import com.hdh.housingfinancewebapp.dto.response.CommonResult;
import com.hdh.housingfinancewebapp.dto.response.ObjectResult;
import com.hdh.housingfinancewebapp.exception.auth.AuthRequestException;
import com.hdh.housingfinancewebapp.exception.auth.DuplicatedUserException;
import com.hdh.housingfinancewebapp.exception.auth.SigninFailedException;
import com.hdh.housingfinancewebapp.exception.auth.UserNotFoundException;
import com.hdh.housingfinancewebapp.security.JwtTokenProvider;
import com.hdh.housingfinancewebapp.service.LoginService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/auth")
public class LoginRestController {
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @Autowired private LoginService loginService;

  @PostMapping(path="/signup")
  public CommonResult signup(@RequestBody @Valid SignUpReq signUpReq, HttpServletResponse res) throws DuplicatedUserException {
    CommonResult result = loginService.doSignUp(signUpReq);
    res.setStatus(HttpStatus.CREATED.value());
    return result;
  }

  @GetMapping(path="/signin")
  public ObjectResult<AuthResult> signin(@RequestParam String id, @RequestParam String password)
      throws UserNotFoundException, SigninFailedException, IllegalArgumentException{
    return loginService.doSignIn(id, password);
  }

  @GetMapping(path="/refresh/token")
  public ObjectResult<AuthResult> refreshToken(@RequestParam String id,  HttpServletRequest req) throws AuthRequestException {
    String refreshToken = jwtTokenProvider.resolveToken(req);
    return loginService.reissueAccessToken(id, refreshToken);
  }
}
