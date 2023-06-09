package org.example.tasktracker.dto.task;

import org.example.tasktracker.dto.task.enums.Priority;
import org.example.tasktracker.dto.task.enums.Status;

public record TaskResponseDto(Long id,
                              Status status,
                              Priority priority,
                              String description,
                              Long projectId) {

}
