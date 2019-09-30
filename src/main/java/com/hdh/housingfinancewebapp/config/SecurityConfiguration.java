package com.hdh.housingfinancewebapp.config;

import com.hdh.housingfinancewebapp.security.AuthEntryPoint;
import com.hdh.housingfinancewebapp.security.AuthProvider;
import com.hdh.housingfinancewebapp.security.CustomAccessDeniedHandler;
import com.hdh.housingfinancewebapp.security.JwtAuthenticationFilter;
import com.hdh.housingfinancewebapp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {   // 서버에 보안설정 적용
  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthProvider authProvider;

  @Autowired
  AuthEntryPoint authEntryPoint;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
        .and()
          .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
          .antMatchers("/").permitAll()
          .antMatchers("/api/auth/**").permitAll()
          .antMatchers("/h2-console/**").permitAll()
          .antMatchers("/api/finance/**").hasRole("USER")
          .antMatchers("/resources/**").permitAll().anyRequest().permitAll()
          .anyRequest().authenticated()
        .and()
          .csrf()
            .disable()
          .headers().frameOptions().disable()
        .and()
          .exceptionHandling()
            .authenticationEntryPoint(authEntryPoint)
        .and().exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .and()
          .authenticationProvider(authProvider)
          .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
  }
}
