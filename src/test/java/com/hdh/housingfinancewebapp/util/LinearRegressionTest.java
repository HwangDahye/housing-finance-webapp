package com.hdh.housingfinancewebapp.util;

import com.hdh.housingfinancewebapp.component.LinearRegressionComponent;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistory;
import com.hdh.housingfinancewebapp.service.FinanceService;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LinearRegressionTest {
    @Autowired private LinearRegressionComponent linearRegressionComponent;

    @Autowired private FinanceService financeService;

    private List<CreditGuaranteeHistory> datas;

    @Before
    public void init(){
      datas = financeService.load().getData();
    }

    @Test
  public void doInit(){
      List<CreditGuaranteeHistory> datasOfSpecificBank = datas.stream()
                                                                .filter(item -> item.getPk().getBank().equals("기타은행"))
                                                                .filter(item -> item.getPk().getMonth() == 7)
                                                                .collect(Collectors.toList());
      int[] amounts = datasOfSpecificBank.stream().mapToInt(item -> item.getAmount()).toArray();
      int[] years = datasOfSpecificBank.stream().mapToInt(item -> item.getPk().getYear()).toArray();

      linearRegressionComponent.process(years, amounts);
      System.out.println(linearRegressionComponent.predict(2018));
    }
}
