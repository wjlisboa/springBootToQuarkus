package com.desbravador.desafioJava.service;

import com.desbravador.desafioJava.model.Person;

import java.util.List;

public interface PersonService {

  List<Person> getPersons();

  Person getPersonById(Long id);

  Person getPersonByCpf(String cpf);

  Person createPerson(Person person);

  List<Person> getPersonByName(String name);

  void deletePerson(Long id);

  Person updatePerson(Person person);
}
