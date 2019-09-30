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
public class LoadTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private final String SIGN_IN_URI = "/api/auth/signin";
  private final String LOAD_URI = "/api/finance/load";
  private final String BEARER_TYPE = "Bearer";
  String id = "user1";
  String password = "test";
  String accessToken;

  @Before
  public void login() throws Exception{
    MvcResult result = mockMvc.perform(get(SIGN_IN_URI)
        .param("id", id)
        .param("password", password))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    accessToken = response.get("data").get("token").asText();
  }

  @Test
  public void doLoad() throws Exception{
    MvcResult result = mockMvc.perform(post(LOAD_URI)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE + " "+ accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));    // 성공조건 : 200
  }

  @Test
  public void failCase_doNotAuthorization() throws Exception {
    MvcResult result = mockMvc.perform(post(LOAD_URI))
        .andExpect(status().isUnauthorized())
        .andDo(print())
        .andReturn();

    assertThat(result.getResponse().getStatus(), is(HttpStatus.UNAUTHORIZED.value()));    // 성공조건 : 401
  }
}
