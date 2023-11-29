package com.desbravador.desafioJava.service;

import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.repository.PersonRepository;
import com.desbravador.desafioJava.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

  private static final Long ID = new Random().nextLong();
  private static final String NAME = UUID.randomUUID().toString();
  private static final String CPF = UUID.randomUUID().toString();
  private static final String ALREADY_EXISTING = "já cadastrada";

  @Mock private PersonRepository repository;

  @InjectMocks private PersonServiceImpl service;

  @Test
  void should_get_persons() {
    when(repository.findAll())
            .thenReturn(List.of(getMockPerson()));

    var result = service.getPersons();

    assertNotNull(result);
    assertTrue(result.stream().allMatch(person -> Objects.nonNull(person.getId())));

    verify(repository).findAll();
  }

  @Test
  void should_get_person_by_id() {
    when(repository.findById(ID)).thenReturn(Optional.of(getMockPerson()));

    var result = service.getPersonById(ID);

    assertNotNull(result);
    assertEquals(ID, result.getId());
    assertEquals(NAME, result.getNome());
    assertTrue(result.getGerente());

    verify(repository).findById(ID);
  }

  @Test
  void should_get_person_by_cpf() {
    when(repository.findByCpf(CPF)).thenReturn(Optional.of(getMockPerson()));

    var result = service.getPersonByCpf(CPF);

    assertNotNull(result);
    assertEquals(ID, result.getId());
    assertEquals(NAME, result.getNome());
    assertTrue(result.getGerente());

    verify(repository).findByCpf(CPF);
  }

  @Test
  void should_get_person_by_name() {
    when(repository.findByNome(NAME)).thenReturn(List.of(getMockPerson()));

    var result = service.getPersonByName(NAME);

    assertNotNull(result);
    var person =  result.get(0);
    assertEquals(ID, person.getId());
    assertEquals(NAME, person.getNome());
    assertTrue(person.getGerente());

    verify(repository).findByNome(NAME);
  }

  @Test
  void should_create_person() {
    var mockPerson = getMockPerson();

    when(repository.findByCpf(CPF)).thenReturn(Optional.empty());
    when(repository.save(mockPerson)).thenReturn(mockPerson);

    var result = service.createPerson(mockPerson);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(NAME, result.getNome());
    assertTrue(result.getGerente());

    verify(repository).findByCpf(CPF);
    verify(repository).save(mockPerson);
  }

  @Test
  void should_not_create_person_already_existing() {
    var mockPerson = getMockPerson();
    when(repository.findByCpf(CPF)).thenReturn(Optional.of(Person.builder().build()));

    var exception = assertThrows(ValidateException.class, () -> service.createPerson(mockPerson)) ;

    assertNotNull(exception);
    assertNotNull(exception.getMessage());
    assertTrue(exception.getMessage().contains(ALREADY_EXISTING));

    verify(repository).findByCpf(CPF);
    verify(repository, never()).save(mockPerson);
  }

  @Test
  void should_delete_person() {
    var mockPerson = getMockPerson();
    when(repository.findById(ID)).thenReturn(Optional.of(mockPerson));

    doNothing().when(repository).delete(mockPerson);

    assertDoesNotThrow(() -> service.deletePerson(ID));

    verify(repository).findById(ID);
    verify(repository).delete(mockPerson);
  }

  @Test
  void should_update_person() {
    var mockPerson = getMockPerson();
    when(repository.findById(ID)).thenReturn(Optional.of(mockPerson));

    var updatedPerson = getMockPerson();
    updatedPerson.setFuncionario(Boolean.TRUE);
    updatedPerson.setGerente(Boolean.FALSE);
    when(repository.save(updatedPerson)).thenReturn(updatedPerson);

    var result = service.updatePerson(updatedPerson);

    assertNotNull(result);
    assertEquals(ID, result.getId());
    assertEquals(NAME, result.getNome());
    assertTrue(result.getFuncionario());
    assertFalse(result.getGerente());

    verify(repository).findById(ID);
    verify(repository).save(updatedPerson);
  }

  private Person getMockPerson() {
    return Person.builder()
            .id(ID)
            .cpf(CPF)
            .nome(NAME)
            .gerente(Boolean.TRUE)
            .build();
  }
}
