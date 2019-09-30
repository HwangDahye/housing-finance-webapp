package com.hdh.housingfinancewebapp.enums;

public enum ResponseCodeEnums {
  SUCCESS(0, "성공"),
  UNKNOWN_EXCEPTION(-1000, "알 수 없는 에러로 요청에 실패하였습니다"),
//  권한 관련
  SIGN_IN_FAIL_EXCEPTION(-1001, "비밀번호가 일치하지 않습니다. 비밀번호를 확인해주세요"),
  USER_NOT_FOUND_EXCEPTION(-1002, "회원 정보를 찾을 수 없습니다. 회원가입을 진행해주세요"),
  AUTH_FAIL_EXCEPTION(-1003, "인증 절차가 필요합니다. 로그인을 진행해 주세요"),
  DUPLICATED_USER_EXCEPTION(-1004, "중복된 아이디입니다"),
  ACCESS_DENIED_EXCEPTION(-1005, "접근 권한이 없습니다"),
  INVALID_TOKEN_EXCEPTION(-1006, "유효하지 않은 토큰입니다. 토큰 갱신 요청을 하거나, 재 로그인 해주세요."),
  INVALID_AUTH_HEADER_EXCEPTION(-1007, "Authorization 헤더값이 타당하지 않습니다."),
//  서버 비즈니스 로직 관련
  FAIL_READ_CSV(-2001, "csv 파일 읽기 도중 에러가 발생하였습니다."),
  FAIL_SAVE_CSV_DATA(-2002, "csv 파일 파싱결과가 존재하지 않습니다."),
  FAIL_TOTAL_AMOUNT_OF_YEAR(-2003, "년도별 각 기관의 합계 연산 결과가 존재하지 않습니다"),
  FAIL_DB_RESULT_EMPTY(-2004, "DB 조회 결과가 존재하지 않습니다");

  private int code;
  private String msg;

  ResponseCodeEnums(int code, String msg){
    this.code = code;
    this.msg = msg;
  }

  public int getCode(){
    return this.code;
  }

  public String getMsg()  {
    return this.msg;
  }
}
