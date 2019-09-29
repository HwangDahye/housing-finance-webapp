package com.hdh.housingfinancewebapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.component.ResponseComponent;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ResponseComponent responseComponent;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException,
      ServletException {
//    CommonResult forbiddenResult = responseComponent.getFailResult(ResponseCodeEnums.ACCESS_DENIED_EXCEPTION.getCode(), ResponseCodeEnums.ACCESS_DENIED_EXCEPTION.getMsg());
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setHeader("content-type", "application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("접근 권한이 없습니다");
    response.getWriter().flush();
    response.getWriter().close();
  }
}