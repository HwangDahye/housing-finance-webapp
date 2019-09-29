package com.hdh.housingfinancewebapp.dto.response.finance;

import com.hdh.housingfinancewebapp.dto.AmountOfYear;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvgAmountResultItem {
  private int year;
  private int amount;

  public AvgAmountResultItem(AmountOfYear obj){
    this.year = obj.getYear();
    this.amount = obj.getTotalAmountOfBank();
  }
}
