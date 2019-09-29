package com.hdh.housingfinancewebapp.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ObjectResult<T> extends CommonResult {
  private T data;
}
