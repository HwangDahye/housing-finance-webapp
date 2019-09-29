package com.hdh.housingfinancewebapp.finance;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ServiceTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private final String SIGN_IN_URI = "/api/auth/signin";
  private final String LOAD_URI = "/api/finance/load";
  private final String GET_BANKS_URI = "/api/finance/banks";
  private final String GET_TOTAL_AMOUNT_URI = "/api/finance/total/amount";
  private final String GET_TOP_BANK_URI = "/api/finance/top";
  private final String GET_AVG_MIN_MAX_URI = "/api/finance/average/amount";
  private final String PREDICT_URI = "/api/finance/predict";
  private final String BEARER_TYPE = "Bearer";
  String id = "user1";
  String password = "test";
  String accessToken;

  @Before
  public void init() throws Exception{
    MvcResult result = mockMvc.perform(get(SIGN_IN_URI)
        .param("id", id)
        .param("password", password))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    accessToken = response.get("data").get("token").asText();

    mockMvc.perform(post(LOAD_URI)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " "+ accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
  }

  @Test
  public void getBanks() throws Exception{
    MvcResult result = mockMvc.perform(get(GET_BANKS_URI)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " "+ accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));    // 성공조건 : 200
  }

  @Test
  public void getTotalAmountOfYear() throws Exception {
    MvcResult result = mockMvc.perform(get(GET_TOTAL_AMOUNT_URI)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " "+ accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));    // 성공조건 : 200
  }

  @Test
  public void getTopBank() throws Exception {
    MvcResult result = mockMvc.perform(get(GET_TOP_BANK_URI)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " "+ accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));    // 성공조건 : 200
  }

  @Test
  public void getAvgMinMaxByDefault() throws Exception {
    MvcResult result = mockMvc.perform(get(GET_AVG_MIN_MAX_URI)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " "+ accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));    // 성공조건 : 200
  }

  @Test
  public void getAvgMinMaxByInput() throws Exception {
    String bank = "신한은행";
    MvcResult result = mockMvc.perform(get(GET_AVG_MIN_MAX_URI+"/"+bank)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " "+ accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));    // 성공조건 : 200
  }

  @Test
  public void doPredict() throws Exception{
    MvcResult result = mockMvc.perform(get(PREDICT_URI)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " "+ accessToken)
          .param("bank", "외환은행").param("month", "5"))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();
    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));    // 성공조건 : 200
  }
}
