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
  @Column(nullable = false)
  private int year;

  @Column(nullable = false)
  private int month;

  @ManyToOne
  @JoinColumn(name ="institute_code")
  private Bank bank;
}
