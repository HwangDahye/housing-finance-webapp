package com.hdh.housingfinancewebapp.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

// Servlet Filter 맨 앞에 붙여짐. 얘는 Spring Control 범위가 아님!
// 여기서 발생한 Exception들은 AuthEntryPoint에서 핸들링됨
// JwtTokenProvider를 이용해서, 헤더에서 토큰값 갖져오고, 토큰의 유효성 판단 진행함
public class JwtAuthenticationFilter extends GenericFilterBean {    // Jwt 토큰을 인증하는 필터
  // 이런 filter 들은 servlet container(was)에 의해서 control됩니다.
  // 이 것들은 spring과는 아무런 관련이 없는 것들이죠.
  // 따라서, spring framework에서 사용하는 DI 즉, @Autowired 같은 것들을 사용할 수 없습니다
  private JwtTokenProvider jwtTokenProvider;

  // Jwt Provier 주입
  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  // Request로 들어오는 Jwt Token의 유효성을 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

    if (token != null && jwtTokenProvider.validateToken(token)) {
      Authentication auth = jwtTokenProvider.getAuthentication(token);
      // 여기에 설정되는 인증서에는, user id,pw,seq, role이 포함되어 있다
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    // [개선점] token이 null이거나, validate에서 걸렸을 때, 예외처리 해줄걸
    filterChain.doFilter(request, response);
  }
}
