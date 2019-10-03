package com.hdh.housingfinancewebapp.service;

import com.hdh.housingfinancewebapp.dto.request.SignUpReq;
import com.hdh.housingfinancewebapp.entity.User;
import com.hdh.housingfinancewebapp.exception.auth.SigninFailedException;
import com.hdh.housingfinancewebapp.exception.auth.UserNotFoundException;
import com.hdh.housingfinancewebapp.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  // User 정보 가져오기
  public User getUser(String id) throws UserNotFoundException {
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  public User getUser(String id, String password){
    User user = getUser(id);
    if(!passwordEncoder.matches(password, user.getPassword())){
      throw new SigninFailedException();
    }
    return user;
  }

  public User saveUser(SignUpReq signUpReq){
    return userRepository.save(new User(signUpReq.getId(), passwordEncoder.encode(signUpReq.getPassword()), signUpReq.getName()));
  }

  public User saveUser(User user){
    return userRepository.save(user);
  }

  public boolean isExistUser(String id){
    return userRepository.findById(id).isPresent();
  }
}
