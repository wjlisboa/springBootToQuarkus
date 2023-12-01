package com.desbravador.desafioJava.mapper;

import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.model.dto.request.CreateProjectRequest;
import com.desbravador.desafioJava.model.dto.request.UpdateProjectRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PersonMapper.class})
public interface ProjectMapper {

    @Mapping(target = "funcionarios", expression = "java(project.getMembros().stream().map(member -> member.getPerson()).map(personMapper::toResponse).toList())")
    ProjectResponse toResponse(Project project);


    @Mapping(source = "cpfGerente", target = "gerente.cpf")
    Project toProject(CreateProjectRequest request);


    @Mapping(source = "cpfGerente", target = "gerente.cpf")
    Project toProject(UpdateProjectRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gerente", ignore = true)
    @Mapping(target = "membros", ignore = true)
    void fillProjectFromProject(Project source, @MappingTarget Project target);

}
