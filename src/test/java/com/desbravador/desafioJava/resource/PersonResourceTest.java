package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.exceptionhandler.Error;
import com.desbravador.desafioJava.exceptionhandler.exception.NotFoundException;
import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.dto.request.CreatePersonRequest;
import com.desbravador.desafioJava.model.dto.request.UpdatePersonRequest;
import com.desbravador.desafioJava.model.dto.response.PersonResponse;
import com.desbravador.desafioJava.service.PersonService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static com.desbravador.desafioJava.util.Constants.PERSON_ALREADY_EXISTING;
import static com.desbravador.desafioJava.util.Constants.PERSON_NOT_FOUND_TO_UPDATE;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class PersonResourceTest {

  private static final Long ID = new Random().nextLong();
  private static final String NAME = UUID.randomUUID().toString();
  private static final String CPF = "01788975588";

  @Captor
  ArgumentCaptor<Person> personCaptor;

  @InjectMock PersonService service;

  @BeforeEach
  public void setup() {
    // Inicialização do MockitoAnnotations para processar os campos anotados com @Mock, @InjectMocks e @Captor
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void should_get_persons() {
    when(service.getPersons())
            .thenReturn(List.of(getMockPerson()));

    var result = given()
            .when()
            .get("/pessoas")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getList(".", PersonResponse.class);

    assertNotNull(result);
    assertTrue(result.stream().allMatch(person -> Objects.nonNull(person.getId())));
    assertTrue(result.stream().anyMatch(person -> CPF.equals(person.getCpf())));

    verify(service).getPersons();
  }

  @Test
  void should_get_person_by_id() {
    when(service.getPersonById(ID)).thenReturn(getMockPerson());

    var result = given()
            .when()
            .get("/pessoas/"+ID)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", PersonResponse.class);

    assertsPerson(result);

    verify(service).getPersonById(ID);
  }



  @Test
  void should_get_person_by_cpf() {
    when(service.getPersonByCpf(CPF)).thenReturn(getMockPerson());

    var result = given()
            .queryParam("cpf", CPF)
            .when()
            .get("/pessoas/findByCpf")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", PersonResponse.class);

    assertsPerson(result);

    verify(service).getPersonByCpf(CPF);
  }

  @Test
  void should_get_person_by_name() {
    when(service.getPersonByName(NAME)).thenReturn(List.of(getMockPerson()));

    var result = given()
            .queryParam("name", NAME)
            .when()
            .get("/pessoas/findByName")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getList(".", PersonResponse.class);


    assertNotNull(result);
    assertsPerson(result.get(0));

    verify(service).getPersonByName(NAME);
  }

  @Test
  void should_create_person() {
    when(service.createPerson(personCaptor.capture())).thenReturn(getMockPerson());

    var result = given()
            .contentType(ContentType.JSON)
            .body(getMockCreateRequest())
            .when()
            .post("/pessoas")
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", PersonResponse.class);

    assertsPerson(result);

    var person = personCaptor.getValue();
    assertEquals(CPF, person.getCpf());
    assertEquals(NAME, person.getNome());

    verify(service).createPerson(person);
  }

  @Test
  void should_not_create_person_already_existing() {
    var errorMessage = String.format(PERSON_ALREADY_EXISTING, CPF);
    when(service.createPerson(any(Person.class))).thenThrow(new ValidateException(errorMessage));

    var error = given()
            .contentType(ContentType.JSON)
            .body(getMockCreateRequest())
            .when()
            .post("/pessoas")
            .then()
            .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", Error.class);

    assertNotNull(error);
    assertEquals(errorMessage, error.getMessage());

    verify(service).createPerson(any(Person.class));
  }

  @Test
  void should_delete_person() {
    doNothing().when(service).deletePerson(ID);

    given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/pessoas/"+ID)
            .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);

    verify(service).deletePerson(ID);
  }

  @Test
  void should_update_person() {
    var updateMock = getMockPerson();
    updateMock.setFuncionario(Boolean.TRUE);
    updateMock.setGerente(Boolean.FALSE);
    when(service.updatePerson(personCaptor.capture())).thenReturn(updateMock);

    var result = given()
            .contentType(ContentType.JSON)
            .body(getMockUpdateRequest())
            .when()
            .put("/pessoas")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", PersonResponse.class);


    var person = personCaptor.getValue();
    assertEquals(ID, person.getId());
    assertEquals(CPF, person.getCpf());
    assertEquals(NAME, person.getNome());

    assertEquals(person.getId(), result.getId());
    assertEquals(person.getCpf(), result.getCpf());
    assertEquals(person.getNome(), result.getNome());
    assertTrue(result.getFuncionario());
    assertFalse(result.getGerente());

    verify(service).updatePerson(person);
  }


  @Test
  void should_not_update_person() {
    var errorMessage = String.format(PERSON_NOT_FOUND_TO_UPDATE, ID);

    when(service.updatePerson(personCaptor.capture())).thenThrow(new NotFoundException(errorMessage));

    var error = given()
                  .contentType(ContentType.JSON)
                  .body(getMockUpdateRequest())
                  .when()
                  .put("/pessoas")
                  .then()
                  .statusCode(HttpStatus.SC_NOT_FOUND)
                  .extract()
                  .body()
                  .jsonPath()
                  .getObject(".", Error.class);


    var person = personCaptor.getValue();
    assertEquals(ID, person.getId());
    assertEquals(CPF, person.getCpf());
    assertEquals(NAME, person.getNome());
    assertTrue(person.getFuncionario());
    assertFalse(person.getGerente());

    assertNotNull(error);
    assertEquals(errorMessage, error.getMessage());

    verify(service).updatePerson(person);
  }



  private void assertsPerson(PersonResponse result) {
    assertNotNull(result);
    assertEquals(ID, result.getId());
    assertEquals(CPF, result.getCpf());
    assertEquals(NAME, result.getNome());
    assertTrue(result.getGerente());
  }

  private Person getMockPerson() {
    return Person.builder()
            .id(ID)
            .cpf(CPF)
            .nome(NAME)
            .gerente(Boolean.TRUE)
            .build();
  }

  private CreatePersonRequest getMockCreateRequest() {
    return CreatePersonRequest.builder()
            .nome(NAME)
            .cpf(CPF)
            .gerente(Boolean.TRUE)
            .build();
  }

  private UpdatePersonRequest getMockUpdateRequest() {
    return UpdatePersonRequest.builder()
            .id(ID)
            .nome(NAME)
            .cpf(CPF)
            .gerente(Boolean.FALSE)
            .funcionario(Boolean.TRUE)
            .build();
  }

}
