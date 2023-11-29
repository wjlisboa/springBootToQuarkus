package com.desbravador.desafioJava.service;

import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.model.Member;
import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.repository.MemberRepository;
import com.desbravador.desafioJava.service.impl.MemberServiceImpl;
import com.desbravador.desafioJava.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

  private static final Long ID = new Random().nextLong();
  private static final Long PROJECT_ID = new Random().nextLong();
  private static final String PROJECT_NAME = UUID.randomUUID().toString();
  private static final Long PERSON_ID = new Random().nextLong();
  private static final String PERSON_CPF = UUID.randomUUID().toString();
  private static final String PERSON_NAME = UUID.randomUUID().toString();
  private static final String NOT_EMPLOYEE = "não é funcionário";
  private static final String MEMBER_EXISTING = "já é funcionário do projeto";

  @Mock private MemberRepository repository;
  @Mock private ProjectService projectService;
  @Mock private PersonService personService;

  @InjectMocks private MemberServiceImpl service;

  @Test
  void should_find_projects_by_cpf_of_member() {
    when(repository.findByPerson_Cpf(PERSON_CPF))
            .thenReturn(List.of(getMockMember()));

    var result = service.findMembersByCpf(PERSON_CPF);

    assertNotNull(result);
    assertTrue(result.stream().allMatch(project -> Objects.nonNull(project.getId())));
    var project = result.get(0);
    assertNotNull(project.getNome());
    assertNotNull(project.getMembros());
    assertFalse(project.getMembros().isEmpty());
    assertTrue(result.stream().allMatch(projects -> Objects.nonNull(projects.getGerente())));

    assertTrue(result.stream().flatMap(projects -> projects.getMembros().stream())
                                .anyMatch(member -> PERSON_CPF.equals(member.getPerson().getCpf())));

    verify(repository).findByPerson_Cpf(PERSON_CPF);
  }

  @Test
  void should_associate_new_member() {
    var mockMember = getMockMember();
    mockMember.setId(null);

    when(projectService.getProjectById(mockMember.getProject().getId()))
            .thenReturn(getMockProject());
    when(personService.getPersonByCpf(mockMember.getPerson().getCpf()))
            .thenReturn(getMockPerson());

    when(repository.findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId()))
            .thenReturn(Optional.empty());

    when(repository.save(mockMember)).thenReturn(mockMember);

    var project = service.associateMember(mockMember);

    assertNotNull(project);
    assertNotNull(project.getId());
    assertNotNull(project.getNome());
    assertNotNull(project.getMembros());
    assertFalse(project.getMembros().isEmpty());

    verify(projectService, times(2)).getProjectById(mockMember.getProject().getId());
    verify(personService).getPersonByCpf(mockMember.getPerson().getCpf());
    verify(repository).findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId());
    verify(repository).save(mockMember);
  }

  @Test
  void should_not_associate_new_member_not_employee() {
    var mockMember = getMockMember();
    mockMember.setId(null);

    when(projectService.getProjectById(mockMember.getProject().getId()))
            .thenReturn(getMockProject());
    var person = getMockPerson();
    person.setFuncionario(Boolean.FALSE);
    when(personService.getPersonByCpf(mockMember.getPerson().getCpf()))
            .thenReturn(person);

    var exception = assertThrows(ValidateException.class, () -> service.associateMember(mockMember));

    assertNotNull(exception);
    assertNotNull(exception.getMessage());
    assertTrue(exception.getMessage().contains(NOT_EMPLOYEE));

    verify(projectService).getProjectById(mockMember.getProject().getId());
    verify(personService).getPersonByCpf(mockMember.getPerson().getCpf());
    verify(repository, never()).findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId());
    verify(repository, never()).save(mockMember);
  }

  @Test
  void should_not_associate_new_member_already_existing() {
    var mockMember = getMockMember();
    mockMember.setId(null);

    when(projectService.getProjectById(mockMember.getProject().getId()))
            .thenReturn(getMockProject());
    when(personService.getPersonByCpf(mockMember.getPerson().getCpf()))
            .thenReturn(getMockPerson());

    when(repository.findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId()))
            .thenReturn(Optional.of(mockMember));

    var exception = assertThrows(ValidateException.class, () -> service.associateMember(mockMember));

    assertNotNull(exception);
    assertNotNull(exception.getMessage());
    assertTrue(exception.getMessage().contains(MEMBER_EXISTING));

    verify(projectService).getProjectById(mockMember.getProject().getId());
    verify(personService).getPersonByCpf(mockMember.getPerson().getCpf());
    verify(repository).findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId());
    verify(repository, never()).save(mockMember);
  }

  @Test
  void should_disassociate_member() {
    var mockMember = getMockMember();

    var mockProject = getMockProject();
    mockProject.setMembros(List.of());
    when(projectService.getProjectById(mockMember.getProject().getId()))
            .thenReturn(mockProject);

    when(repository.findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId()))
            .thenReturn(Optional.of(getMockMember()));

    doNothing().when(repository).delete(mockMember);

    var project = service.disassociateMember(mockMember);

    assertNotNull(project);
    assertNotNull(project.getId());
    assertNotNull(project.getNome());
    assertNotNull(project.getMembros());
    assertTrue(project.getMembros().isEmpty());

    verify(projectService).getProjectById(mockMember.getProject().getId());
    verify(repository).findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId());
    verify(repository).delete(mockMember);
  }

  @Test
  void should_not_disassociate_member_no_existing() {
    var mockMember = getMockMember();

    when(repository.findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId()))
            .thenReturn(Optional.empty());

    var exception = assertThrows(ValidateException.class, () -> service.disassociateMember(mockMember));

    assertNotNull(exception);
    assertNotNull(exception.getMessage());
    assertEquals(Constants.MEMBER_NOT_FOUND, exception.getMessage());

    verify(projectService, never()).getProjectById(mockMember.getProject().getId());
    verify(repository).findByPerson_CpfAndProject_Id(mockMember.getPerson().getCpf(), mockMember.getProject().getId());
    verify(repository, never()).delete(mockMember);
  }

  private Member getMockMember() {
    return Member.builder()
                    .id(ID)
                    .project(getMockProject())
                    .person(getMockPerson())
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
