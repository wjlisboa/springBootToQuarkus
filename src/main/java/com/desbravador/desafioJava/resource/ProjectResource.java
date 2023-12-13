package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.mapper.ProjectMapper;
import com.desbravador.desafioJava.model.dto.request.CreateProjectRequest;
import com.desbravador.desafioJava.model.dto.request.UpdateProjectRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import com.desbravador.desafioJava.service.ProjectService;
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


@Tag(name = "Projeto", description = "Crud de projetos")
@JaxrsEndPointValidated
@RestController
@RequestMapping(value = "/projetos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProjectResource {

    private final ProjectService service;
    private final ProjectMapper mapper;

    @Operation(summary = "Retornar todos Projetos")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Não existem projetos", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public List<ProjectResponse> findAllProjects() {
        return service.getProjects()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Retornar Projeto filtrando pelo Id")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/{id}")
    public ProjectResponse findProjectById(@PathVariable final Long id) {
        return mapper.toResponse(service.getProjectById(id));
    }

    
    @Operation(summary = "Retornar projetos filtrando pelo Nome")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/findByName")
    public List<ProjectResponse> findProjectByName(@RequestParam final String name) {
        return service.getProjectByName(name)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Criar um projeto")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Projeto Criado", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProjectResponse createProject(@RequestBody @Valid final CreateProjectRequest request) {
        return mapper.toResponse(service.createProject(mapper.toProject(request)));
    }

    @Operation(summary = "Atualizar um projeto")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Projeto Atualizado", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping
    public ProjectResponse updateProject(@RequestBody @Valid final UpdateProjectRequest request) {
        return mapper.toResponse(service.updateProject(mapper.toProject(request)));
    }

    @Operation(summary = "Excluir um projeto")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Projeto Excluido", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable final Long id) {
        service.deleteProject(id);
    }
}