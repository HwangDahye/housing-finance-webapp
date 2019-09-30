package com.hdh.housingfinancewebapp.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class AuthRequestException extends AuthenticationException {
  public AuthRequestException(String s){ super(s); }
}
