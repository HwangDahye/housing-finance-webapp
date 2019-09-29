package com.hdh.housingfinancewebapp.exception.advice;

import com.hdh.housingfinancewebapp.component.ResponseComponent;
import com.hdh.housingfinancewebapp.dto.response.CommonResult;
import com.hdh.housingfinancewebapp.enums.ResponseCodeEnums;
import com.hdh.housingfinancewebapp.exception.CsvFileReadException;
import com.hdh.housingfinancewebapp.exception.auth.AuthRequestException;
import com.hdh.housingfinancewebapp.exception.auth.DuplicatedUserException;
import com.hdh.housingfinancewebapp.exception.auth.InvalidTokenException;
import com.hdh.housingfinancewebapp.exception.auth.SigninFailedException;
import com.hdh.housingfinancewebapp.exception.auth.UserNotFoundException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
  final ResponseComponent responseComponent;

  public ExceptionAdvice(ResponseComponent responseComponent) {
    this.responseComponent = responseComponent;
  }

  @ExceptionHandler(SigninFailedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public CommonResult signinFailException(HttpServletResponse response) {
    response.setCharacterEncoding("UTF-8");
    return responseComponent.getFailResult(ResponseCodeEnums.SIGN_IN_FAIL_EXCEPTION.getCode(), ResponseCodeEnums.SIGN_IN_FAIL_EXCEPTION.getMsg());
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public CommonResult userNotFoundException(HttpServletResponse response) {
    response.setCharacterEncoding("UTF-8");
    return responseComponent.getFailResult(ResponseCodeEnums.USER_NOT_FOUND_EXCEPTION.getCode(), ResponseCodeEnums.USER_NOT_FOUND_EXCEPTION.getMsg());
  }

  @ExceptionHandler(DuplicatedUserException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public CommonResult duplicatedUserException(HttpServletResponse response) {
    response.setCharacterEncoding("UTF-8");
    return responseComponent.getFailResult(ResponseCodeEnums.DUPLICATED_USER_EXCEPTION.getCode(), ResponseCodeEnums.DUPLICATED_USER_EXCEPTION.getMsg());
  }

  @ExceptionHandler(InvalidTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public CommonResult invalidTokenException(HttpServletResponse response){
    response.setCharacterEncoding("UTF-8");
    return responseComponent.getFailResult(ResponseCodeEnums.INVALID_TOKEN_EXCEPTION.getCode(), ResponseCodeEnums.INVALID_TOKEN_EXCEPTION.getMsg());
  }

  @ExceptionHandler(AuthRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public CommonResult authRequestException(HttpServletResponse response){
    response.setCharacterEncoding("UTF-8");
    return responseComponent.getFailResult(ResponseCodeEnums.INVALID_AUTH_HEADER_EXCEPTION.getCode(), ResponseCodeEnums.INVALID_AUTH_HEADER_EXCEPTION.getMsg());
  }

  @ExceptionHandler(CsvFileReadException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public CommonResult csvFileReadException(HttpServletResponse response){
    response.setCharacterEncoding("UTF-8");
    return responseComponent.getFailResult(ResponseCodeEnums.FAIL_READ_CSV.getCode(), ResponseCodeEnums.FAIL_READ_CSV.getMsg());
  }
}
