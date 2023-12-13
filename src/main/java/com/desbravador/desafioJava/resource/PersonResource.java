package com.desbravador.desafioJava.resource;

import com.desbravador.desafioJava.mapper.PersonMapper;
import com.desbravador.desafioJava.model.dto.request.CreatePersonRequest;
import com.desbravador.desafioJava.model.dto.request.UpdatePersonRequest;
import com.desbravador.desafioJava.model.dto.response.PersonResponse;
import com.desbravador.desafioJava.service.PersonService;
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


@Tag(name = "Pessoa", description = "Crud de pessoas")
@JaxrsEndPointValidated
@RestController
@RequestMapping(value = "/pessoas", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersonResource {

    private final PersonService service;
    private final PersonMapper mapper;

    @Operation(summary = "Retornar todas Pessoas")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Não existem pessoas", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public List<PersonResponse> findAllPersons() {
        return service.getPersons()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Retornar Pessoa filtrando pelo Id")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/{id}")
    public PersonResponse findPersonById(@PathVariable final Long id) {
        return mapper.toResponse(service.getPersonById(id));
    }

    @Operation(summary = "Retornar Pessoa filtrando pelo CPF")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "/findByCpf")
    public PersonResponse findPersonByCpf(@RequestParam final String cpf) {
        return mapper.toResponse(service.getPersonByCpf(cpf));
    }

    @Operation(summary = "Retornar pessoas filtrando pelo Nome")
    @APIResponses({
            @APIResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/findByName")
    public List<PersonResponse> findPersonByName(@RequestParam final String name) {
        return service.getPersonByName(name)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Criar uma pessoa")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Pessoa Criada", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public PersonResponse createPerson(@RequestBody @Valid final CreatePersonRequest request) {
        return mapper.toResponse(service.createPerson(mapper.toPerson(request)));
    }

    @Operation(summary = "Atualizar uma pessoa")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Pessoa Atualizada", content = {
                    @Content(schema = @Schema(implementation = PersonResponse.class), mediaType = "application/json") }),
            @APIResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping
    public PersonResponse updatePerson(@RequestBody @Valid final UpdatePersonRequest request) {
        return mapper.toResponse(service.updatePerson(mapper.toPerson(request)));
    }

    @Operation(summary = "Excluir uma pessoa")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Pessoa Excluída", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "404", description = "Pessoa não existente", content = {
                    @Content(schema = @Schema()) }),
            @APIResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable final Long id){
        service.deletePerson(id);
    }
}