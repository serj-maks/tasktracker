package org.example.tasktracker.service;

import java.util.List;

import org.example.tasktracker.exception.AlreadyExistsException;
import org.example.tasktracker.exception.NotFoundException;
import org.example.tasktracker.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.example.tasktracker.Project;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Project getById(long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Project.class, id));
    }

    @Transactional
    public Project create(Project project) throws AlreadyExistsException {
        if (projectRepository.existsByName(project.getName())) {
            throw new AlreadyExistsException(Project.class, "name: " + project.getName());
        }
        return projectRepository.save(project);
    }
}
