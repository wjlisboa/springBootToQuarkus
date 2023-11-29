package com.desbravador.desafioJava.service;

import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.model.ProjectRiskEnum;
import com.desbravador.desafioJava.model.ProjectStatusEnum;
import com.desbravador.desafioJava.repository.ProjectRepository;
import com.desbravador.desafioJava.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

  private static final Long ID = new Random().nextLong();
  private static final String NAME = UUID.randomUUID().toString();
  private static final String DESCRIPTION = UUID.randomUUID().toString();

  private static final Long PERSON_ID = new Random().nextLong();
  private static final String PERSON_CPF = UUID.randomUUID().toString();
  private static final String PERSON_NAME = UUID.randomUUID().toString();
  private static final String NOT_MANAGER = "não é gerente";
  private static final String CAN_NOT_DELETE = "não pode ser excluído";

  @Mock private ProjectRepository repository;

  @Mock private PersonService personService;

  @InjectMocks private ProjectServiceImpl service;

  @Test
  void should_get_projects() {
    when(repository.findAll())
            .thenReturn(List.of(getMockProject()));

    var result = service.getProjects();

    assertNotNull(result);
    assertTrue(result.stream().allMatch(project -> Objects.nonNull(project.getId())));

    verify(repository).findAll();
  }

  @Test
  void should_get_project_by_id() {
    when(repository.findById(ID)).thenReturn(Optional.of(getMockProject()));

    var result = service.getProjectById(ID);

    assertNotNull(result);
    assertEquals(ID, result.getId());
    assertEquals(NAME, result.getNome());
    assertNotNull(result.getGerente());
    assertEquals(PERSON_CPF, result.getGerente().getCpf());

    verify(repository).findById(ID);
  }

  @Test
  void should_get_project_by_name() {
    when(repository.findByNome(NAME)).thenReturn(List.of(getMockProject()));

    var result = service.getProjectByName(NAME);

    assertNotNull(result);
    var project =  result.get(0);
    assertEquals(ID, project.getId());
    assertEquals(NAME, project.getNome());
    assertNotNull(project.getGerente());
    assertEquals(PERSON_CPF, project.getGerente().getCpf());

    verify(repository).findByNome(NAME);
  }

  @Test
  void should_create_project() {
    var mockProject = getMockProject();

    when(personService.getPersonByCpf(mockProject.getGerente().getCpf())).thenReturn(getMockPerson(Boolean.TRUE));
    when(repository.save(mockProject)).thenReturn(mockProject);

    var result = service.createProject(mockProject);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(NAME, result.getNome());
    assertNotNull(result.getGerente());
    assertEquals(PERSON_CPF, result.getGerente().getCpf());

    verify(personService).getPersonByCpf(mockProject.getGerente().getCpf());
    verify(repository).save(mockProject);
  }

  @Test
  void should_not_create_project_without_manager() {
    var mockProject = getMockProject();

    when(personService.getPersonByCpf(mockProject.getGerente().getCpf())).thenReturn(getMockPerson(Boolean.FALSE));

    var exception = assertThrows(ValidateException.class, () -> service.createProject(mockProject)) ;

    assertNotNull(exception);
    assertNotNull(exception.getMessage());
    assertTrue(exception.getMessage().contains(NOT_MANAGER));

    verify(personService).getPersonByCpf(mockProject.getGerente().getCpf());
    verify(repository, never()).save(mockProject);
  }
  @Test
  void should_delete_project() {
    var mockProject = getMockProject();
    when(repository.findById(ID)).thenReturn(Optional.of(mockProject));

    doNothing().when(repository).delete(mockProject);

    assertDoesNotThrow(() -> service.deleteProject(ID));

    verify(repository).delete(mockProject);
  }

  @Test
  void should_not_delete_project_with_status() {
    var mockProject = getMockProject();
    mockProject.setStatus(ProjectStatusEnum.INICIADO.name());
    when(repository.findById(ID)).thenReturn(Optional.of(mockProject));

    var exception = assertThrows(ValidateException.class, () -> service.deleteProject(ID)) ;

    assertNotNull(exception);
    assertNotNull(exception.getMessage());
    assertTrue(exception.getMessage().contains(CAN_NOT_DELETE));

    verify(repository, never()).delete(mockProject);
  }

  @Test
  void should_update_project() {
    var mockProject = getMockProject();
    when(repository.findById(ID)).thenReturn(Optional.of(mockProject));
    when(personService.getPersonByCpf(mockProject.getGerente().getCpf())).thenReturn(getMockPerson(Boolean.TRUE));

    var updatedProject = getMockProject();
    var localDate = LocalDate.now();
    updatedProject.setDataInicio(localDate);
    when(repository.save(updatedProject)).thenReturn(updatedProject);

    var result = service.updateProject(updatedProject);

    assertNotNull(result);
    assertEquals(ID, result.getId());
    assertEquals(NAME, result.getNome());
    assertEquals(localDate, result.getDataInicio());
    assertNotNull(result.getGerente());
    assertEquals(PERSON_CPF, result.getGerente().getCpf());

    verify(repository).findById(ID);
    verify(personService).getPersonByCpf(mockProject.getGerente().getCpf());
    verify(repository).save(updatedProject);
  }

  private Project getMockProject() {
    return Project.builder()
                    .id(ID)
                    .nome(NAME)
                    .descricao(DESCRIPTION)
                    .status(ProjectStatusEnum.EM_ANALISE.name())
                    .risco(ProjectRiskEnum.BAIXO.name())
            .gerente(getMockPerson(Boolean.TRUE))
            .build();
  }

  private Person getMockPerson(Boolean manager) {
    return Person.builder()
            .id(PERSON_ID)
            .cpf(PERSON_CPF)
            .nome(PERSON_NAME)
            .gerente(manager)
            .build();
  }
}
