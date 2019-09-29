package com.hdh.housingfinancewebapp.login;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.enums.ResponseCodeEnums;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SignInTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private final String SIGN_IN_URI = "/api/auth/signin";
  String id = "user1";
  String password = "test";


  @Test
  public void doSignIn() throws Exception{
    MvcResult result = mockMvc.perform(get(SIGN_IN_URI)
        .param("id", id)
        .param("password", password))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    assertNotNull(response.get("data").get("token"));
    assertNotNull(response.get("data").get("refreshToken"));
    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));
  }

  @Test
  public void failCase_requestMethodType() throws Exception{
    MvcResult result = mockMvc.perform(post(SIGN_IN_URI)
        .param("id", id)
        .param("password", password))
        .andExpect(status().isMethodNotAllowed())   // 405 발생 해야 함
        .andDo(print())
        .andReturn();

    assertThat(result.getResponse().getStatus(), is(HttpStatus.METHOD_NOT_ALLOWED.value()));    // 성공조건 : 405
  }

  @Test
  public void failCase_invalidPassword() throws Exception{
    password = "invalidPassword";
    MvcResult result = mockMvc.perform(get(SIGN_IN_URI)
        .param("id", id)
        .param("password", password))
        .andExpect(status().isUnauthorized())   // 401 발생 해야 함
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    assertEquals(Integer.parseInt(response.get("code").asText()), ResponseCodeEnums.SIGN_IN_FAIL_EXCEPTION.getCode());  // 성공조건 : SIGN_IN_FAIL_EXCEPTION 코드
    assertThat(result.getResponse().getStatus(), is(HttpStatus.UNAUTHORIZED.value()));  // 성공조건 : 401
  }

  @Test
  public void failCase_notExistUser() throws Exception{
    id = "newUser";
    MvcResult result = mockMvc.perform(get(SIGN_IN_URI)
        .param("id", id)
        .param("password", password))
        .andExpect(status().isUnauthorized())   // 401 발생 해야 함
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    assertEquals(Integer.parseInt(response.get("code").asText()), ResponseCodeEnums.USER_NOT_FOUND_EXCEPTION.getCode());  // 성공조건 : USER_NOT_FOUND_EXCEPTION 코드
    assertThat(result.getResponse().getStatus(), is(HttpStatus.UNAUTHORIZED.value()));  // 성공조건 : 401
  }
}
