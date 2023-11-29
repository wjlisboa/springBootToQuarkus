package com.desbravador.desafioJava.resource.impl;

import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.dto.request.CreatePersonRequest;
import com.desbravador.desafioJava.model.dto.request.UpdatePersonRequest;
import com.desbravador.desafioJava.model.dto.response.PersonResponse;
import com.desbravador.desafioJava.resource.PersonResource;
import com.desbravador.desafioJava.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/pessoas", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersonResourceImpl implements PersonResource {


    private final PersonService service;

    @Override
    public List<PersonResponse> findAllPersons() {
        return service.getPersons().stream().map(PersonResponse::of).collect(Collectors.toList());
    }

    @Override
    public PersonResponse findPersonById(final Long id) {
        return PersonResponse.of(service.getPersonById(id));
    }

    @Override
    public PersonResponse findPersonByCpf(String cpf) {
        return PersonResponse.of(service.getPersonByCpf(cpf));
    }


    @Override
    public  List<PersonResponse> findPersonByName(final String name) {
        return service.getPersonByName(name).stream().map(PersonResponse::of).collect(Collectors.toList());
    }

    @Override
    public PersonResponse createPerson(final CreatePersonRequest request) {
        return PersonResponse.of(service.createPerson(Person.of(request)));
    }

    @Override
    public PersonResponse updatePerson(final UpdatePersonRequest request) {
        return PersonResponse.of(service.updatePerson(Person.of(request)));
    }

    @Override
    public void deletePerson(Long id) {
        service.deletePerson(id);
    }
}