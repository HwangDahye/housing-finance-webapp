package com.hdh.housingfinancewebapp.dto.response.finance;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TotalEachYearResultContent {
  private int year;
  private int total_amount;
  private Map<String, Integer> detail_amount;
}
