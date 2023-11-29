package com.desbravador.desafioJava.service;

import com.desbravador.desafioJava.model.Project;

import java.util.List;

public interface ProjectService {

  List<Project> getProjects();

  Project getProjectById(Long id);

  Project createProject(Project project);

  List<Project> getProjectByName(String name);

  void deleteProject(Long id);

  Project updateProject(Project project);
}
