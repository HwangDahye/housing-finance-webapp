package com.hdh.housingfinancewebapp.service;

import static com.hdh.housingfinancewebapp.enums.ResponseCodeEnums.FAIL_DB_RESULT_EMPTY;
import static com.hdh.housingfinancewebapp.enums.ResponseCodeEnums.FAIL_SAVE_CSV_DATA;
import static com.hdh.housingfinancewebapp.enums.ResponseCodeEnums.FAIL_TOTAL_AMOUNT_OF_YEAR;
import static com.hdh.housingfinancewebapp.enums.ResponseCodeEnums.INVALID_MONTH_PARAM;
import static com.hdh.housingfinancewebapp.enums.ResponseCodeEnums.NOT_EXIST_BANK_PARAM;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.component.CsvComponent;
import com.hdh.housingfinancewebapp.component.LinearRegressionComponent;
import com.hdh.housingfinancewebapp.component.ResponseComponent;
import com.hdh.housingfinancewebapp.dto.TotalAmountGroupbyYearBankDto;
import com.hdh.housingfinancewebapp.dto.response.ObjectResult;
import com.hdh.housingfinancewebapp.dto.response.finance.MinMaxAvgAmountResult;
import com.hdh.housingfinancewebapp.dto.response.finance.MinMaxAvgAmountResultItem;
import com.hdh.housingfinancewebapp.dto.response.finance.PredictResult;
import com.hdh.housingfinancewebapp.dto.response.finance.TopBankResult;
import com.hdh.housingfinancewebapp.dto.response.finance.TotalEachYearResult;
import com.hdh.housingfinancewebapp.dto.response.finance.TotalEachYearResultContent;
import com.hdh.housingfinancewebapp.entity.Bank;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistory;
import com.hdh.housingfinancewebapp.entity.CreditGuaranteeHistoryPK;
import com.hdh.housingfinancewebapp.exception.CsvFileReadException;
import com.hdh.housingfinancewebapp.repository.BankRepository;
import com.hdh.housingfinancewebapp.repository.CreditGuaranteeHistoryRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Log4j2
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

  private static final int PREDICT_YEAR = 2018;
  private static final String BANK_CODE_PREFIX = "bnk";

  private List<Bank> banks; // TODO

  public ObjectResult<List<CreditGuaranteeHistory>> load(){
    clearData();

    Resource resource = new ClassPathResource("/assets/"+csvFIleName);

    // read csv file
    List<List<String>> records;
    try{
      records = csvComponent.readCSV(resource.getInputStream());
    }catch (IOException e){
      throw new CsvFileReadException(e);
    }

    // create bank list
    List<String> bankNames = records.get(csvHeaderRow).subList(csvBankStartIdx, records.get(0).size());
    banks = createBanks(bankNames);
    bankRepository.saveAll(banks);

    // create history list
    records = records.subList(csvDataRow, records.size());

    List<CreditGuaranteeHistory> historyList = new ArrayList<>();

    for(List<String> columns : records){
      int year = Integer.parseInt(columns.get(csvYearIdx));
      int month = Integer.parseInt(columns.get(csvMonthIdx));

      historyList.addAll(IntStream.range(0, banks.size())
                            .mapToObj(idx ->
                                new CreditGuaranteeHistory(
                                    new CreditGuaranteeHistoryPK(year, month, banks.get(idx)),
                                    Integer.parseInt(columns.get(csvBankStartIdx + idx)))
                            ).collect(Collectors.toList()));

    }

    if(historyList.size() > 0){
      historyRepo.saveAll(historyList);
      return responseComponent.getSuccessObjectResult(historyList);
    }else{
      return responseComponent.getFailObjectResult(FAIL_SAVE_CSV_DATA.getCode(), FAIL_SAVE_CSV_DATA.getMsg());
    }
  }

  public ObjectResult<Map<String, String>> getBanks(){
    Map<String, String> banks = bankRepository.findAllByOrderByInstituteCodeAsc().stream()
                                        .collect(Collectors.toMap(Bank::getInstituteCode, Bank::getInstituteName));

    if(banks.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }
    TreeMap<String, String> results = new TreeMap<>(banks);
    return responseComponent.getSuccessObjectResult(results);
  }

  public ObjectResult<TotalEachYearResult> getTotalEachYear(){
    List<TotalEachYearResultContent> contents = new ArrayList<>();

    List<TotalAmountGroupbyYearBankDto> totalList = historyRepo.getTotalAmountGroupbyYearBank();

    if(totalList.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }

    TreeMap<Integer, List<TotalAmountGroupbyYearBankDto>> taMap = totalList.stream().collect(Collectors.groupingBy(TotalAmountGroupbyYearBankDto::getYear, TreeMap::new, Collectors.toList()));

    taMap.entrySet().stream().forEach(entry -> {
      List<TotalAmountGroupbyYearBankDto> taList = entry.getValue();

      int totalAmount = taList.stream().collect(Collectors.summingInt(TotalAmountGroupbyYearBankDto::getTotalAmount));
      Map<String, Integer> detailAmount = taList.stream().collect(Collectors.toMap(TotalAmountGroupbyYearBankDto::getBank, TotalAmountGroupbyYearBankDto::getTotalAmount));

      contents.add(new TotalEachYearResultContent(entry.getKey(), totalAmount, detailAmount));
    });

    if(contents.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_TOTAL_AMOUNT_OF_YEAR.getCode(), FAIL_TOTAL_AMOUNT_OF_YEAR.getMsg());
    }

    TotalEachYearResult result = new TotalEachYearResult("주택금융 공급현황", contents);
    return responseComponent.getSuccessObjectResult(result);
  }

  public ObjectResult<TopBankResult> getTopBank(){
    TopBankResult result = new TopBankResult();

    List<TotalAmountGroupbyYearBankDto> totalList = historyRepo.getTotalAmountGroupbyYearBank();

    if(totalList.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }

    TotalAmountGroupbyYearBankDto top = totalList.stream()
                                            .max(Comparator.comparing(TotalAmountGroupbyYearBankDto::getTotalAmount))
                                            .orElseThrow(NoSuchElementException::new);
    result.setBank(top.getBank());
    result.setYear(top.getYear());
    return responseComponent.getSuccessObjectResult(result);
  }

  public ObjectResult<MinMaxAvgAmountResult> getMinMaxAvgAmount(String bank){
    if(!isExistBank(bank)){
      return responseComponent.getFailObjectResult(NOT_EXIST_BANK_PARAM.getCode(), NOT_EXIST_BANK_PARAM.getMsg());
    }

    MinMaxAvgAmountResult result = new MinMaxAvgAmountResult();
    List<TotalAmountGroupbyYearBankDto> totalList = historyRepo.getTotalAmountGroupbyYearBank();

    if(totalList.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }

    List<TotalAmountGroupbyYearBankDto> avgListByBank = totalList.stream()
        .filter(obj -> obj.getBank().equals(bank))
        .filter(obj -> obj.getYear() < 2017)
        .map(obj -> {
          int avg = Math.round(obj.getTotalAmount().floatValue() / 12);
          obj.setTotalAmount(avg);
          return obj;
        })
        .collect(Collectors.toList());


    TotalAmountGroupbyYearBankDto min = avgListByBank.stream().min(Comparator.comparing(TotalAmountGroupbyYearBankDto::getTotalAmount)).orElseThrow(NoSuchElementException::new);
    TotalAmountGroupbyYearBankDto max = avgListByBank.stream().max(Comparator.comparing(TotalAmountGroupbyYearBankDto::getTotalAmount)).orElseThrow(NoSuchElementException::new);


    result.setBank(bank);
    result.setSupport_amount(Arrays.asList(new MinMaxAvgAmountResultItem(min), new MinMaxAvgAmountResultItem(max)));

    return responseComponent.getSuccessObjectResult(result);
  }

  public ObjectResult<PredictResult> doPredict(String bank, int month){
    if(!isExistBank(bank)){
      return responseComponent.getFailObjectResult(NOT_EXIST_BANK_PARAM.getCode(), NOT_EXIST_BANK_PARAM.getMsg());
    }

    if(month < 1 || month > 12){
      return responseComponent.getFailObjectResult(INVALID_MONTH_PARAM.getCode(), INVALID_MONTH_PARAM.getMsg());
    }

    Bank bankObj = bankRepository.findByInstituteName(bank);

    List<CreditGuaranteeHistory> dataOfSpecificBank = historyRepo.findAll().stream()
        .filter(item -> item.getPk().getBank().getInstituteName().equals(bank))
        .filter(item -> item.getPk().getMonth() == month)
        .collect(Collectors.toList());

    if(dataOfSpecificBank.size() == 0){
      return responseComponent.getFailObjectResult(FAIL_DB_RESULT_EMPTY.getCode(), FAIL_DB_RESULT_EMPTY.getMsg());
    }

    int[] years = dataOfSpecificBank.stream().mapToInt(item -> item.getPk().getYear()).toArray();
    int[] amounts = dataOfSpecificBank.stream().mapToInt(item -> item.getAmount()).toArray();

    linearRegressionComponent.process(years, amounts);

    log.info("linearRegression Hypothesis Function = {}", linearRegressionComponent.toString());

    int predictResult = linearRegressionComponent.predict(PREDICT_YEAR);
    PredictResult result = new PredictResult(bankObj.getInstituteCode(), PREDICT_YEAR, month, predictResult);

    return responseComponent.getSuccessObjectResult(result);
  }

  private void clearData(){
    historyRepo.deleteAll();
    bankRepository.deleteAll();
  }

  private List<Bank> createBanks(List<String> bankNames){
    List<Bank> results = IntStream.range(0, bankNames.size()).mapToObj(idx -> new Bank(BANK_CODE_PREFIX.concat(String.format("%03d", idx)), bankNames.get(idx))).collect(Collectors.toList());
    log.debug("createBanks result => {}", results);
    return results;
  }

  private boolean isExistBank(String bank){
    Bank findResult = bankRepository.findByInstituteName(bank);
    return findResult != null ? true : false;
  }
}
