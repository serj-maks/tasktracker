package org.example.tasktracker.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import org.example.tasktracker.IntegrationTest;
import org.example.tasktracker.Project;

class ProjectRepositoryTest extends IntegrationTest {

    @Test
    void save() {
        transactionTemplate.executeWithoutResult(s -> {
            Project project = project("Sales", "sales desc");
            Project savedProject = projectRepository.save(project);
            Project actualProject = projectRepository.findById(savedProject.getId()).get();
            assertEquals(savedProject, actualProject);
        });
    }

    @Test
    void save_shouldNotSaveDuplicateName() {
        projectRepository.save(project("Sales", "sales desc"));
        assertThrows(DataIntegrityViolationException.class, () -> projectRepository.save(project("Sales", "sales desc 2")));
    }

    private static Project project(String name, String description) {
        return new Project()
            .setName(name)
            .setDescription(description);
    }
}
