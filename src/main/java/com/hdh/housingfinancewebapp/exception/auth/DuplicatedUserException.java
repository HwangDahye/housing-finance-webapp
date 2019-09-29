package com.hdh.housingfinancewebapp.exception.auth;

public class DuplicatedUserException extends RuntimeException {
  public DuplicatedUserException() {}
  public DuplicatedUserException(String s){ super(s); }
}
