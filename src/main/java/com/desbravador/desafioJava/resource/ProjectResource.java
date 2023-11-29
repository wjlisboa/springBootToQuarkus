package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.model.dto.request.CreateProjectRequest;
import com.desbravador.desafioJava.model.dto.request.UpdateProjectRequest;
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



@Tag(name = "Projeto", description = "Crud de projetos")
@Validated
public interface ProjectResource {


    @Operation(summary = "Retornar todos Projetos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Não existem projetos", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    List<ProjectResponse> findAllProjects();

    @Operation(summary = "Retornar Projeto filtrando pelo Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/{id}")
    ProjectResponse findProjectById(@PathVariable final Long id);

    
    @Operation(summary = "Retornar projetos filtrando pelo Nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/findByName")
    List<ProjectResponse> findProjectByName(@RequestParam final String name);

    @Operation(summary = "Criar um projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Projeto Criado", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    ProjectResponse createProject(@RequestBody @Valid final CreateProjectRequest request);

    @Operation(summary = "Atualizar um projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto Atualizado", content = {
                    @Content(schema = @Schema(implementation = ProjectResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping
    ProjectResponse updateProject(@RequestBody @Valid final UpdateProjectRequest request);

    @Operation(summary = "Excluir um projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Projeto Excluido", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Projeto não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteProject(@PathVariable final Long id);
}