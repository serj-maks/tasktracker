package org.example.tasktracker.resource;

import java.util.List;

import org.example.tasktracker.dto.project.ProjectResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.example.tasktracker.dto.project.ProjectCreateDto;
import org.example.tasktracker.dto.task.TaskResponseDto;
import org.example.tasktracker.dto.user.UserResponseDto;

import jakarta.validation.Valid;

@RequestMapping("api/projects")
public interface ProjectResource {

    @RequestMapping(method = RequestMethod.GET)
//    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ProjectResponseDto> getAll();

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProjectResponseDto getById(@PathVariable long id);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProjectResponseDto create(@RequestBody @Valid ProjectCreateDto dto);

    @GetMapping("/{id}/users")
    @ResponseStatus(HttpStatus.OK)
    List<UserResponseDto> getAllUsers(@PathVariable long id);

    @GetMapping("/{id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    List<TaskResponseDto> getAllTasks(@PathVariable long id,
                                      @RequestParam int page,
                                      @RequestParam int size);
}
