package com.hdh.housingfinancewebapp.exception.auth;

public class SigninFailedException extends RuntimeException {
  public SigninFailedException(String msg) {
    super(msg);
  }

  public SigninFailedException() {
    super();
  }
}
