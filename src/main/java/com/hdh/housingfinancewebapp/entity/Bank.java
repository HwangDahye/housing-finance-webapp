package com.hdh.housingfinancewebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
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
  @Column(nullable = false)
  private String instituteCode;
  @Column(nullable = false)
  private String instituteName;
  @JsonIgnore
  private Timestamp createDatetime;
  @JsonIgnore
  private char useYn;

  public Bank(String instituteCode, String instituteName){
    this.instituteCode = instituteCode;
    this.instituteName = instituteName;
  }

  @PrePersist
  public void prePersist(){
    if(createDatetime == null)
      createDatetime = new Timestamp(new Date().getTime());
    if(useYn == '\u0000')
      useYn = 'Y';
  }
}
