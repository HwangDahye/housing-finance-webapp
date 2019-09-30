package com.hdh.housingfinancewebapp.dto.response.finance;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TotalEachYearResult {
  private String name;
  private List<TotalEachYearResultContent> summarys;
}
