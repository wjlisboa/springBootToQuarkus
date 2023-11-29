package com.desbravador.desafioJava.model.dto.request;

import com.desbravador.desafioJava.model.ProjectRiskEnum;
import com.desbravador.desafioJava.model.ProjectStatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectRequest {

  @NotEmpty(message = "O campo 'nome' deve ser informado.")
  private String nome;

  private LocalDate dataInicio;
  private LocalDate dataPrevisaoFim;
  private LocalDate dataFim;
  private String descricao;
  private ProjectStatusEnum status;
  private BigDecimal orcamento;
  private ProjectRiskEnum risco;

  @NotNull(message = "O campo 'CPF do Gerente' deve ser informado.")
  private String cpfGerente;

}
