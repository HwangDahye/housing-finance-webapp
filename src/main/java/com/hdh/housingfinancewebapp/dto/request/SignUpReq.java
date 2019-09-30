package com.hdh.housingfinancewebapp.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReq {
  @NotBlank
  private String id;
  @NotBlank
  private String password;
  private String name;
}
