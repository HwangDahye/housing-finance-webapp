package com.hdh.housingfinancewebapp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CreditGuaranteeHistoryPK implements Serializable {
  private static final long serialVersionUID = 1L;
//  TODO : getTotalAmountOfYear 에서 month는 select 하지 않음. 그래서 nullable 속성 지워보려고 주석처리함
//  @Column(name = "year", nullable = false)
  private int year;

//  @Column(name = "month", nullable = false)
  private int month;

//  @Column(name = "bank", nullable = false)
  @ManyToOne
  @JoinColumn(name ="institute_code")
  private Bank bank;
}
