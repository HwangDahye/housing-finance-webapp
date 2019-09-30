package com.hdh.housingfinancewebapp.dto.response.finance;

import com.hdh.housingfinancewebapp.dto.TotalAmountGroupbyYearBankDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MinMaxAvgAmountResultItem {
  private int year;
  private int amount;

  public MinMaxAvgAmountResultItem(TotalAmountGroupbyYearBankDto obj){
    this.year = obj.getYear();
    this.amount = obj.getTotalAmount();
  }
}
