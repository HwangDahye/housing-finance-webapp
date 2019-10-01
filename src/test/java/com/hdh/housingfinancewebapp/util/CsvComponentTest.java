package com.hdh.housingfinancewebapp.util;

import com.hdh.housingfinancewebapp.component.CsvComponent;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CsvComponent.class)
public class CsvComponentTest {
  @Autowired
  CsvComponent csvComponent;

  @Value("${csv.file.name}")
  String fileName;
  @Test
  public void csvComponentTest(){
    try {
      Resource resource = new ClassPathResource("/assets/"+fileName);
      List<List<String>> records = csvComponent.readCSV(resource.getInputStream());
      records.stream().map(columns -> String.join(",", columns)).forEach(System.out::println);
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}
