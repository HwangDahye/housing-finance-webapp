package com.hdh.housingfinancewebapp.repository;

import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistory;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistoryPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditGuaranteeHistoryRepository extends JpaRepository<CreditGuaranteeHistory, CreditGuaranteeHistoryPK> {
  @Query(value="SELECT YEAR, BANK, SUM(AMOUNT) FROM TB_CREDIT_GUARANTEE_HISTORY GROUP BY(YEAR, BANK)", nativeQuery = true)
  List<Object[]> getTotalAmountOfYear();

  @Query(value="SELECT YEAR, BANK, AVG(AMOUNT) FROM TB_CREDIT_GUARANTEE_HISTORY WHERE BANK=?1 GROUP BY YEAR, BANK", nativeQuery = true)
  List<Object[]> getAverageOfYear(String bank);
}
