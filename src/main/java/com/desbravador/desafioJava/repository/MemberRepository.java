package com.desbravador.desafioJava.repository;

import com.desbravador.desafioJava.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  List<Member> findByPerson_Cpf(String cpf);

  Optional<Member> findByPerson_CpfAndProject_Id(String cpf, Long idProject);
}
