package com.hdh.housingfinancewebapp.entity;

import com.hdh.housingfinancewebapp.dto.TotalAmountGroupbyYearBankDto;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedNativeQuery(name = "CreditGuaranteeHistory.getTotalAmountGroupbyYearBank",
    query="SELECT T1.YEAR AS year, T2.INSTITUTE_NAME AS bank, SUM(T1.AMOUNT) AS totalAmount "
        + "FROM TB_CREDIT_GUARANTEE_HISTORY AS T1 "
        + "INNER JOIN TB_BANK  AS T2 ON T1.INSTITUTE_CODE = T2.INSTITUTE_CODE "
        + "GROUP BY(T1.YEAR, T1.INSTITUTE_CODE)",
    resultSetMapping = "TotalAmountGroupbyYearBankDtoMapping")
@SqlResultSetMapping(name = "TotalAmountGroupbyYearBankDtoMapping",
    classes = @ConstructorResult(
        targetClass = TotalAmountGroupbyYearBankDto.class,
        columns = {
            @ColumnResult(name="year", type = Integer.class),
            @ColumnResult(name="bank", type = String.class),
            @ColumnResult(name="totalAmount", type = Integer.class),
        }
    ))
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
