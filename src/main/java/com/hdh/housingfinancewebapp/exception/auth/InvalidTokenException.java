package com.hdh.housingfinancewebapp.exception.auth;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException(String msg) {
    super(msg);
  }

  public InvalidTokenException(Exception e) {
    super(e);
  }

  public InvalidTokenException() {
    super();
  }
}

