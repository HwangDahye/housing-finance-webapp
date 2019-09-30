package com.hdh.housingfinancewebapp.repository;

import com.hdh.housingfinancewebapp.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  // Java8의 Optional 기능을 통해 null 체크를 하기위해 사용해봄
  Optional<User> findById(String id);
}
