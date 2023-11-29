package com.desbravador.desafioJava.repository;

import com.desbravador.desafioJava.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
  List<Person> findByNome(String name);
  Optional<Person> findByCpf(String cpf);
}
