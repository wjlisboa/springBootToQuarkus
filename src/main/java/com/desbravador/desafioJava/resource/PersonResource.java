package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.model.dto.request.CreatePersonRequest;
import com.desbravador.desafioJava.model.dto.request.UpdatePersonRequest;
import com.desbravador.desafioJava.model.dto.response.PersonResponse;
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


@Tag(name = "Pessoa", description = "Crud de pessoas")
@Validated
public interface PersonResource {


    @Operation(summary = "Retornar todas Pessoas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Não existem pessoas", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    List<PersonResponse> findAllPersons();

    @Operation(summary = "Retornar Pessoa filtrando pelo Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/{id}")
    PersonResponse findPersonById(@PathVariable final Long id);

    @Operation(summary = "Retornar Pessoa filtrando pelo CPF")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/findByCpf")
    PersonResponse findPersonByCpf(@RequestParam final String cpf);

    @Operation(summary = "Retornar pessoas filtrando pelo Nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/findByName")
    List<PersonResponse> findPersonByName(@RequestParam final String name);

    @Operation(summary = "Criar uma pessoa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pessoa Criada", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    PersonResponse createPerson(@RequestBody @Valid final CreatePersonRequest request);

    @Operation(summary = "Atualizar uma pessoa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pessoa Atualizada", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping
    PersonResponse updatePerson(@RequestBody @Valid final UpdatePersonRequest request);

    @Operation(summary = "Excluir uma pessoa")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pessoa Excluída", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deletePerson(@PathVariable final Long id);
}