package com.hdh.housingfinancewebapp.dto.response;

import com.hdh.housingfinancewebapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResult {
  private String token;
  private String refreshToken;
//  private User user;
}
