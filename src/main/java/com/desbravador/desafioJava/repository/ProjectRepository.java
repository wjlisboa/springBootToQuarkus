package com.desbravador.desafioJava.repository;

import com.desbravador.desafioJava.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  List<Project> findByNome(String name);
}
