package com.hdh.housingfinancewebapp.login;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.entity.User;
import com.hdh.housingfinancewebapp.enums.ResponseCodeEnums;
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
public class RefreshTokenTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private final String SIGN_IN_URI = "/api/auth/signin";
  private final String REFRESH_TOKEN_URI = "/api/auth/refresh/token";
  private final String BEARER_TYPE = "Bearer";
  String id = "user1";
  String password = "test";
  String accessToken;
  String refreshToken;

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
    refreshToken = response.get("data").get("refreshToken").asText();
  }

  @Test
  public void reqRefreshToken() throws Exception{
    MvcResult result = mockMvc.perform(get(REFRESH_TOKEN_URI)
        .param("id", id)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE+" "+refreshToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    assertNotEquals(accessToken, response.get("data").get("token"));
    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));
  }

  @Test
  public void failCase_invalidRefreshToken() throws Exception{
    refreshToken = "invalidRefreshToken";
    MvcResult result = mockMvc.perform(get(REFRESH_TOKEN_URI)
        .param("id", id)
        .header(HttpHeaders.AUTHORIZATION, BEARER_TYPE+" "+refreshToken))
        .andExpect(status().isUnauthorized())   // 401 발생해야함
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    assertEquals(Integer.parseInt(response.get("code").asText()), ResponseCodeEnums.INVALID_TOKEN_EXCEPTION.getCode());    // 성공조건 : INVALID_TOKEN_EXCEPTION 코드
    assertThat(result.getResponse().getStatus(), is(HttpStatus.UNAUTHORIZED.value()));    // 성공조건 : 401
  }

  @Test
  public void failCase_invalidAuthHeader() throws Exception{
    MvcResult result = mockMvc.perform(get(REFRESH_TOKEN_URI)
        .param("id", id)
        .header(HttpHeaders.AUTHORIZATION, "jwt"+" "+refreshToken))
        .andExpect(status().isBadRequest())   // 400 발생해야함
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    assertEquals(Integer.parseInt(response.get("code").asText()), ResponseCodeEnums.INVALID_AUTH_HEADER_EXCEPTION.getCode());    // 성공조건 : INVALID_AUTH_HEADER_EXCEPTION 코드
    assertThat(result.getResponse().getStatus(), is(HttpStatus.BAD_REQUEST.value()));    // 성공조건 : 400
  }
}
