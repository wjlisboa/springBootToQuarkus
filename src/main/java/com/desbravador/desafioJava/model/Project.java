package com.desbravador.desafioJava.model;


import com.desbravador.desafioJava.model.dto.request.CreateProjectRequest;
import com.desbravador.desafioJava.model.dto.request.UpdateProjectRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
@Entity(name = "Projeto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"membros"})
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String nome;

  private LocalDate dataInicio;

  private LocalDate dataPrevisaoFim;

  private LocalDate dataFim;

  private String descricao;

  private String status;

  private BigDecimal orcamento;

  private String risco;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "idGerente")
  private Person gerente;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<Member> membros;


  public static Project of(CreateProjectRequest createRequest) {
    var project = builder().build();
    BeanUtils.copyProperties(createRequest, project);
    project.setStatus(Optional.ofNullable(createRequest.getStatus()).map(Enum::name).orElse(null));
    project.setRisco(Optional.ofNullable(createRequest.getRisco()).map(Enum::name).orElse(null));
    project.setGerente(Person.builder().cpf(createRequest.getCpfGerente()).build());
    return project;
  }

  public static Project of(UpdateProjectRequest updateRequest) {
    var project = builder().build();
    BeanUtils.copyProperties(updateRequest, project);
    project.setStatus(Optional.ofNullable(updateRequest.getStatus()).map(Enum::name).orElse(null));
    project.setRisco(Optional.ofNullable(updateRequest.getRisco()).map(Enum::name).orElse(null));
    project.setGerente(Person.builder().cpf(updateRequest.getCpfGerente()).build());
    return project;
  }
}
