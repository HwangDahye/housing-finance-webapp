package com.hdh.housingfinancewebapp.service;

import static com.hdh.housingfinancewebapp.enums.ResponseCodeEnums.FAIL_DB_RESULT_EMPTY;
import static com.hdh.housingfinancewebapp.enums.ResponseCodeEnums.FAIL_SAVE_CSV_DATA;
import static com.hdh.housingfinancewebapp.enums.ResponseCodeEnums.FAIL_TOTAL_AMOUNT_OF_YEAR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.component.CsvComponent;
import com.hdh.housingfinancewebapp.component.LinearRegressionComponent;
import com.hdh.housingfinancewebapp.component.ResponseComponent;
import com.hdh.housingfinancewebapp.dto.AmountOfYear;
import com.hdh.housingfinancewebapp.dto.response.ObjectResult;
import com.hdh.housingfinancewebapp.dto.response.finance.AvgAmountResult;
import com.hdh.housingfinancewebapp.dto.response.finance.AvgAmountResultItem;
import com.hdh.housingfinancewebapp.dto.response.finance.PredictResult;
import com.hdh.housingfinancewebapp.dto.response.finance.TopBankResult;
import com.hdh.housingfinancewebapp.dto.response.finance.TotalAmountResult;
import com.hdh.housingfinancewebapp.entity.Bank;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistory;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistoryPK;
import com.hdh.housingfinancewebapp.exception.CsvFileReadException;
import com.hdh.housingfinancewebapp.repository.BankRepository;
import com.hdh.housingfinancewebapp.repository.CreditGuaranteeHistoryRepository;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FinanceService {
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  CreditGuaranteeHistoryRepository historyRepo;

  @Autowired
  BankRepository bankRepository;

  @Autowired
  ResponseComponent responseComponent;

  @Autowired
  LinearRegressionComponent linearRegressionComponent;

  @Autowired
  CsvComponent csvComponent;

  @Value("${csv.file.name}")
  String csvFIleName;

  @Value("${csv.file.header.row.idx}")
  int csvHeaderRow;

  @Value("${csv.file.data.row.idx}")
  int csvDataRow;

  @Value("${csv.file.bank.start.cell.idx}")
  int csvBankStartIdx;

  @Value("${csv.file.bank.year.cell.idx}")
  int csvYearIdx;

  @Value("${csv.file.bank.month.cell.idx}")
  int csvMonthIdx;

  private final int PREDICT_YEAR = 2018;

  public ObjectResult<List<CreditGuaranteeHistory>> load(){
    List<List<String>> records;
    try{
      records = csvComponent.readCSV(csvFIleName);
    }catch (IOException e){
      throw new CsvFileReadException(e);
    }

    // TODO 이거 필요한걸까?
    List<Bank> banks = records.get(csvHeaderRow)
                                      .subList(csvBankStartIdx, records.get(0).size())
                                      .stream().map(item -> new Bank(item, item)).collect(Collectors.toList());
//    bankRepository.saveAll(banks);

    records = records.subList(csvDataRow, records.size());

    List<CreditGuaranteeHistory> creditGuaranteeHistoryList = new ArrayList<>();

    // TODO : 더 좋은 방법이 있을까?
    for(List<String> columns : records){
      int year = Integer.parseInt(columns.get(csvYearIdx));
      int month = Integer.parseInt(columns.get(csvMonthIdx));

      for(int idx = 0; idx < banks.size(); idx++){
        String bank = banks.get(idx).getInstituteCode();
        int amount = Integer.parseInt(columns.get(csvBankStartIdx + idx));

        CreditGuaranteeHistory history = new CreditGuaranteeHistory(new CreditGuaranteeHistoryPK(year, month, bank) ,amount);
        creditGuaranteeHistoryList.add(history);
      }
    }

    if(creditGuaranteeHistoryList.size() > 0){
      historyRepo.saveAll(creditGuaranteeHistoryList);
      return responseComponent.getSuccessObjectResult(creditGuaranteeHistoryList);
    }else{
      return responseComponent.getFailObjectResult(FAIL_SAVE_CSV_DATA.getCode(), FAIL_SAVE_CSV_DATA.getMsg());
    }
  }

  public ObjectResult<Map<String, String>> getBanks(){
    Map<String, String> banks = bankRepository.findAll().stream()
                                        .collect(Collectors.toMap(Bank::getInstituteCode, Bank::getInstituteName));
    if(banks.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }
    return responseComponent.getSuccessObjectResult(banks);
  }

  public ObjectResult<List<TotalAmountResult>> getTotalAmountOfYear(){
    List<TotalAmountResult> results = new ArrayList<>();

    List<Object[]> repoResults = historyRepo.getTotalAmountOfYear();

    if(repoResults.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }

    TreeMap<Integer, List<AmountOfYear>> totalAmountGroupByYearMap = repoResults.stream().map(obj -> new AmountOfYear((int) obj[0],(String) obj[1],((BigInteger) obj[2]).intValue()))
                                                                        .collect(Collectors.groupingBy(AmountOfYear::getYear, TreeMap::new, Collectors.toList()));


    totalAmountGroupByYearMap.entrySet().stream().forEach(entry -> {
      List<AmountOfYear> amountOfYearList = entry.getValue();

      int totalAmount = amountOfYearList.stream().collect(Collectors.summingInt(AmountOfYear::getTotalAmountOfBank));
      Map<String, Integer> detailAmount = amountOfYearList.stream().collect(Collectors.toMap(AmountOfYear::getBank, AmountOfYear::getTotalAmountOfBank));

      results.add(new TotalAmountResult(entry.getKey(), totalAmount, detailAmount));
    });

    if(results.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_TOTAL_AMOUNT_OF_YEAR.getCode(), FAIL_TOTAL_AMOUNT_OF_YEAR.getMsg());
    }
    return responseComponent.getSuccessObjectResult(results);
  }

  public ObjectResult<TopBankResult> getTopInstitution(){
    TopBankResult result = new TopBankResult();

    List<Object[]> repoResults = historyRepo.getTotalAmountOfYear();

    if(repoResults.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }

    AmountOfYear top = repoResults.stream().map(obj-> new AmountOfYear((int) obj[0], (String) obj[1],((BigInteger) obj[2]).intValue()))
                                            .max(Comparator.comparing(AmountOfYear::getTotalAmountOfBank))
                                            .orElseThrow(NoSuchElementException::new);
    result.setBank(top.getBank());
    result.setYear(top.getYear());
    return responseComponent.getSuccessObjectResult(result);
  }

  public ObjectResult<AvgAmountResult> getMaxAndMinAvgAmountOfYear(String bank){
    AvgAmountResult result = new AvgAmountResult();
    List<Object[]> repoResults = historyRepo.getTotalAmountOfYear();

    if(repoResults.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }

    List<AmountOfYear> avgListByBank = repoResults.stream()
        .filter(obj -> (obj[1]).equals(bank))
        .filter(obj -> (int) obj[0] < 2017)
        .map(obj -> new AmountOfYear((int) obj[0],(String) obj[1], Math.round( ((BigInteger) obj[2]).floatValue() / 12)) )
        .collect(Collectors.toList());


    AmountOfYear min = avgListByBank.stream().min(Comparator.comparing(AmountOfYear::getTotalAmountOfBank)).orElseThrow(NoSuchElementException::new);;
    AmountOfYear max = avgListByBank.stream().max(Comparator.comparing(AmountOfYear::getTotalAmountOfBank)).orElseThrow(NoSuchElementException::new);;


    result.setBank(bank);
    result.setSupportAmount(Arrays.asList(new AvgAmountResultItem(min), new AvgAmountResultItem(max)));

    return responseComponent.getSuccessObjectResult(result);
  }

  public ObjectResult<PredictResult> doPredict(String bank, int month){
    Bank bankObj = bankRepository.findByInstituteName(bank);

    List<CreditGuaranteeHistory> datasOfSpecificBank = historyRepo.findAll().stream()
        .filter(item -> item.getPk().getBank().equals(bank))
        .filter(item -> item.getPk().getMonth() == month)
        .collect(Collectors.toList());

    if(datasOfSpecificBank.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }

    int[] amounts = datasOfSpecificBank.stream().mapToInt(item -> item.getAmount()).toArray();
    int[] years = datasOfSpecificBank.stream().mapToInt(item -> item.getPk().getYear()).toArray();

    linearRegressionComponent.process(years, amounts);

    int predictResult = linearRegressionComponent.predict(PREDICT_YEAR);
    PredictResult result = new PredictResult(bankObj.getInstituteCode(), PREDICT_YEAR, month, predictResult);

    return responseComponent.getSuccessObjectResult(result);
  }
}
