package com.desbravador.desafioJava.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
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
public class CreatePersonRequest {

  @NotEmpty(message = "O campo 'nome' deve ser informado.")
  private String nome;
  private LocalDate dataNascimento;

  @CPF
  private String cpf;
  private Boolean funcionario;
  private Boolean gerente;
}
