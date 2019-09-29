package com.hdh.housingfinancewebapp.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TB_CREDIT_GUARANTEE_HISTORY")
public class CreditGuaranteeHistory {
  @EmbeddedId
  private CreditGuaranteeHistoryPK pk;
  @Column
  private int amount;
}
