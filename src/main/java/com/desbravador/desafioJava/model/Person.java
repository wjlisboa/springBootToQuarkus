package com.desbravador.desafioJava.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity(name = "Pessoa")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotNull
  private String nome;

  @Column(name = "data_nascimento")
  private LocalDate dataNascimento;
  private String cpf;
  private Boolean funcionario;
  private Boolean gerente;
  
}
