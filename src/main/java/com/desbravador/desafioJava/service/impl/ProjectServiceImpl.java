package com.desbravador.desafioJava.service.impl;

import com.desbravador.desafioJava.exceptionhandler.exception.NotFoundException;
import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.model.ProjectStatusEnum;
import com.desbravador.desafioJava.repository.ProjectRepository;
import com.desbravador.desafioJava.service.PersonService;
import com.desbravador.desafioJava.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.desbravador.desafioJava.util.Constants.*;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository repository;

  private final PersonService personService;

  @Override
  public List<Project> getProjects() {
    return repository.findAll();
  }

  @Override
  public Project getProjectById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException(String.format(PROJECT_NOT_FOUND, id)));
  }

  @Override
  @Transactional
  public Project createProject(Project project) {
    project.setGerente(getGerente(project.getGerente().getCpf()));
    return repository.save(project);
  }

  @Override
  public List<Project> getProjectByName(String name) {
    return repository.findByNome(name);
  }

  @Override
  @Transactional
  public void deleteProject(Long id) {
    var project = repository.findById(id).orElseThrow(() -> new NotFoundException(String.format(PROJECT_NOT_FOUND_TO_DELETE, id)));
    validateProjectStatus(project);
    repository.delete(project);
  }

  @Override
  @Transactional
  public Project updateProject(Project project) {
    var existingProject =
            repository.findById(project.getId()).orElseThrow(() -> new NotFoundException(String.format(PROJECT_NOT_FOUND_TO_UPDATE, project.getId())));

    fillExistingProject(project, existingProject);

    return repository.save(existingProject);
  }

  private Person getGerente(String cpf) {
    var person = personService.getPersonByCpf(cpf);
    if (!person.getGerente()) {
      throw new ValidateException(String.format(PERSON_NOT_MANAGER, cpf, person.getNome()));
    }
    return person;
  }

  private void fillExistingProject(Project project, Project existingProject) {
    Optional.ofNullable(project.getGerente().getCpf()).ifPresent(cpf -> existingProject.setGerente(getGerente(cpf)));
    Optional.ofNullable(project.getDataInicio()).ifPresent(existingProject::setDataInicio);
    Optional.ofNullable(project.getDataPrevisaoFim()).ifPresent(existingProject::setDataPrevisaoFim);
    Optional.ofNullable(project.getDataFim()).ifPresent(existingProject::setDataFim);
    Optional.ofNullable(project.getNome()).ifPresent(existingProject::setNome);
    Optional.ofNullable(project.getDescricao()).ifPresent(existingProject::setDescricao);
    Optional.ofNullable(project.getStatus()).ifPresent(existingProject::setStatus);
    Optional.ofNullable(project.getOrcamento()).ifPresent(existingProject::setOrcamento);
    Optional.ofNullable(project.getRisco()).ifPresent(existingProject::setRisco);
  }

  private void validateProjectStatus(Project project) {
    var statusEnum = ProjectStatusEnum.valueOf(project.getStatus());
    if (List.of(ProjectStatusEnum.INICIADO, ProjectStatusEnum.EM_ANDAMENTO, ProjectStatusEnum.ENCERRADO).contains(statusEnum)) {
      throw new ValidateException(String.format(PROJECT_CAN_NOT_DELETE, project.getNome(), statusEnum));
    }
  }
}
