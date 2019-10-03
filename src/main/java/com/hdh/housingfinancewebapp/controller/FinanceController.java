package com.hdh.housingfinancewebapp.controller;

import com.hdh.housingfinancewebapp.dto.response.ObjectResult;
import com.hdh.housingfinancewebapp.dto.response.finance.MinMaxAvgAmountResult;
import com.hdh.housingfinancewebapp.dto.response.finance.PredictResult;
import com.hdh.housingfinancewebapp.dto.response.finance.TopBankResult;
import com.hdh.housingfinancewebapp.dto.response.finance.TotalEachYearResult;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistory;
import com.hdh.housingfinancewebapp.service.FinanceService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {
  @Autowired
  FinanceService financeService;

  @PostMapping(path = "/load")
  public ObjectResult<List<CreditGuaranteeHistory>> load(){
    return financeService.load();
  }

  @GetMapping(path = "/banks")
  public ObjectResult<Map<String, String>> getBanks(){
    return financeService.getBanks();
  }

  @GetMapping(path = "/total/amount")
  public ObjectResult<TotalEachYearResult> getTotalEachYear(){
    return financeService.getTotalEachYear();
  }

  @GetMapping(path = "/top")
  public ObjectResult<TopBankResult> getTopBank(){
    return financeService.getTopBank();
  }

  @GetMapping(path = "/minmax/avg/amount")
  public ObjectResult<MinMaxAvgAmountResult> getMinMaxAvgAmount(@RequestParam(value="bank", required = false) Optional<String> bank){
    return financeService.getMinMaxAvgAmount(bank.isPresent() ? bank.get() : "외환은행");
  }

  @GetMapping(path = "/predict")
  public ObjectResult<PredictResult> doPredict(@RequestParam String bank, @RequestParam int month){
    return financeService.doPredict(bank, month);
  }
}
