package com.desbravador.desafioJava.service;

import com.desbravador.desafioJava.model.Member;
import com.desbravador.desafioJava.model.Project;

import java.util.List;

public interface MemberService {

  List<Project> findMembersByCpf(String cpf);

  Project associateMember(Member member);

  Project disassociateMember(Member member);
}
