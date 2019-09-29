package com.hdh.housingfinancewebapp.dto.response.finance;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvgAmountResult {
  private String bank;
  private List<AvgAmountResultItem> supportAmount;
}
