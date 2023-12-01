package com.desbravador.desafioJava.model.dto.response;

import com.desbravador.desafioJava.model.ProjectRiskEnum;
import com.desbravador.desafioJava.model.ProjectStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {

  private Long id;
  private String nome;
  private LocalDate dataInicio;
  private LocalDate dataPrevisaoFim;
  private LocalDate dataFim;
  private String descricao;
  private ProjectStatusEnum status;
  private BigDecimal orcamento;
  private ProjectRiskEnum risco;
  private PersonResponse gerente;
  private List<PersonResponse> funcionarios;

}
