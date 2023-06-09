package org.example.tasktracker.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCreateDto(@NotBlank String title,
                            @NotNull Long projectId,
                            @NotNull Long creatorId) {

}
