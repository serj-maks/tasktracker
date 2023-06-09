package org.example.tasktracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.example.tasktracker.exception.AlreadyExistsException;
import org.example.tasktracker.exception.NotFoundException;
import org.example.tasktracker.repository.ProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.tasktracker.Project;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepositoryMock;

    @InjectMocks
    private ProjectService projectService;

    @AfterEach
    void tearDown() {
        reset(projectRepositoryMock);
    }

    @Test
    void getById_returnProject() {
        long id = 1L;
        Project expected = new Project()
            .setName("project01")
            .setDescription("project01 desc");

        when(projectRepositoryMock.findById(id))
            .thenReturn(Optional.of(expected));
        Project actual = projectService.getById(id);

        assertEquals(expected, actual);
        verify(projectRepositoryMock).findById(id);
    }

    @Test
    void getById_throwException() {
        assertThrows(NotFoundException.class, () -> projectService.getById(Long.MAX_VALUE));
    }

    @Test
    void save() {
        Project project = new Project()
            .setName("Sales")
            .setDescription("sales desc");

        when(projectRepositoryMock.save(project))
            .thenReturn(project);
        Project savedProject = projectService.create(project);

        assertEquals(project.getName(), savedProject.getName());
        verify(projectRepositoryMock).save(project);
    }

    @Test
    void save_whenUsersHasEqualNames_thenThrowException() {
        Project project = new Project()
            .setName("Sales")
            .setDescription("sales desc");

        when(projectRepositoryMock.existsByName(project.getName()))
            .thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> projectService.create(project));
    }
}
