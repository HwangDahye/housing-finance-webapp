package com.hdh.housingfinancewebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmountOfYear {
  private int year;
  private String bank;
  private int totalAmountOfBank;
}
