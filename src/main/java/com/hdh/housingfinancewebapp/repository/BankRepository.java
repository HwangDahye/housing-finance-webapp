package com.hdh.housingfinancewebapp.repository;

import com.hdh.housingfinancewebapp.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, String> {
  Bank findByInstituteName(String instituteName);
}
