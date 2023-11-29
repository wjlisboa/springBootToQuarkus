package com.desbravador.desafioJava.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePersonRequest {

  @NotNull(message =  "O campo 'id' deve ser informado.")
  private Long id;

  private String nome;
  private LocalDate dataNascimento;

  @CPF
  private String cpf;
  private Boolean funcionario;
  private Boolean gerente;
}
