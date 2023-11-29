package com.desbravador.desafioJava.model;


import com.desbravador.desafioJava.model.dto.request.CreatePersonRequest;
import com.desbravador.desafioJava.model.dto.request.UpdatePersonRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Data
@Entity(name = "Pessoa")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotNull
  private String nome;
  
  private LocalDate dataNascimento;
  private String cpf;
  private Boolean funcionario;
  private Boolean gerente;


  public static Person of(CreatePersonRequest createRequest) {
    var person = builder().build();
    BeanUtils.copyProperties(createRequest, person);
    return person;
  }

  public static Person of(UpdatePersonRequest updateRequest) {
    var person = builder().build();
    BeanUtils.copyProperties(updateRequest, person);
    return person;
  }
  
  
}
