package org.example.tasktracker.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.example.tasktracker.dto.user.UserCreateDto;
import org.example.tasktracker.dto.user.UserResponseDto;
import org.example.tasktracker.dto.task.TaskResponseDto;

import jakarta.validation.Valid;

@RequestMapping("api/users")
public interface UserResource {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<UserResponseDto> getAll();

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserResponseDto getById(@PathVariable long id);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponseDto create(@RequestBody @Valid UserCreateDto dto);

    @GetMapping("/{id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    List<TaskResponseDto> getAllAssignedTasks(@PathVariable long id);

    @PutMapping("/{userId}/projects/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    void addToProject(@PathVariable("userId") long userId,
                      @PathVariable("projectId") long projectId);
}
