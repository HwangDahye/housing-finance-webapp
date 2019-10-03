package com.hdh.housingfinancewebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TB_USER")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int userSeq;
  @Column(nullable = false)
  private String id;
  @Column(nullable = false)
  @JsonIgnore
  private String password;
  private String name;
  @JsonIgnore
  private String refreshToken;
  private LocalDateTime createDatetime;
}
