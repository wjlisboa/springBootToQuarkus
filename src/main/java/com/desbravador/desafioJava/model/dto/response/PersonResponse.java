package com.desbravador.desafioJava.model.dto.response;

import com.desbravador.desafioJava.model.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonResponse {

  private Long id;
  private String nome;
  private LocalDate dataNascimento;
  private String cpf;
  private Boolean funcionario;
  private Boolean gerente;

  public static PersonResponse of(Person person) {
    var response = builder().build();
    BeanUtils.copyProperties(person, response);
    return response;
  }
}
