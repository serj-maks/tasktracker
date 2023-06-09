package org.example.tasktracker.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import org.example.tasktracker.Project;

class AlreadyExistsExceptionTest {

    @Test
    void alreadyExistsException_messageCheck() {
        Project project = new Project()
            .setName("Sales")
            .setDescription("sales desc");

        AlreadyExistsException exception = assertThrows(
            AlreadyExistsException.class, () -> {
                throw new AlreadyExistsException(Project.class, "name: " + project.getName());
            }
        );

        assertEquals("'Project' with 'name: Sales' already exists", exception.getMessage());
    }
}