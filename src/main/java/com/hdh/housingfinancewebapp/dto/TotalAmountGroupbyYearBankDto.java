package com.hdh.housingfinancewebapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalAmountGroupbyYearBankDto {
  private Integer year;
  private String bank;
  private Integer totalAmount;

  public TotalAmountGroupbyYearBankDto(Integer year, String bank, Integer totalAmount){
    this.year = year;
    this.bank = bank;
    this.totalAmount = totalAmount;
  }
}
