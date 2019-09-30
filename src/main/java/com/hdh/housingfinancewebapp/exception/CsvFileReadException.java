package com.hdh.housingfinancewebapp.exception;

public class CsvFileReadException extends RuntimeException {
  public CsvFileReadException() {}
  public CsvFileReadException(String s){ super(s); }
  public CsvFileReadException(Exception e){ super(e); }
}
