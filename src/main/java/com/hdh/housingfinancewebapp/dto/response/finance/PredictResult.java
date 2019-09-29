package com.hdh.housingfinancewebapp.dto.response.finance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PredictResult {
  private String bank;
  private int year;
  private int month;
  private int amount;
}
