package com.desbravador.desafioJava.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

  @Column(name = "data_inicio")
  private LocalDate dataInicio;

  @Column(name = "data_previsao_fim")
  private LocalDate dataPrevisaoFim;

  @Column(name = "data_fim")
  private LocalDate dataFim;

  private String descricao;

  private String status;

  private BigDecimal orcamento;

  private String risco;


  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_Gerente")
  private Person gerente;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<Member> membros;

}
