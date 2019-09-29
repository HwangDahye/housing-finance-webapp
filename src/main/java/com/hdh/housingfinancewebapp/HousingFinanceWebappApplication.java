package com.hdh.housingfinancewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HousingFinanceWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(HousingFinanceWebappApplication.class, args);
	}

  @Bean
  public PasswordEncoder passwordEncoder() {
    // 패스워드는 무조건 단방향 암호화/해싱을 사용해야 한다
    // 양방향 암호화를 사용하게 되는 경우, db에 암호화된 비밀번호를 복호화하면 정보 다 털리는거 똑같기 때문
    // 그래서 비밀번호 차기 할 때, 인증 후 새 비밀번호를 입력하는것도, db에 있는 암호화된 값을 복호화 할수 없기에 새로 입력받는 개념임

    // PasswordEncoder는 여러 해시 함수가 잇는데 default로 bcrypt를 해싱함수를 사용함
    // 입력값이 같음에도 불구하고, 매번 다른값을 return 함
    // 그래서 equal이 아닌, matches 를 사용할것!
    // 출처 : https://gompangs.github.io/2019/02/27/PasswordEncoder/
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
