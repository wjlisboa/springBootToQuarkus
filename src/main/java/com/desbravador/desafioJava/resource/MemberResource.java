package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.mapper.ProjectMapper;
import com.desbravador.desafioJava.model.Member;
import com.desbravador.desafioJava.model.dto.request.MemberRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import com.desbravador.desafioJava.service.MemberService;
import io.quarkus.hibernate.validator.runtime.jaxrs.JaxrsEndPointValidated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Funcionários", description = "Funcionalidade para pesquisar, associar e desassociar funcionários nos projetos")
@JaxrsEndPointValidated
@RestController
@RequestMapping(value = "/funcionarios", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberResource {

    private final MemberService service;
    private final ProjectMapper projectMapper;

    @Operation(summary = "Retornar Projetos filtrando pelo CPF")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/findByCpf")
    public List<ProjectResponse> findMembersByCpf(@RequestParam String cpf) {
        return service.findMembersByCpf(cpf)
                .stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Associar uma pessoa a um determinado projeto")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Associação criada com sucesso", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Pessoa ou Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/associateMember")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProjectResponse associateMember(@RequestBody @Valid final MemberRequest request) {
        return projectMapper.toResponse(service.associateMember(Member.of(request)));
    }

    @Operation(summary = "Desassociar uma pessoa de um determinado projeto")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Desassociação realizada com sucesso", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Pessoa ou Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/disassociateMember")
    public ProjectResponse disassociateMember(@RequestBody @Valid final MemberRequest request) {
        return projectMapper.toResponse(service.disassociateMember(Member.of(request)));
    }
}