package com.hdh.housingfinancewebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_BANK")
public class Bank {
  @Id
  @NotNull
  private String instituteCode;
  @NotNull
  private String instituteName;
  @JsonIgnore
  private Timestamp createDatetime;
  @JsonIgnore
  private char useYn;

  public Bank(String instituteCode, String instituteName){
    this.instituteCode = instituteCode;
    this.instituteName = instituteName;
  }
}
