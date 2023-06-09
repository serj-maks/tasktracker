package org.example.tasktracker.dto.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectCreateDto(@NotBlank String name,
                               String description) {

}
