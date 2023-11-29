package com.desbravador.desafioJava.model.dto.response;

import com.desbravador.desafioJava.model.Member;
import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.model.ProjectRiskEnum;
import com.desbravador.desafioJava.model.ProjectStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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


  public static ProjectResponse of(Project project) {
    var response = builder().build();
    BeanUtils.copyProperties(project, response);
    response.setStatus(Optional.ofNullable(project.getStatus()).map(ProjectStatusEnum::valueOf).orElse(null));
    response.setRisco(Optional.ofNullable(project.getRisco()).map(ProjectRiskEnum::valueOf).orElse(null));
    response.setGerente(Optional.ofNullable(project.getGerente()).map(PersonResponse::of).orElse(null));
    response.setFuncionarios(getFuncionarios(project.getMembros()));

    return response;
  }

  private static List<PersonResponse> getFuncionarios(List<Member> members) {
    if (Objects.nonNull(members)) {
      return members.stream()
              .map(Member::getPerson)
              .map(PersonResponse::of)
              .collect(Collectors.toList());
    }
    return List.of();
  }
}
