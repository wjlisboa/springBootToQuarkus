package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.exceptionhandler.Error;
import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.model.ProjectRiskEnum;
import com.desbravador.desafioJava.model.ProjectStatusEnum;
import com.desbravador.desafioJava.model.dto.request.CreateProjectRequest;
import com.desbravador.desafioJava.model.dto.request.UpdateProjectRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import com.desbravador.desafioJava.service.ProjectService;
import com.desbravador.desafioJava.util.Constants;
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

import static com.desbravador.desafioJava.util.Constants.PERSON_NOT_MANAGER;
import static com.desbravador.desafioJava.util.Constants.PROJECT_CAN_NOT_DELETE;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProjectResourceTest {

  private static final Long ID = new Random().nextLong();
  private static final String NAME = UUID.randomUUID().toString();

  private static final String OTHER_NAME = UUID.randomUUID().toString();
  private static final String DESCRIPTION = UUID.randomUUID().toString();

  private static final Long PERSON_ID = new Random().nextLong();
  private static final String PERSON_CPF = UUID.randomUUID().toString();
  private static final String PERSON_NAME = UUID.randomUUID().toString();

  @Captor
  ArgumentCaptor<Project> projectCaptor;

  @InjectMock
  ProjectService service;

  @BeforeEach
  public void setup() {
    // Inicialização do MockitoAnnotations para processar os campos anotados com @Mock, @InjectMocks e @Captor
    MockitoAnnotations.openMocks(this);
  }


  @Test
  void should_get_projects() {
    when(service.getProjects())
            .thenReturn(List.of(getMockProject()));

    var result = given()
            .when()
            .get("/projetos")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getList(".", ProjectResponse.class);

    assertNotNull(result);
    assertTrue(result.stream().allMatch(project -> Objects.nonNull(project.getId())));
    assertTrue(result.stream().anyMatch(project -> NAME.equals(project.getNome())));

    verify(service).getProjects();
  }

  @Test
  void should_get_project_by_id() {

    when(service.getProjectById(ID)).thenReturn(getMockProject());

    var result = given()
            .when()
            .get("/projetos/"+ID)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", ProjectResponse.class);

    assertsProject(result);

    verify(service).getProjectById(ID);
  }

  @Test
  void should_get_project_by_name() {
    when(service.getProjectByName(NAME)).thenReturn(List.of(getMockProject()));

    var result = given()
            .queryParam("name", NAME)
            .when()
            .get("/projetos/findByName")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getList(".", ProjectResponse.class);


    assertNotNull(result);
    assertsProject(result.get(0));

    verify(service).getProjectByName(NAME);
  }

  @Test
  void should_create_project() {
    when(service.createProject(projectCaptor.capture())).thenReturn(getMockProject());

    var result = given()
            .contentType(ContentType.JSON)
            .body(getMockCreateRequest())
            .when()
            .post("/projetos")
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", ProjectResponse.class);

    assertsProject(result);

    var project = projectCaptor.getValue();
    assertEquals(PERSON_CPF, project.getGerente().getCpf());
    assertEquals(NAME, project.getNome());

    verify(service).createProject(project);
  }

  @Test
  void should_not_create_project_without_manager() {
    var errorMessage = String.format(PERSON_NOT_MANAGER, PERSON_CPF, PERSON_NAME);
    when(service.createProject(any(Project.class))).thenThrow(new ValidateException(errorMessage));

    var error = given()
            .contentType(ContentType.JSON)
            .body(getMockCreateRequest())
            .when()
            .post("/projetos")
            .then()
            .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", Error.class);

    assertNotNull(error);
    assertEquals(errorMessage, error.getMessage());

    verify(service).createProject(any(Project.class));
  }
  @Test
  void should_delete_project() {
    doNothing().when(service).deleteProject(ID);

    given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/projetos/"+ID)
            .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);

    verify(service).deleteProject(ID);
  }

  @Test
  void should_not_delete_project_with_status() {
    var errorMessage = String.format(PROJECT_CAN_NOT_DELETE, NAME, ProjectStatusEnum.INICIADO);
    doThrow(new ValidateException(errorMessage)).when(service).deleteProject(ID);

    var error = given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/projetos/"+ID)
            .then()
            .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", Error.class);

    assertNotNull(error);
    assertEquals(errorMessage, error.getMessage());

    verify(service).deleteProject(ID);
  }

  @Test
  void should_test_null_pointer() {
    doThrow(new NullPointerException(Constants.INTERNAL_ERROR_MESSAGE)).when(service).deleteProject(ID);

    var error = given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/projetos/"+ID)
            .then()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", Error.class);

    assertNotNull(error);
    assertEquals(Constants.INTERNAL_ERROR_MESSAGE, error.getMessage());
  }

  @Test
  void should_update_project() {
    var updateMock = getMockProject();
    updateMock.setNome(OTHER_NAME);
    when(service.updateProject(projectCaptor.capture())).thenReturn(updateMock);

    var result = given()
            .contentType(ContentType.JSON)
            .body(getMockUpdateRequest())
            .when()
            .put("/projetos")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .jsonPath()
            .getObject(".", ProjectResponse.class);


    var project = projectCaptor.getValue();
    assertEquals(ID, project.getId());
    assertEquals(OTHER_NAME, project.getNome());

    assertEquals(project.getId(), result.getId());
    assertEquals(project.getNome(), result.getNome());

    verify(service).updateProject(project);
  }

  private void assertsProject(ProjectResponse result) {
    assertNotNull(result);
    assertEquals(ID, result.getId());
    assertEquals(NAME, result.getNome());
    assertEquals(DESCRIPTION, result.getDescricao());
    assertEquals(ProjectStatusEnum.EM_ANALISE, result.getStatus());
    assertNotNull(result.getGerente());
    assertEquals(PERSON_CPF, result.getGerente().getCpf());
  }

  private Project getMockProject() {
    return Project.builder()
                    .id(ID)
                    .nome(NAME)
                    .descricao(DESCRIPTION)
                    .status(ProjectStatusEnum.EM_ANALISE.name())
                    .risco(ProjectRiskEnum.BAIXO.name())
            .gerente(getMockPerson())
            .membros(List.of())
            .build();
  }

  private Person getMockPerson() {
    return Person.builder()
            .id(PERSON_ID)
            .cpf(PERSON_CPF)
            .nome(PERSON_NAME)
            .gerente(Boolean.TRUE)
            .build();
  }

  private CreateProjectRequest getMockCreateRequest() {
    return CreateProjectRequest.builder()
            .nome(NAME)
            .cpfGerente(PERSON_CPF)
            .build();
  }

  private UpdateProjectRequest getMockUpdateRequest() {
    return UpdateProjectRequest.builder()
            .id(ID)
            .nome(OTHER_NAME)
            .cpfGerente(PERSON_CPF)
            .build();
  }
}