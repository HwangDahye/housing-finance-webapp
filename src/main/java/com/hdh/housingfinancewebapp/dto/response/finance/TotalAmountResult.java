package com.hdh.housingfinancewebapp.dto.response.finance;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TotalAmountResult {
  private int year;
  private int totalAmount;
  private Map<String, Integer> detailAmount;
}
