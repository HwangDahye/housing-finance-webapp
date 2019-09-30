package com.hdh.housingfinancewebapp.exception.auth;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String msg) {
    super(msg);
  }

  public UserNotFoundException() {
    super();
  }
}
