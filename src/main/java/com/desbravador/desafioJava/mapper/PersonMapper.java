package com.desbravador.desafioJava.mapper;

import com.desbravador.desafioJava.model.Person;
import com.desbravador.desafioJava.model.dto.request.CreatePersonRequest;
import com.desbravador.desafioJava.model.dto.request.UpdatePersonRequest;
import com.desbravador.desafioJava.model.dto.response.PersonResponse;
import org.mapstruct.*;

@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {

    PersonResponse toResponse(Person person);

    Person toPerson(CreatePersonRequest createPersonRequest);

    Person toPerson(UpdatePersonRequest updatePersonRequest);

    @Mapping(target = "id", ignore = true)
    void fillPersonFromPerson(Person source, @MappingTarget Person target);
}
