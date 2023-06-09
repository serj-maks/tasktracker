package org.example.tasktracker.resource;

import java.util.List;

import org.example.tasktracker.dto.task.TaskCreateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.example.tasktracker.dto.task.TaskLiteResponseDto;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.task.TaskUpdateDto;

import jakarta.validation.Valid;

@RequestMapping("api/tasks")
public interface TaskResource {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<TaskLiteResponseDto> getAll();

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskResponseDto getById(@PathVariable long id);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TaskResponseDto create(@RequestBody @Valid TaskCreateDto dto);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskResponseDto update(@RequestBody @Valid TaskUpdateDto dto,
                           @PathVariable long id);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteById(@PathVariable long id);

    @PutMapping("/{taskId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    void assignToUser(@PathVariable("taskId") long taskId,
                      @PathVariable("userId") long userId);
}
