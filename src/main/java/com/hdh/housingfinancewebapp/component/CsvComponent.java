package com.hdh.housingfinancewebapp.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CsvComponent {
  private static final String COMMA_PATTERN = ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";
  private static final String ETC_STRINGS = "[ !@#$%^&*(),.?\\\":{}|<>]";
  private static final String CURRENCY_UNIT_TEXT = "1\\)\\(억원\\)|\\(억원\\)";

  public List<List<String>> readCSV(InputStream inputStream) throws IOException {
    List<List<String>> records = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] columnArr = line.split(COMMA_PATTERN);
        // 빈 cell 미포함, 통화단위 제거, 그외 특수문자 제거
        List<String> columns = Arrays.asList(columnArr).stream()
            .filter(col -> col != null && !col.isEmpty())
            .map(col -> {
              if(Pattern.compile(CURRENCY_UNIT_TEXT).matcher(col).find()){
                return col.replaceAll(CURRENCY_UNIT_TEXT, "");
              }else{
                return col.replaceAll(ETC_STRINGS, "");
              }
            })
            .collect(Collectors.toList());
        records.add(columns);
      }
    }
    return records;
  }
}
