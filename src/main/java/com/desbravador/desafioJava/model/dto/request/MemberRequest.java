package com.desbravador.desafioJava.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequest {

  @CPF
  @NotEmpty(message = "O campo 'cpf' deve ser informado.")
  private String cpf;

  @NotNull(message = "O campo 'idProjeto' deve ser informado.")
  private Long idProjeto;
}
