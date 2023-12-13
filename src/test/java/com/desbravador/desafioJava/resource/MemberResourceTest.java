package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.exceptionhandler.Error;
import com.desbravador.desafioJava.exceptionhandler.exception.NotFoundException;
import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.model.Member;
import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.model.dto.request.MemberRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import com.desbravador.desafioJava.service.MemberService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.desbravador.desafioJava.util.Constants.PERSON_NOT_EMPLOYEE;
import static com.desbravador.desafioJava.util.Constants.PROJECT_NOT_FOUND;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class MemberResourceTest {

  private static final Long ID = new Random().nextLong();
  private static final Long PROJECT_ID = new Random().nextLong();
  private static final String PROJECT_NAME = UUID.randomUUID().toString();
  private static final Long PERSON_ID = new Random().nextLong();
  private static final String PERSON_CPF = "01788975588";
  private static final String PERSON_NAME = UUID.randomUUID().toString();

  @InjectMock MemberService service;

  @Test
  void should_find_projects_by_cpf_of_member() {
    when(service.findMembersByCpf(PERSON_CPF))
            .thenReturn(List.of(getMockProject()));

    var projectResponses = executeFindByCpf();

    assertNotNull(projectResponses);
    assertFalse(projectResponses.isEmpty());
    assertEquals(PROJECT_ID, projectResponses.get(0).getId());
    assertEquals(PROJECT_NAME, projectResponses.get(0).getNome());
    verify(service).findMembersByCpf(PERSON_CPF);
  }

  @Test
  void should_not_find_projects_by_cpf_of_member() {
    when(service.findMembersByCpf(PERSON_CPF))
            .thenReturn(List.of());

    var projectResponses = executeFindByCpf();

    assertNotNull(projectResponses);
    assertTrue(projectResponses.isEmpty());
    verify(service).findMembersByCpf(PERSON_CPF);
  }


  @Test
  void should_associate_member() {
    var mockMember = Member.of(getMockRequest());
    when(service.associateMember(mockMember))
            .thenReturn(getMockProject());

    var project = given()
                    .contentType(ContentType.JSON)
                    .body(getMockRequest())
                    .when()
                    .post("/funcionarios/associateMember")
                    .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                    .body()
                    .jsonPath()
                    .getObject(".", ProjectResponse.class);

    assertNotNull(project);
    assertEquals(PROJECT_ID, project.getId());
    assertEquals(PROJECT_NAME, project.getNome());
    assertTrue(project.getFuncionarios().stream().anyMatch(personResponse -> PERSON_CPF.equals(personResponse.getCpf())));
    verify(service).associateMember(mockMember);
  }


  @Test
  void should_not_associate_member_with_nonexistent_project() {
    var errorMessage = String.format(PROJECT_NOT_FOUND, PROJECT_ID);
    when(service.associateMember(any(Member.class)))
            .thenThrow(new NotFoundException(errorMessage));

    var error = given()
                  .contentType(ContentType.JSON)
                  .body(getMockRequest())
                  .when()
                  .post("/funcionarios/associateMember")
                  .then()
                  .log().all()
                  .statusCode(HttpStatus.SC_NOT_FOUND)
                  .extract()
                  .body()
                  .jsonPath()
                  .getObject(".", Error.class);

    assertNotNull(error);
    assertEquals(errorMessage, error.getMessage());
    verify(service).associateMember(any());
  }


  @Test
  void should_not_associate_member_not_employee() {
    var errorMessage = String.format(PERSON_NOT_EMPLOYEE, PERSON_CPF, PERSON_NAME);
    when(service.associateMember(any(Member.class)))
            .thenThrow(new ValidateException(errorMessage));

    var error = given()
            .contentType(ContentType.JSON)
            .body(getMockRequest())
            .when()
            .post("/funcionarios/associateMember")
            .then()
            .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", Error.class);

    assertNotNull(error);
    assertEquals(errorMessage, error.getMessage());
    verify(service).associateMember(any());
  }

  @Test
  void should_not_associate_member_with_invalid_cpf() {

    given()
      .contentType(ContentType.JSON)
      .body(MemberRequest.builder()
              .idProjeto(PROJECT_ID)
              .cpf(UUID.randomUUID().toString())
              .build())
      .when()
      .post("/funcionarios/associateMember")
      .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST)
      .body("title", equalTo("Constraint Violation"))
      .body("violations", hasSize(1))
      .body("violations[0].field", equalTo("associateMember.arg0.cpf"))
      .body("violations[0].message", equalTo("número do registro de contribuinte individual brasileiro (CPF) inválido"));

    verify(service, never()).associateMember(any());
  }

  @Test
  void should_not_associate_member_with_empty_cpf() {

    given()
            .contentType(ContentType.JSON)
            .body(MemberRequest.builder()
                    .idProjeto(PROJECT_ID)
                    .build())
            .when()
            .post("/funcionarios/associateMember")
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("title", equalTo("Constraint Violation"))
            .body("violations", hasSize(1))
            .body("violations[0].field", equalTo("associateMember.arg0.cpf"))
            .body("violations[0].message", equalTo("O campo 'cpf' deve ser informado."));

    verify(service, never()).associateMember(any());
  }


  @Test
  void should_disassociate_member() {
    var mockMember = Member.of(getMockRequest());
    var mockProject = getMockProject();
    mockProject.setMembros(List.of());
    when(service.disassociateMember(mockMember))
            .thenReturn(mockProject);

    var project = given()
            .contentType(ContentType.JSON)
            .body(getMockRequest())
            .when()
            .delete("/funcionarios/disassociateMember")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", ProjectResponse.class);

    assertNotNull(project);
    assertEquals(PROJECT_ID, project.getId());
    assertEquals(PROJECT_NAME, project.getNome());
    assertFalse(project.getFuncionarios().stream().anyMatch(personResponse -> PERSON_CPF.equals(personResponse.getCpf())));
    verify(service).disassociateMember(mockMember);
  }

  @Test
  void should_not_disassociate_member_with_invalid_cpf() {

    given()
      .contentType(ContentType.JSON)
      .body(MemberRequest.builder()
              .idProjeto(PROJECT_ID)
              .cpf(UUID.randomUUID().toString())
              .build())
      .when()
      .delete("/funcionarios/disassociateMember")
      .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST)
      .body("title", equalTo("Constraint Violation"))
      .body("violations", hasSize(1))
      .body("violations[0].field", equalTo("disassociateMember.arg0.cpf"))
      .body("violations[0].message", equalTo("número do registro de contribuinte individual brasileiro (CPF) inválido"));

    verify(service, never()).disassociateMember(any());
  }

  private List<ProjectResponse> executeFindByCpf() {
    return given()
            .queryParam("cpf", PERSON_CPF)
            .when()
            .get("/funcionarios/findByCpf")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getList(".", ProjectResponse.class);
  }


  private MemberRequest getMockRequest() {
    return MemberRequest.builder()
            .idProjeto(PROJECT_ID)
            .cpf(PERSON_CPF)
            .build();
  }
  private Project getMockProject() {
    return Project.builder()
            .id(PROJECT_ID)
            .nome(PROJECT_NAME)
            .membros(getMockMembers())
            .gerente(getMockPerson())
            .build();
  }

  private Person getMockPerson() {
    return Person.builder()
            .id(PERSON_ID)
            .cpf(PERSON_CPF)
            .nome(PERSON_NAME)
            .funcionario(Boolean.TRUE)
            .gerente(Boolean.TRUE)
            .build();
  }

  private List<Member> getMockMembers() {
    return List.of(Member.builder()
            .id(ID)
            .person(Person.builder()
                    .id(PERSON_ID)
                    .cpf(PERSON_CPF)
                    .funcionario(Boolean.TRUE)
                    .build())
            .project(Project.builder()
                    .id(PROJECT_ID)
                    .build())
            .build());
  }

}
