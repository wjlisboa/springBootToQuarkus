package com.desbravador.desafioJava.model;


import com.desbravador.desafioJava.model.dto.request.MemberRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "Membros")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "idProjeto")
  private Project project;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "idPessoa")
  private Person person;

  public static Member of(MemberRequest request) {
    return builder()
            .project(Project.builder().id(request.getIdProjeto()).build())
            .person(Person.builder().cpf(request.getCpf()).build())
            .build();
  }
}
