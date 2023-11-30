package com.desbravador.desafioJava.mapper;

import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.dto.request.CreatePersonRequest;
import com.desbravador.desafioJava.model.dto.request.UpdatePersonRequest;
import com.desbravador.desafioJava.model.dto.response.PersonResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PersonMapper {

    PersonResponse toResponse(Person person);

    Person toPerson(CreatePersonRequest createPersonRequest);

    Person toPerson(UpdatePersonRequest updatePersonRequest);
}
