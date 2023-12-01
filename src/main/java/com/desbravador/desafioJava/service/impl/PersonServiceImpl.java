package com.desbravador.desafioJava.service.impl;

import com.desbravador.desafioJava.exceptionhandler.exception.NotFoundException;
import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.mapper.PersonMapper;
import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.repository.PersonRepository;
import com.desbravador.desafioJava.service.PersonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.desbravador.desafioJava.util.Constants.*;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

  private final PersonRepository repository;
  private final PersonMapper mapper;

  @Override
  public List<Person> getPersons() {
    return repository.findAll();
  }

  @Override
  public Person getPersonById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException(String.format(PERSON_NOT_FOUND, id)));
  }

  @Override
  public Person getPersonByCpf(String cpf) {
    return repository.findByCpf(cpf).orElseThrow(() -> new NotFoundException(String.format(PERSON_NOT_FOUND, cpf)));
  }

  @Override
  @Transactional
  public Person createPerson(Person person) {
    validateExistingPerson(person);
    return repository.save(person);
  }

  @Override
  public List<Person> getPersonByName(String name) {
    return repository.findByNome(name);
  }

  @Override
  @Transactional
  public void deletePerson(Long id) {
    var person = repository.findById(id).orElseThrow(() -> new NotFoundException(String.format(PERSON_NOT_FOUND_TO_DELETE, id)));
    repository.delete(person);
  }

  @Override
  @Transactional
  public Person updatePerson(Person person) {
    var existingPerson =
            repository.findById(person.getId()).orElseThrow(() -> new NotFoundException(String.format(PERSON_NOT_FOUND_TO_UPDATE, person.getId())));
    mapper.fillPersonFromPerson(person, existingPerson);
    return repository.save(existingPerson);
  }

  private void validateExistingPerson(Person person) {
    var existingPerson = Optional.ofNullable(person.getCpf()).flatMap(repository::findByCpf);
    if (existingPerson.isPresent()) {
      throw new ValidateException(String.format(PERSON_ALREADY_EXISTING, person.getCpf()));
    }
  }

}
