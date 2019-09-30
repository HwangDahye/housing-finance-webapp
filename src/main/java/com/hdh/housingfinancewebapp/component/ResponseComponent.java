package com.hdh.housingfinancewebapp.component;

import com.hdh.housingfinancewebapp.dto.response.CommonResult;
import com.hdh.housingfinancewebapp.dto.response.ObjectResult;
import com.hdh.housingfinancewebapp.enums.ResponseCodeEnums;
import org.springframework.stereotype.Component;

@Component
public class ResponseComponent {
  // 단일건 결과를 처리하는 메소드
  public <T> ObjectResult<T> getSuccessObjectResult(T data) {
    ObjectResult<T> result = new ObjectResult<>();
    result.setData(data);
    setSuccessResult(result);
    return result;
  }
  public <T> ObjectResult<T> getFailObjectResult(int code, String msg) {
    ObjectResult<T> result = new ObjectResult<>();
    result.setData(null);
    result.setSuccess(false);
    result.setCode(code);
    result.setMsg(msg);
    return result;
  }
  // 성공 결과만 처리하는 메소드
  public CommonResult getSuccessResult() {
    CommonResult result = new CommonResult();
    setSuccessResult(result);
    return result;
  }
  // 실패 결과만 처리하는 메소드
  public CommonResult getFailResult(int code, String msg) {
    CommonResult result = new CommonResult();
    result.setSuccess(false);
    result.setCode(code);
    result.setMsg(msg);
    return result;
  }
  // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
  private void setSuccessResult(CommonResult result) {
    result.setSuccess(true);
    result.setCode(ResponseCodeEnums.SUCCESS.getCode());
    result.setMsg(ResponseCodeEnums.SUCCESS.getMsg());
  }
}
