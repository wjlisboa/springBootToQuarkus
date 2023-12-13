package com.desbravador.desafioJava.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonResponse {

  private Long id;
  private String nome;
  private LocalDate dataNascimento;
  private String cpf;
  private Boolean funcionario;
  private Boolean gerente;

}
