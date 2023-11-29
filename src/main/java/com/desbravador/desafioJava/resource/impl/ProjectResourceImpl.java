package com.desbravador.desafioJava.resource.impl;

import com.desbravador.desafioJava.model.Project;
import com.desbravador.desafioJava.model.dto.request.CreateProjectRequest;
import com.desbravador.desafioJava.model.dto.request.UpdateProjectRequest;
import com.desbravador.desafioJava.model.dto.response.ProjectResponse;
import com.desbravador.desafioJava.resource.ProjectResource;
import com.desbravador.desafioJava.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/projetos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProjectResourceImpl implements ProjectResource {


    private final ProjectService service;

    @Override
    public List<ProjectResponse> findAllProjects() {
        return service.getProjects().stream().map(ProjectResponse::of).collect(Collectors.toList());
    }

    @Override
    public ProjectResponse findProjectById(final Long id) {
        return ProjectResponse.of(service.getProjectById(id));
    }


    @Override
    public  List<ProjectResponse> findProjectByName(final String name) {
        return service.getProjectByName(name).stream().map(ProjectResponse::of).collect(Collectors.toList());
    }

    @Override
    public ProjectResponse createProject(final CreateProjectRequest request) {
        return ProjectResponse.of(service.createProject(Project.of(request)));
    }

    @Override
    public ProjectResponse updateProject(final UpdateProjectRequest request) {
        return ProjectResponse.of(service.updateProject(Project.of(request)));
    }

    @Override
    public void deleteProject(Long id) {
        service.deleteProject(id);
    }
}