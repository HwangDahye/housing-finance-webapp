package com.hdh.housingfinancewebapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.component.ResponseComponent;
import com.hdh.housingfinancewebapp.dto.response.CommonResult;
import com.hdh.housingfinancewebapp.enums.ResponseCodeEnums;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {   // 인증이 되지 않았을 경우, 발생하는 Exception Handling

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ResponseComponent responseComponent;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException ex) throws IOException {
    CommonResult result = responseComponent
        .getFailResult(ResponseCodeEnums.AUTH_FAIL_EXCEPTION.getCode(),
            ResponseCodeEnums.AUTH_FAIL_EXCEPTION.getMsg());

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(result));
    response.getWriter().flush();
    response.getWriter().close();
  }
}