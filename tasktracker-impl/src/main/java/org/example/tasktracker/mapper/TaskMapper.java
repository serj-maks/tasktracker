package org.example.tasktracker.mapper;

import java.util.List;

import org.example.tasktracker.dto.task.TaskCreateDto;
import org.example.tasktracker.dto.task.TaskLiteResponseDto;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.task.TaskUpdateDto;
import org.example.tasktracker.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface TaskMapper {

    TaskLiteResponseDto toTaskLiteResponseDto(Task task);

    List<TaskLiteResponseDto> toTasksLiteResponseDto(List<Task> tasks);

    @Mapping(source = "project.id", target = "projectId")
    TaskResponseDto toTaskResponseDto(Task task);

    @Mapping(source = "project.id", target = "projectId")
    List<TaskResponseDto> toTasksResponseDto(List<Task> tasks);

    Task toTask(TaskCreateDto dto);

    Task toTask(TaskUpdateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    void updateTask(Task newTask, @MappingTarget Task oldTask);
}
