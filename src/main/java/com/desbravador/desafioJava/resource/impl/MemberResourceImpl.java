package com.desbravador.desafioJava.resource.impl;

import com.desbravador.desafioJava.model.Member;
import com.desbravador.desafioJava.model.dto.request.MemberRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import com.desbravador.desafioJava.resource.MemberResource;
import com.desbravador.desafioJava.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/funcionarios", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberResourceImpl implements MemberResource {

    private final MemberService service;

    @Override
    public List<ProjectResponse> findMembersByCpf(String cpf) {
        return service.findMembersByCpf(cpf).stream().map(ProjectResponse::of).collect(Collectors.toList());
    }

    @Override
    public ProjectResponse associateMember(MemberRequest request) {
        return ProjectResponse.of(service.associateMember(Member.of(request)));
    }

    @Override
    public ProjectResponse disassociateMember(MemberRequest request) {
        return ProjectResponse.of(service.disassociateMember(Member.of(request)));
    }
}