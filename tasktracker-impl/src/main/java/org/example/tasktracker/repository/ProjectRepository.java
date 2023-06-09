package org.example.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.tasktracker.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByName(String name);
}
