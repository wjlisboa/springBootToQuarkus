package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.model.dto.request.MemberRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Funcionários", description = "Funcionalidade para pesquisar, associar e desassociar funcionários nos projetos")
@Validated
public interface MemberResource {

    @Operation(summary = "Retornar Projetos filtrando pelo CPF")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/findByCpf")
    List<ProjectResponse> findMembersByCpf(@RequestParam final String cpf);

    @Operation(summary = "Associar uma pessoa a um determinado projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Associação criada com sucesso", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Pessoa ou Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/associateMember")
    @ResponseStatus(code = HttpStatus.CREATED)
    ProjectResponse associateMember(@RequestBody @Valid final MemberRequest request);

    @Operation(summary = "Desassociar uma pessoa de um determinado projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Desassociação realizada com sucesso", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Pessoa ou Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/disassociateMember")
    ProjectResponse disassociateMember(@RequestBody @Valid final MemberRequest request);
}