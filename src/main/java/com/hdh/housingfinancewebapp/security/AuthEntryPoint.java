package com.hdh.housingfinancewebapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.component.ResponseComponent;
import com.hdh.housingfinancewebapp.dto.response.CommonResult;
import com.hdh.housingfinancewebapp.enums.ResponseCodeEnums;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ResponseComponent responseComponent;

  // auth exception handling하는 친구, security configure에 정의되어 있음
  // Filter에서 validateToken 실패하니까 여기로 빠짐
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException ex) throws IOException {
    CommonResult result = responseComponent
        .getFailResult(ResponseCodeEnums.AUTH_FAIL_EXCEPTION.getCode(),
            ResponseCodeEnums.AUTH_FAIL_EXCEPTION.getMsg());

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setHeader("content-type", "application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(result));
    response.getWriter().flush();
    response.getWriter().close();
  }
}