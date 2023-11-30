package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.model.dto.request.CreateProjectRequest;
import com.desbravador.desafioJava.model.dto.request.UpdateProjectRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import io.quarkus.hibernate.validator.runtime.jaxrs.JaxrsEndPointValidated;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Tag(name = "Projeto", description = "Crud de projetos")
@JaxrsEndPointValidated
public interface ProjectResource {


    @Operation(summary = "Retornar todos Projetos")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Não existem projetos", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    List<ProjectResponse> findAllProjects();

    @Operation(summary = "Retornar Projeto filtrando pelo Id")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/{id}")
    ProjectResponse findProjectById(@PathVariable final Long id);

    
    @Operation(summary = "Retornar projetos filtrando pelo Nome")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/findByName")
    List<ProjectResponse> findProjectByName(@RequestParam final String name);

    @Operation(summary = "Criar um projeto")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Projeto Criado", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    ProjectResponse createProject(@RequestBody @Valid final CreateProjectRequest request);

    @Operation(summary = "Atualizar um projeto")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Projeto Atualizado", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping
    ProjectResponse updateProject(@RequestBody @Valid final UpdateProjectRequest request);

    @Operation(summary = "Excluir um projeto")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Projeto Excluido", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteProject(@PathVariable final Long id);
}