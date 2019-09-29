package com.hdh.housingfinancewebapp.login;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.housingfinancewebapp.dto.request.SignUpReq;
import com.hdh.housingfinancewebapp.enums.ResponseCodeEnums;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SignUpTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private final String SIGN_UP_URI = "/api/auth/signup";
  SignUpReq signUpReq;

  @Before
  public void init(){
    signUpReq = new SignUpReq("dahye1234", "dahye", "1234");
  }

  @Test
  public void doSignUp() throws Exception{
    MvcResult result = mockMvc.perform(post(SIGN_UP_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(signUpReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    assertThat(result.getResponse().getStatus(), is(HttpStatus.OK.value()));
  }

  @Test
  public void failCase_requestMethodType() throws Exception{
    MvcResult result = mockMvc.perform(get(SIGN_UP_URI)   // get으로 요청 시도
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(signUpReq)))
        .andExpect(status().isMethodNotAllowed())   // 405 발생 해야함
        .andDo(print())
        .andReturn();

    assertThat(result.getResponse().getStatus(), is(HttpStatus.METHOD_NOT_ALLOWED.value()));    // 성공조건 : 405
  }

  @Test
  public void failCase_invalidRequestBody() throws Exception{
    signUpReq.setPassword(null);    // NotBlank Validation 검증 시도

    MvcResult result = mockMvc.perform(post(SIGN_UP_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(signUpReq)))
        .andExpect(status().isBadRequest())   //  400 발생 해야함
        .andDo(print())
        .andReturn();

    assertThat(result.getResponse().getStatus(), is(HttpStatus.BAD_REQUEST.value()));   // 성공조건 : 400
  }

  @Test
  public void failCase_duplicatedUser()throws Exception{
    signUpReq.setId("user1");
    MvcResult result = mockMvc.perform(post(SIGN_UP_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(signUpReq)))
        .andExpect(status().isConflict())   //  409 발생함
        .andDo(print())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    assertEquals(Integer.parseInt(response.get("code").asText()), ResponseCodeEnums.DUPLICATED_USER_EXCEPTION.getCode());  // 성공조건 : DUPLICATED_USER_EXCEPTION 코드
    assertThat(result.getResponse().getStatus(), is(HttpStatus.CONFLICT.value()));
  }
}
