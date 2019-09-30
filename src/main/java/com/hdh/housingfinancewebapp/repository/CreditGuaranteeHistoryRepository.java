package com.hdh.housingfinancewebapp.repository;

import com.hdh.housingfinancewebapp.dto.TotalAmountGroupbyYearBankDto;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistory;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistoryPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditGuaranteeHistoryRepository extends JpaRepository<CreditGuaranteeHistory, CreditGuaranteeHistoryPK> {
  @Query(nativeQuery = true)
  List<TotalAmountGroupbyYearBankDto> getTotalAmountGroupbyYearBank();
}
