package com.desbravador.desafioJava.service.impl;

import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import com.desbravador.desafioJava.model.Member;
import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.repository.MemberRepository;
import com.desbravador.desafioJava.service.MemberService;
import com.desbravador.desafioJava.service.PersonService;
import com.desbravador.desafioJava.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.desbravador.desafioJava.util.Constants.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final PersonService personService;
  private final ProjectService projectService;
  private final MemberRepository repository;

  @Override
  public List<Project> findMembersByCpf(String cpf) {
    return repository.findByPerson_Cpf(cpf)
                        .stream()
                        .map(Member::getProject)
                        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Project associateMember(Member member) {
    var newMember = Member.builder()
                            .project(projectService.getProjectById(member.getProject().getId()))
                            .person(personService.getPersonByCpf(member.getPerson().getCpf()))
                            .build();
    validateMember(newMember);
    repository.save(newMember);
    return projectService.getProjectById(member.getProject().getId());
  }

  @Override
  public Project disassociateMember(Member member) {
    var existingMember = repository.findByPerson_CpfAndProject_Id(member.getPerson().getCpf(), member.getProject().getId());
    existingMember.ifPresentOrElse(
            repository::delete,
            () -> { throw new ValidateException(MEMBER_NOT_FOUND); });
    return projectService.getProjectById(member.getProject().getId());
  }

  private void validateMember(Member newMember) {
    if (!newMember.getPerson().getFuncionario()) {
      throw new ValidateException(String.format(PERSON_NOT_EMPLOYEE, newMember.getPerson().getCpf(), newMember.getPerson().getNome()));
    }
    var existingMember = repository.findByPerson_CpfAndProject_Id(newMember.getPerson().getCpf(), newMember.getProject().getId());
    existingMember.ifPresent(alreadyMember -> {
              throw new ValidateException(String.format(MEMBER_ALREADY_EXISTING,
                      alreadyMember.getPerson().getCpf(),
                      alreadyMember.getProject().getNome()));
            }
    );
  }

}
