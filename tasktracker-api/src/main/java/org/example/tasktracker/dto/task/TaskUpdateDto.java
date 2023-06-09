package org.example.tasktracker.dto.task;

import java.time.LocalDate;

import org.example.tasktracker.dto.task.enums.Status;
import org.example.tasktracker.dto.task.enums.Priority;

import jakarta.validation.constraints.NotBlank;

public record TaskUpdateDto(@NotBlank String title,
                            LocalDate deadline,
                            Priority priority,
                            Status status,
                            String description) {
}
